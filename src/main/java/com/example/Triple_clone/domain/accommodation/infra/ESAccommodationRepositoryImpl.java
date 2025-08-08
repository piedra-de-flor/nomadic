package com.example.Triple_clone.domain.accommodation.infra;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.web.dto.SearchParams;
import com.example.Triple_clone.domain.accommodation.web.dto.AutocompleteResult;
import com.example.Triple_clone.domain.accommodation.web.dto.SearchSuggestionResponse;
import com.example.Triple_clone.domain.accommodation.web.dto.TrendingSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ESAccommodationRepositoryImpl {

    private final ElasticsearchClient elasticsearchClient;

    public List<AutocompleteResult> smartAutocomplete(String prefix) {
        try {
            List<AutocompleteResult> results = new ArrayList<>();

            SearchResponse<AccommodationDocument> exactMatch = elasticsearchClient.search(s -> s
                    .index("accommodation")
                    .query(q -> q.matchPhrasePrefix(m -> m.field("name").query(prefix)))
                    .size(5), AccommodationDocument.class);

            exactMatch.hits().hits().forEach(hit -> {
                results.add(AutocompleteResult.builder()
                        .text(hit.source().getName())
                        .type("exact")
                        .score(hit.score())
                        .accommodationId(hit.source().getId())
                        .build());
            });

            SearchResponse<AccommodationDocument> ngramMatch = elasticsearchClient.search(s -> s
                    .index("accommodation")
                    .query(q -> q.bool(b -> b
                            .should(sh -> sh.match(m -> m.field("name.ngram").query(prefix)))
                            .should(sh -> sh.match(m -> m.field("name.edge").query(prefix)))
                    ))
                    .size(5), AccommodationDocument.class);

            ngramMatch.hits().hits().forEach(hit -> {
                if (results.stream().noneMatch(r -> r.getText().equals(hit.source().getName()))) {
                    results.add(AutocompleteResult.builder()
                            .text(hit.source().getName())
                            .type("partial")
                            .score(hit.score())
                            .accommodationId(hit.source().getId())
                            .build());
                }
            });

            SearchResponse<AccommodationDocument> regionMatch = elasticsearchClient.search(s -> s
                    .index("accommodation")
                    .query(q -> q.matchPhrasePrefix(m -> m.field("region").query(prefix)))
                    .size(3), AccommodationDocument.class);

            regionMatch.hits().hits().forEach(hit -> {
                String regionText = hit.source().getRegion() + " 지역";
                if (results.stream().noneMatch(r -> r.getText().contains(hit.source().getRegion()))) {
                    results.add(AutocompleteResult.builder()
                            .text(regionText)
                            .type("region")
                            .score(hit.score())
                            .build());
                }
            });

            SearchResponse<AccommodationDocument> roomMatch = elasticsearchClient.search(s -> s
                    .index("accommodation")
                    .query(q -> q.nested(n -> n
                            .path("rooms")
                            .query(nq -> nq.matchPhrasePrefix(m -> m.field("rooms.name").query(prefix)))
                    ))
                    .size(3), AccommodationDocument.class);

            roomMatch.hits().hits().forEach(hit -> {
                hit.source().getRooms().stream()
                        .filter(room -> room.getName().toLowerCase().startsWith(prefix.toLowerCase()))
                        .forEach(room -> {
                            String roomText = room.getName() + " (" + hit.source().getName() + ")";
                            if (results.stream().noneMatch(r -> r.getText().equals(roomText))) {
                                results.add(AutocompleteResult.builder()
                                        .text(roomText)
                                        .type("room")
                                        .score(hit.score())
                                        .accommodationId(hit.source().getId())
                                        .roomId(room.getId())
                                        .build());
                            }
                        });
            });

            SearchResponse<AccommodationDocument> fuzzyMatch = elasticsearchClient.search(s -> s
                    .index("accommodation")
                    .query(q -> q.fuzzy(f -> f
                            .field("name")
                            .value(prefix)
                            .fuzziness("AUTO")
                    ))
                    .size(3), AccommodationDocument.class);

            fuzzyMatch.hits().hits().forEach(hit -> {
                if (results.stream().noneMatch(r -> r.getText().equals(hit.source().getName()))) {
                    results.add(AutocompleteResult.builder()
                            .text(hit.source().getName())
                            .type("corrected")
                            .score(hit.score() * 0.8f)
                            .accommodationId(hit.source().getId())
                            .build());
                }
            });

            return results.stream()
                    .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                    .distinct()
                    .limit(10)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("스마트 자동완성 오류: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public SearchSuggestionResponse getSearchSuggestions(String query) {
        try {
            SearchSuggestionResponse response = new SearchSuggestionResponse();

            SearchResponse<AccommodationDocument> similarSearch = elasticsearchClient.search(s -> s
                    .index("accommodation")
                    .query(q -> q.moreLikeThis(mlt -> mlt
                            .fields("name", "address", "intro", "amenities")
                            .like(l -> l.text(query))
                            .minTermFreq(1)
                            .minDocFreq(1)
                            .maxQueryTerms(12)
                    ))
                    .size(5), AccommodationDocument.class);

            List<String> similarQueries = similarSearch.hits().hits().stream()
                    .map(hit -> hit.source().getName())
                    .distinct()
                    .collect(Collectors.toList());
            response.setSimilarQueries(similarQueries);

            SearchResponse<Void> popularTerms = elasticsearchClient.search(s -> s
                    .index("accommodation")
                    .size(0)
                    .aggregations("popular_categories", a -> a
                            .terms(t -> t.field("category").size(5))
                    )
                    .aggregations("popular_regions", a -> a
                            .terms(t -> t.field("region").size(5))
                    ), Void.class);

            if (popularTerms.aggregations() != null) {
                var categoryAgg = popularTerms.aggregations().get("popular_categories").sterms();
                var regionAgg = popularTerms.aggregations().get("popular_regions").sterms();

                List<String> popularSearches = new ArrayList<>();
                categoryAgg.buckets().array().forEach(bucket ->
                        popularSearches.add(bucket.key().stringValue() + " 숙소"));
                regionAgg.buckets().array().forEach(bucket ->
                        popularSearches.add(bucket.key().stringValue() + " 여행"));

                response.setPopularSearches(popularSearches);
            }

            List<String> contextSuggestions = generateContextSuggestions(query);
            response.setContextSuggestions(contextSuggestions);

            return response;

        } catch (IOException e) {
            log.error("검색 제안 생성 오류: {}", e.getMessage(), e);
            return new SearchSuggestionResponse();
        }
    }

    private List<String> generateContextSuggestions(String query) {
        List<String> suggestions = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        if (lowerQuery.contains("바다") || lowerQuery.contains("해변")) {
            suggestions.addAll(Arrays.asList(
                    "바다뷰 오션뷰 리조트",
                    "해변가 펜션 독채",
                    "바다 근처 수영장 있는 호텔",
                    "해변 도보 5분 숙소"
            ));
        }

        if (lowerQuery.contains("산") || lowerQuery.contains("자연")) {
            suggestions.addAll(Arrays.asList(
                    "산속 힐링 펜션",
                    "자연 속 글램핑",
                    "산뷰 리조트",
                    "계곡 근처 독채"
            ));
        }

        if (lowerQuery.contains("가족") || lowerQuery.contains("아이")) {
            suggestions.addAll(Arrays.asList(
                    "키즈 클럽 있는 리조트",
                    "가족 단위 펜션",
                    "어린이 놀이시설 호텔",
                    "유아 동반 가능 숙소"
            ));
        }

        if (lowerQuery.contains("커플") || lowerQuery.contains("로맨틱")) {
            suggestions.addAll(Arrays.asList(
                    "커플 전용 펜션",
                    "로맨틱 호텔 스위트룸",
                    "프라이빗 풀빌라",
                    "커플 스파 패키지"
            ));
        }

        return suggestions.stream().limit(5).collect(Collectors.toList());
    }

    public List<TrendingSearch> getTrendingSearches() {
        try {
            SearchResponse<AccommodationDocument> trending = elasticsearchClient.search(s -> s
                    .index("accommodation")
                    .query(q -> q.matchAll(m -> m))
                    .sort(sort -> sort.field(f -> f.field("review_count").order(SortOrder.Desc)))
                    .size(10), AccommodationDocument.class);

            return trending.hits().hits().stream()
                    .map(hit -> TrendingSearch.builder()
                            .keyword(hit.source().getName())
                            .category(hit.source().getCategory())
                            .region(hit.source().getRegion())
                            .searchCount(hit.source().getReviewCount())
                            .build())
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("트렌딩 검색어 조회 오류: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}