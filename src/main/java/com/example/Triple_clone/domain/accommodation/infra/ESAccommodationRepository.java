package com.example.Triple_clone.domain.accommodation.infra;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.domain.SortOption;
import com.example.Triple_clone.domain.accommodation.web.dto.AutocompleteResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
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
            SearchResponse<AccommodationDocument> response = elasticsearchClient.search(s -> s
                    .index("accommodation")
                    .query(q -> q.bool(b -> b
                            .should(sh -> sh.term(t -> t
                                    .field("name.keyword")
                                    .value(query)
                                    .boost(10.0f)
                            ))
                            .should(sh -> sh.fuzzy(f -> f
                                    .field("name")
                                    .value(query)
                                    .fuzziness("AUTO")
                                    .boost(5.0f)
                            ))
                            .should(sh -> sh.match(m -> m
                                    .field("name")
                                    .query(query)
                                    .boost(3.0f)
                            ))
                    ))
                    .size(1), AccommodationDocument.class);

            if (!response.hits().hits().isEmpty()) {
                return response.hits().hits().get(0).source().getName();
            }

            return query;

        } catch (IOException e) {
            log.error("최적 매칭 검색 오류: {}", e.getMessage(), e);
            return query;
        }
    }

    public List<String> getSimilarAccommodationNames(String accommodationName, int limit) {
        try {
            SearchResponse<AccommodationDocument> response = elasticsearchClient.search(s -> s
                    .index("accommodation")
                    .query(q -> q.moreLikeThis(mlt -> mlt
                            .fields("name")
                            .like(l -> l.text(accommodationName))
                            .minTermFreq(1)
                            .minDocFreq(1)
                            .maxQueryTerms(5)
                    ))
                    .size(limit), AccommodationDocument.class);

            return response.hits().hits().stream()
                    .map(hit -> hit.source().getName())
                    .filter(name -> !name.equals(accommodationName))
                    .collect(Collectors.toList());

        } catch (IOException e) {
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
                                    .should(sh -> sh.match(m -> m.field("name").query(query).boost(3.0f)))
                                    .should(sh -> sh.match(m -> m.field("address").query(query).boost(2.0f)))
                                    .should(sh -> sh.match(m -> m.field("region").query(query).boost(2.0f)))
                                    .should(sh -> sh.match(m -> m.field("amenities").query(query).boost(1.0f)))
                            );
                        }
                    })
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

    private co.elastic.clients.elasticsearch._types.SortOptions buildSortQuery(SortOption sortOption) {
        return switch (sortOption) {
            case ID_ASC -> co.elastic.clients.elasticsearch._types.SortOptions.of(s -> s
                    .field(f -> f.field("id").order(SortOrder.Asc)));

            case REVIEW_DESC -> co.elastic.clients.elasticsearch._types.SortOptions.of(s -> s
                    .field(f -> f.field("reviewCount").order(SortOrder.Desc)));

            case RATING_DESC -> co.elastic.clients.elasticsearch._types.SortOptions.of(s -> s
                    .field(f -> f.field("rating").order(SortOrder.Desc)));

            case DAYUSE_PRICE_ASC -> co.elastic.clients.elasticsearch._types.SortOptions.of(s -> s
                    .field(f -> f.field("rooms.dayusePrice").order(SortOrder.Asc).missing("_last")));

            case DAYUSE_PRICE_DESC -> co.elastic.clients.elasticsearch._types.SortOptions.of(s -> s
                    .field(f -> f.field("rooms.dayusePrice").order(SortOrder.Desc).missing("_last")));

            case STAY_PRICE_ASC -> co.elastic.clients.elasticsearch._types.SortOptions.of(s -> s
                    .field(f -> f.field("rooms.stayPrice").order(SortOrder.Asc).missing("_last")));

            case STAY_PRICE_DESC -> co.elastic.clients.elasticsearch._types.SortOptions.of(s -> s
                    .field(f -> f.field("rooms.stayPrice").order(SortOrder.Desc).missing("_last")));

            case ROOM_PRICE_ASC -> co.elastic.clients.elasticsearch._types.SortOptions.of(s -> s
                    .field(f -> f.field("minStayPrice").order(SortOrder.Asc).missing("_last")));

            case ROOM_PRICE_DESC -> co.elastic.clients.elasticsearch._types.SortOptions.of(s -> s
                    .field(f -> f.field("minStayPrice").order(SortOrder.Desc).missing("_last")));
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