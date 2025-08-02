package com.example.Triple_clone.domain.accommodation.infra;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.domain.SortOption;
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
            String searchKeyword,
            String category,
            Float ratingMin, Float ratingMax,
            String region,
            Integer dayusePriceMin, Integer dayusePriceMax,
            Boolean dayuseAvailable,
            Boolean hasDayuseDiscount,
            Integer stayPriceMin, Integer stayPriceMax,
            Boolean stayAvailable,
            Boolean hasStayDiscount,
            Integer roomPriceMin, Integer roomPriceMax,
            Integer roomCapacityMin, Integer roomCapacityMax,
            String roomCheckoutTime,
            SortOption sortOption,
            Pageable pageable
    ) {
        List<Query> mustQueries = new ArrayList<>();

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            mustQueries.add(QueryBuilders.multiMatch(m -> m
                    .fields("name^5", "address", "region^2", "category", "intro", "info", "rooms.name^3")
                    .query(searchKeyword)
                    .fuzziness("AUTO")
                    .type(TextQueryType.BestFields)
            ));
        }

        Query finalQuery = mustQueries.isEmpty()
                ? QueryBuilders.matchAll().build()._toQuery()
                : Query.of(q -> q.bool(b -> b.must(mustQueries)));

        try {
            SearchRequest request = new SearchRequest.Builder()
                    .index("accommodation")
                    .query(finalQuery)
                    .from((int) pageable.getOffset())
                    .size(pageable.getPageSize())
                    .build();

            SearchResponse<AccommodationDocument> response = elasticsearchClient.search(request, AccommodationDocument.class);

            List<AccommodationDocument> results = response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            long total = response.hits().total() != null ? response.hits().total().value() : results.size();

            return new PageImpl<>(results, pageable, total);

        } catch (IOException e) {
            log.error("[ES 검색 오류] {}", e.getMessage());
            throw new RuntimeException("Elasticsearch 검색 오류: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> autocompleteName(String prefix) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
