package com.example.Triple_clone.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ShardStatistics;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.util.DateTime;
import com.example.Triple_clone.domain.entity.AccommodationDocument;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

import static co.elastic.clients.elasticsearch.searchable_snapshots.StatsLevel.Shards;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ESAccommodationRepositoryImplTest {

    @Mock
    private ElasticsearchClient elasticsearchClient;

    private ESAccommodationRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new ESAccommodationRepositoryImpl(elasticsearchClient);
    }

    @Test
    public void searchByConditionsFromES_정상작동_테스트() throws IOException {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        AccommodationDocument doc = new AccommodationDocument();
        doc.setName("테스트 숙소");
        doc.setLocal("서울");

        ShardStatistics shardStats = new ShardStatistics.Builder()
                .total(1)
                .successful(1)
                .skipped(0)
                .failed(0)
                .build();

        Hit<AccommodationDocument> hit = new Hit.Builder<AccommodationDocument>()
                .index("accommodation") // ✅ 필수!
                .id("1")
                .score(1.0)
                .source(doc)
                .build();

        TotalHits totalHits = new TotalHits.Builder()
                .value(1L)
                .relation(TotalHitsRelation.Eq) // ✅ 이거 필수!
                .build();

        HitsMetadata<AccommodationDocument> hitsMetadata = new HitsMetadata.Builder<AccommodationDocument>()
                .hits(List.of(hit))
                .total(totalHits) // relation 설정 필요 없음
                .build();

        SearchResponse<AccommodationDocument> mockResponse = new SearchResponse.Builder<AccommodationDocument>()
                .took(100L)
                .timedOut(false)
                .shards(shardStats)
                .hits(hitsMetadata)
                .build();

        when(elasticsearchClient.search((SearchRequest) any(), eq(AccommodationDocument.class)))
                .thenReturn(mockResponse);

        // when
        Page<AccommodationDocument> result = repository.searchByConditionsFromES(
                "서울", "테스트", "호텔", "10",
                "10000", "20000", "4.0", "true",
                "50000", "100000", "15:00", "false",
                pageable
        );

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("테스트 숙소");

        verify(elasticsearchClient, times(1)).search((SearchRequest) any(), eq(AccommodationDocument.class));
    }
}
