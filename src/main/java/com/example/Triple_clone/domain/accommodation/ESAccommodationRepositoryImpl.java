package com.example.Triple_clone.domain.accommodation;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ESAccommodationRepositoryImpl implements ESAccommodationRepository {

    private final ElasticsearchClient elasticsearchClient;

    @Override
    public Page<AccommodationDocument> searchByConditionsFromES(
            String local, String name, String category, String discountRate,
            String startLentPrice, String endLentPrice, String score,
            String lentStatus, String startLodgmentPrice, String endLodgmentPrice,
            String enterTime, String lodgmentStatus, Pageable pageable) {

        List<Query> mustQueries = new ArrayList<>();

        if (local != null && !local.isEmpty()) {
            mustQueries.add(QueryBuilders.term(t -> t.field("local.keyword").value(local)));
        }
        if (name != null && !name.isEmpty()) {
            mustQueries.add(QueryBuilders.match(m -> m.field("name").query(name)));
        }
        if (category != null && !category.isEmpty()) {
            mustQueries.add(QueryBuilders.term(t -> t.field("category.keyword").value(category)));
        }
        if (score != null && !score.isEmpty()) {
            mustQueries.add(QueryBuilders.range(r -> r.field("score").gte(JsonData.of(Double.parseDouble(score)))));
        }
        if (enterTime != null && !enterTime.isEmpty()) {
            mustQueries.add(QueryBuilders.range(r -> r.field("enter_time").gte(JsonData.of(enterTime))));
        }
        if (discountRate != null && !discountRate.isEmpty()) {
            long dr = Long.parseLong(discountRate);
            mustQueries.add(QueryBuilders.bool(b -> b
                    .should(QueryBuilders.range(r -> r.field("lodgment_discount_rate").gte(JsonData.of(dr))))
                    .should(QueryBuilders.range(r -> r.field("lent_discount_rate").gte(JsonData.of(dr))))
                    .minimumShouldMatch("1")
            ));
        }
        if (startLentPrice != null && !startLentPrice.isEmpty()) {
            mustQueries.add(QueryBuilders.range(r -> r.field("lent_price").gte(JsonData.of(Long.parseLong(startLentPrice)))));
        }
        if (endLentPrice != null && !endLentPrice.isEmpty()) {
            mustQueries.add(QueryBuilders.range(r -> r.field("lent_price").lte(JsonData.of(Long.parseLong(endLentPrice)))));
        }
        if (lentStatus != null && !lentStatus.isEmpty()) {
            mustQueries.add(QueryBuilders.term(t -> t.field("lent_status").value(Boolean.parseBoolean(lentStatus))));
        }
        if (startLodgmentPrice != null && !startLodgmentPrice.isEmpty()) {
            mustQueries.add(QueryBuilders.range(r -> r.field("lodgment_price").gte(JsonData.of(Long.parseLong(startLodgmentPrice)))));
        }
        if (endLodgmentPrice != null && !endLodgmentPrice.isEmpty()) {
            mustQueries.add(QueryBuilders.range(r -> r.field("lodgment_price").lte(JsonData.of(Long.parseLong(endLodgmentPrice)))));
        }
        if (lodgmentStatus != null && !lodgmentStatus.isEmpty()) {
            mustQueries.add(QueryBuilders.term(t -> t.field("lodgment_status").value(Boolean.parseBoolean(lodgmentStatus))));
        }

        BoolQuery boolQuery = new BoolQuery.Builder()
                .must(mustQueries)
                .build();

        try {
            SearchResponse<AccommodationDocument> response = elasticsearchClient.search(
                    s -> s.index("accommodation")
                            .query(q -> q.bool(boolQuery))
                            .from((int) pageable.getOffset())
                            .size(pageable.getPageSize()),
                    AccommodationDocument.class
            );

            List<AccommodationDocument> results = response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            long total = response.hits().total() != null ? response.hits().total().value() : results.size();

            return new PageImpl<>(results, pageable, total);

        } catch (IOException e) {
            log.error("Elasticsearch 검색 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("Elasticsearch 검색 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
