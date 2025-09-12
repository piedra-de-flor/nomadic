package com.example.Triple_clone.domain.accommodation.infra;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortMode;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.mapping.FieldType;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.util.ObjectBuilder;
import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.domain.SortOption;
import com.example.Triple_clone.domain.accommodation.web.dto.AutocompleteResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import co.elastic.clients.elasticsearch.core.search.Rescore;
import co.elastic.clients.elasticsearch.core.search.RescoreQuery;


import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ESAccommodationRepository {

    private final ElasticsearchClient elasticsearchClient;

    public List<AutocompleteResult> searchAccommodationNames(String query, int limit) {
        try {
            SearchResponse<AccommodationDocument> response = elasticsearchClient.search(s -> s
                    .index("accommodation")
                    .query(q -> q.bool(b -> b
                            .should(sh -> sh.matchPhrasePrefix(m -> m
                                    .field("name")
                                    .query(query)
                                    .boost(3.0f)
                            ))
                            .should(sh -> sh.match(m -> m
                                    .field("name.ngram")
                                    .query(query)
                                    .boost(2.0f)
                            ))
                            .should(sh -> sh.match(m -> m
                                    .field("name.edge_ngram")
                                    .query(query)
                                    .boost(1.5f)
                            ))
                            .should(sh -> sh.fuzzy(f -> f
                                    .field("name")
                                    .value(query)
                                    .fuzziness("AUTO")
                                    .boost(1.0f)
                            ))
                    ))
                    .size(limit), AccommodationDocument.class);

            return response.hits().hits().stream()
                    .map(hit -> AutocompleteResult.builder()
                            .text(hit.source().getName())
                            .type(determineMatchType(query, hit.source().getName()))
                            .score(hit.score())
                            .accommodationId(hit.source().getId())
                            .build())
                    .distinct() // 중복 제거
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("자동완성 검색 오류: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public String findBestMatch(String query) {
        try {
            if (query == null || query.trim().isEmpty()) return query;
            final String qstr = query.trim();

            SearchResponse<AccommodationDocument> resp = elasticsearchClient.search(s -> s
                            .index("accommodation")
                            .query(q -> q.multiMatch(m -> m
                                    .query(qstr)
                                    .type(co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType.MostFields)
                                    .fields("name^6", "name.ngram^4", "region^5", "address^2")
                                    .fuzziness("AUTO")
                                    .lenient(true)
                            ))
                            .sort(so -> so.score(sc -> sc.order(co.elastic.clients.elasticsearch._types.SortOrder.Desc)))
                            .size(1),
                    AccommodationDocument.class
            );

            if (!resp.hits().hits().isEmpty() && resp.hits().hits().get(0).source() != null) {
                var hit = resp.hits().hits().get(0);
                return hit.source().getName() != null ? hit.source().getName() : query;
            }

            SearchResponse<AccommodationDocument> fallback = elasticsearchClient.search(s -> s
                            .index("accommodation")
                            .query(q -> q.bool(b -> b
                                    .should(sh -> sh.term(t -> t.field("name.keyword").value(v -> v.stringValue(qstr)).boost(5.0f)))
                                    .should(sh -> sh.prefix(p -> p.field("name.keyword").value(qstr).caseInsensitive(true).boost(3.0f)))
                                    .should(sh -> sh.term(t -> t.field("region.keyword").value(v -> v.stringValue(qstr)).boost(4.0f)))
                                    .should(sh -> sh.prefix(p -> p.field("region.keyword").value(qstr).caseInsensitive(true).boost(2.0f)))
                                    .minimumShouldMatch("1")
                            ))
                            .sort(so -> so.score(sc -> sc.order(co.elastic.clients.elasticsearch._types.SortOrder.Desc)))
                            .size(1),
                    AccommodationDocument.class
            );

            if (!fallback.hits().hits().isEmpty() && fallback.hits().hits().get(0).source() != null) {
                return fallback.hits().hits().get(0).source().getName();
            }

            return query;

        } catch (Exception e) {
            log.error("최적 매칭 검색 오류: {}", e.getMessage(), e);
            return query;
        }
    }

    public List<String> getSimilarAccommodationNames(String accommodationName, int limit) {
        try {
            if (accommodationName == null || accommodationName.trim().isEmpty()) return Collections.emptyList();
            final String qstr = accommodationName.trim();

            SearchResponse<AccommodationDocument> resp = elasticsearchClient.search(s -> s
                            .index("accommodation")
                            .query(q -> q.bool(b -> b
                                    .should(sh -> sh.multiMatch(m -> m
                                            .query(qstr)
                                            .type(co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType.MostFields)
                                            .fields("name^6", "name.ngram^4")
                                            .fuzziness("AUTO")
                                            .lenient(true)
                                    ))
                                    // keyword 폴백(정확/접두)
                                    .should(sh -> sh.term(t -> t.field("name.keyword").value(v -> v.stringValue(qstr)).boost(4.0f)))
                                    .should(sh -> sh.prefix(p -> p.field("name.keyword").value(qstr).caseInsensitive(true).boost(3.0f)))
                                    .minimumShouldMatch("1")
                            ))
                            .sort(so -> so.score(sc -> sc.order(co.elastic.clients.elasticsearch._types.SortOrder.Desc)))
                            .size(Math.max(1, limit) + 5),
                    AccommodationDocument.class
            );

            return resp.hits().hits().stream()
                    .map(h -> h.source() != null ? h.source().getName() : null)
                    .filter(Objects::nonNull)
                    .filter(name -> !name.equalsIgnoreCase(accommodationName))
                    .distinct()
                    .limit(Math.max(1, limit))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("유사 호텔명 검색 오류: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<AccommodationDocument> searchAccommodationsWithSort(String query, SortOption sortOption, int page, int size) {
        try {
            SearchResponse<AccommodationDocument> response = elasticsearchClient.search(s -> s
                    .index("accommodation")
                    .query(q -> {
                        if (query == null || query.trim().isEmpty()) {
                            return q.matchAll(m -> m);
                        } else {
                            return q.bool(b -> b
                                    .should(sh -> sh.term(t -> t.field("name.keyword").value(query).boost(10.0f)))
                                    .should(sh -> sh.matchPhrasePrefix(m -> m.field("name").query(query).boost(5.0f)))
                                    .should(sh -> sh.fuzzy(f -> f.field("name").value(query).fuzziness("AUTO").boost(3.0f)))
                                    .should(sh -> sh.match(m -> m.field("name").query(query).boost(2.0f)))
                                    .should(sh -> sh.match(m -> m.field("address").query(query).boost(1.5f)))
                                    .should(sh -> sh.match(m -> m.field("region").query(query).boost(1.5f)))
                                    .should(sh -> sh.match(m -> m.field("amenities").query(query).boost(1.0f)))
                                    .minimumShouldMatch("1")
                            );
                        }
                    })
                    .sort(so -> so.score(sc -> sc.order(SortOrder.Desc)))
                    .sort(buildSortQuery(sortOption))
                    .from(page * size)
                    .size(size), AccommodationDocument.class);

            return response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("정렬 검색 오류: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private SortOptions sortTopLevel(String field, SortOrder order, FieldType unmapped) {
        return SortOptions.of(so -> so.field(f -> f
                .field(field)
                .order(order)
                .missing("_last")
                .unmappedType(unmapped)
        ));
    }

    private SortOptions sortRoomsPrice(String field, SortOrder order) {
        return SortOptions.of(so -> so.field(f -> {
            FieldSort.Builder fb = new FieldSort.Builder()
                    .field(field)
                    .order(order)
                    .missing("_last")
                    .mode(SortMode.Min)
                    .unmappedType(FieldType.Integer);

            fb = fb.nested(n -> n.path("rooms"));

            return (ObjectBuilder<FieldSort>) fb.build();
        }));
    }

    private co.elastic.clients.elasticsearch._types.SortOptions buildSortQuery(SortOption sortOption) {
        if (sortOption == null) {
            return SortOptions.of(s -> s.field(f -> f.field("id").order(SortOrder.Asc)));
        }
        return switch (sortOption) {
            case ID_ASC          -> SortOptions.of(s -> s.field(f -> f.field("id").order(SortOrder.Asc)));
            case REVIEW_DESC     -> sortTopLevel("review_count", SortOrder.Desc, FieldType.Integer);
            case RATING_DESC     -> sortTopLevel("rating",       SortOrder.Desc, FieldType.Float);
            case DAYUSE_PRICE_ASC  -> sortRoomsPrice("rooms.dayuse_price", SortOrder.Asc);
            case DAYUSE_PRICE_DESC -> sortRoomsPrice("rooms.dayuse_price", SortOrder.Desc);
            case STAY_PRICE_ASC    -> sortRoomsPrice("rooms.stay_price",   SortOrder.Asc);
            case STAY_PRICE_DESC   -> sortRoomsPrice("rooms.stay_price",   SortOrder.Desc);
            case ROOM_PRICE_ASC    -> sortTopLevel("min_stay_price", SortOrder.Asc,  FieldType.Integer);
            case ROOM_PRICE_DESC   -> sortTopLevel("min_stay_price", SortOrder.Desc, FieldType.Integer);
        };
    }

    private String determineMatchType(String query, String result) {
        if (result.toLowerCase().startsWith(query.toLowerCase())) {
            return "prefix";
        } else if (result.toLowerCase().contains(query.toLowerCase())) {
            return "partial";
        } else {
            return "fuzzy";
        }
    }
}