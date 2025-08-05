package com.example.Triple_clone.domain.accommodation.infra;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
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

        if (category != null && !category.isEmpty()) {
            mustQueries.add(QueryBuilders.term(t -> t.field("category").value(category)));
        }

        if (ratingMin != null) {
            mustQueries.add(QueryBuilders.range(r -> r.field("rating").gte(JsonData.of(ratingMin))));
        }
        if (ratingMax != null) {
            mustQueries.add(QueryBuilders.range(r -> r.field("rating").lte(JsonData.of(ratingMax))));
        }

        if (region != null && !region.isEmpty()) {
            mustQueries.add(QueryBuilders.term(t -> t.field("region").value(region)));
        }

        if (dayusePriceMin != null || dayusePriceMax != null) {
            List<Query> dayuseQueries = new ArrayList<>();
            if (dayusePriceMin != null) {
                dayuseQueries.add(QueryBuilders.range(r -> r.field("rooms.dayuse_price").gte(JsonData.of(dayusePriceMin))));
            }
            if (dayusePriceMax != null) {
                dayuseQueries.add(QueryBuilders.range(r -> r.field("rooms.dayuse_price").lte(JsonData.of(dayusePriceMax))));
            }

            mustQueries.add(QueryBuilders.nested(n -> n
                    .path("rooms")
                    .query(q -> q.bool(b -> b.must(dayuseQueries)))
            ));
        }

        if (dayuseAvailable != null && dayuseAvailable) {
            mustQueries.add(QueryBuilders.nested(n -> n
                    .path("rooms")
                    .query(q -> q.bool(b -> b
                            .must(QueryBuilders.exists(e -> e.field("rooms.dayuse_price")))
                            .mustNot(QueryBuilders.term(t -> t.field("rooms.dayuse_soldout").value(true)))
                    ))
            ));
        }

        if (hasDayuseDiscount != null && hasDayuseDiscount) {
            mustQueries.add(QueryBuilders.nested(n -> n
                    .path("rooms")
                    .query(q -> q.term(t -> t.field("rooms.has_dayuse_discount").value(true)))
            ));
        }

        if (stayPriceMin != null || stayPriceMax != null) {
            List<Query> stayQueries = new ArrayList<>();
            if (stayPriceMin != null) {
                stayQueries.add(QueryBuilders.range(r -> r.field("rooms.stay_price").gte(JsonData.of(stayPriceMin))));
            }
            if (stayPriceMax != null) {
                stayQueries.add(QueryBuilders.range(r -> r.field("rooms.stay_price").lte(JsonData.of(stayPriceMax))));
            }

            mustQueries.add(QueryBuilders.nested(n -> n
                    .path("rooms")
                    .query(q -> q.bool(b -> b.must(stayQueries)))
            ));
        }

        if (stayAvailable != null && stayAvailable) {
            mustQueries.add(QueryBuilders.nested(n -> n
                    .path("rooms")
                    .query(q -> q.bool(b -> b
                            .must(QueryBuilders.exists(e -> e.field("rooms.stay_price")))
                            .mustNot(QueryBuilders.term(t -> t.field("rooms.stay_soldout").value(true)))
                    ))
            ));
        }

        if (hasStayDiscount != null && hasStayDiscount) {
            mustQueries.add(QueryBuilders.nested(n -> n
                    .path("rooms")
                    .query(q -> q.term(t -> t.field("rooms.has_stay_discount").value(true)))
            ));
        }

        if (roomPriceMin != null || roomPriceMax != null) {
            List<Query> roomPriceQueries = new ArrayList<>();

            if (roomPriceMin != null) {
                roomPriceQueries.add(QueryBuilders.bool(b -> b
                        .should(QueryBuilders.range(r -> r.field("rooms.dayuse_price").gte(JsonData.of(roomPriceMin))))
                        .should(QueryBuilders.range(r -> r.field("rooms.stay_price").gte(JsonData.of(roomPriceMin))))
                        .minimumShouldMatch("1")
                ));
            }
            if (roomPriceMax != null) {
                roomPriceQueries.add(QueryBuilders.bool(b -> b
                        .should(QueryBuilders.range(r -> r.field("rooms.dayuse_price").lte(JsonData.of(roomPriceMax))))
                        .should(QueryBuilders.range(r -> r.field("rooms.stay_price").lte(JsonData.of(roomPriceMax))))
                        .minimumShouldMatch("1")
                ));
            }

            mustQueries.add(QueryBuilders.nested(n -> n
                    .path("rooms")
                    .query(q -> q.bool(b -> b.must(roomPriceQueries)))
            ));
        }

        if (roomCapacityMin != null || roomCapacityMax != null) {
            List<Query> capacityQueries = new ArrayList<>();
            if (roomCapacityMin != null) {
                capacityQueries.add(QueryBuilders.range(r -> r.field("rooms.capacity").gte(JsonData.of(roomCapacityMin))));
            }
            if (roomCapacityMax != null) {
                capacityQueries.add(QueryBuilders.range(r -> r.field("rooms.capacity").lte(JsonData.of(roomCapacityMax))));
            }

            mustQueries.add(QueryBuilders.nested(n -> n
                    .path("rooms")
                    .query(q -> q.bool(b -> b.must(capacityQueries)))
            ));
        }

        if (roomCheckoutTime != null && !roomCheckoutTime.isEmpty()) {
            mustQueries.add(QueryBuilders.nested(n -> n
                    .path("rooms")
                    .query(q -> q.term(t -> t.field("rooms.stay_checkout_time").value(roomCheckoutTime)))
            ));
        }

        Query finalQuery = mustQueries.isEmpty()
                ? QueryBuilders.matchAll().build()._toQuery()
                : Query.of(q -> q.bool(b -> b.must(mustQueries)));

        try {
            SearchRequest.Builder builder = new SearchRequest.Builder()
                    .index("accommodation")
                    .query(finalQuery)
                    .from((int) pageable.getOffset())
                    .size(pageable.getPageSize());

            if (sortOption != null) {
                builder.sort(s -> switch (sortOption) {
                    case REVIEW_DESC -> s.field(f -> f.field("review_count").order(co.elastic.clients.elasticsearch._types.SortOrder.Desc));
                    case RATING_DESC -> s.field(f -> f.field("rating").order(co.elastic.clients.elasticsearch._types.SortOrder.Desc));
                    case STAY_PRICE_ASC -> s.field(f -> f.field("min_stay_price").order(co.elastic.clients.elasticsearch._types.SortOrder.Asc));
                    case STAY_PRICE_DESC -> s.field(f -> f.field("min_stay_price").order(co.elastic.clients.elasticsearch._types.SortOrder.Desc));
                    default -> s;
                });
            }

            SearchRequest finalRequest = builder.build();

            SearchResponse<AccommodationDocument> response = elasticsearchClient.search(finalRequest, AccommodationDocument.class);

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
        try {
            SearchResponse<AccommodationDocument> response = elasticsearchClient.search(s -> s
                    .index("accommodation")
                    .query(q -> q
                            .bool(b -> b
                                    .should(sh -> sh
                                            .matchPhrasePrefix(m -> m
                                                    .field("name")
                                                    .query(prefix)
                                            )
                                    )
                                    .should(sh -> sh
                                            .match(m -> m
                                                    .field("name.ngram")
                                                    .query(prefix)
                                            )
                                    )
                                    .should(sh -> sh
                                            .match(m -> m
                                                    .field("name.edge")
                                                    .query(prefix)
                                            )
                                    )
                                    .minimumShouldMatch("1")
                            )
                    )
                    .size(10), AccommodationDocument.class);

            return response.hits().hits().stream()
                    .sorted((a, b) -> {
                        float scoreB = b.score() != null ? b.score().floatValue() : 0f;
                        float scoreA = a.score() != null ? a.score().floatValue() : 0f;
                        return Float.compare(scoreB, scoreA);
                    })
                    .map(Hit::source)
                    .map(AccommodationDocument::getName)
                    .distinct()
                    .limit(10)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("[ES 자동완성 오류] {}", e.getMessage());
            throw new RuntimeException("자동완성 검색 중 오류: " + e.getMessage(), e);
        }
    }
}