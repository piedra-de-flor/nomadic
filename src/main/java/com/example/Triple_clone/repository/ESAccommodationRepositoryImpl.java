package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.AccommodationDocument;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQuery;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class ESAccommodationRepositoryImpl implements ESAccommodationRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Page<AccommodationDocument> searchByConditionsFromES(
            String local, String name, String category, String discountRate,
            String startLentPrice, String endLentPrice, String score,
            String lentStatus, String startLodgmentPrice, String endLodgmentPrice,
            String enterTime, String lodgmentStatus, Pageable pageable) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (local != null && !local.isEmpty()) {
            boolQuery.must(QueryBuilders.matchQuery("local", local));
        }
        if (name != null && !name.isEmpty()) {
            boolQuery.must(QueryBuilders.matchQuery("name", name));
        }
        if (category != null && !category.isEmpty()) {
            boolQuery.must(QueryBuilders.matchQuery("category", category));
        }
        if (score != null && !score.isEmpty()) {
            boolQuery.must(QueryBuilders.rangeQuery("score").gte(Double.parseDouble(score)));
        }
        if (enterTime != null && !enterTime.isEmpty() && endLodgmentPrice != null && !endLodgmentPrice.isEmpty()) {
            boolQuery.must(QueryBuilders.rangeQuery("enterTime")
                    .gte(enterTime)
                    .lte(endLodgmentPrice));
        }
        if (discountRate != null && !discountRate.isEmpty()) {
            boolQuery.must(QueryBuilders.rangeQuery("lodgmentDiscountRate").gte(Long.parseLong(discountRate)));
            boolQuery.should(QueryBuilders.rangeQuery("lentDiscountRate").gte(Long.parseLong(discountRate)));
        }
        if (startLentPrice != null && !startLentPrice.isEmpty()) {
            boolQuery.must(QueryBuilders.rangeQuery("lentPrice").gte(Long.parseLong(startLentPrice)));
        }
        if (endLentPrice != null && !endLentPrice.isEmpty()) {
            boolQuery.must(QueryBuilders.rangeQuery("lentPrice").lte(Long.parseLong(endLentPrice)));
        }
        if (lentStatus != null && !lentStatus.isEmpty()) {
            boolQuery.must(QueryBuilders.termQuery("lentStatus", Boolean.parseBoolean(lentStatus)));
        }
        if (startLodgmentPrice != null && !startLodgmentPrice.isEmpty()) {
            boolQuery.must(QueryBuilders.rangeQuery("lodgmentPrice").gte(Long.parseLong(startLodgmentPrice)));
        }
        if (endLodgmentPrice != null && !endLodgmentPrice.isEmpty()) {
            boolQuery.must(QueryBuilders.rangeQuery("lodgmentPrice").lte(Long.parseLong(endLodgmentPrice)));
        }
        if (lodgmentStatus != null && !lodgmentStatus.isEmpty()) {
            boolQuery.must(QueryBuilders.termQuery("lodgmentStatus", Boolean.parseBoolean(lodgmentStatus)));
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(pageable)
                .build();

        SearchHits<AccommodationDocument> searchHits = elasticsearchOperations.search(searchQuery, AccommodationDocument.class);

        List<AccommodationDocument> results = searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        return new PageImpl<>(results, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), searchHits.getTotalHits());
    }
}