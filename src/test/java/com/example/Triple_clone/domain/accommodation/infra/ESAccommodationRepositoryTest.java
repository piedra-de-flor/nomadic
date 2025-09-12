package com.example.Triple_clone.domain.accommodation.infra;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.domain.SortOption;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.*;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@Testcontainers
class ESAccommodationRepositoryTest {
    @Container
    static ElasticsearchContainer es = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:8.14.1")
            .withEnv("xpack.security.enabled", "false")
            .withEnv("discovery.type", "single-node");

    static final String INDEX = "accommodation";
    static ElasticsearchClient client;
    ESAccommodationRepository repo;
    List<String> insertedIds;

    @BeforeAll
    static void initClient() {
        RestClient lowClient = RestClient.builder(HttpHost.create(es.getHttpHostAddress()))
                .build();
        ElasticsearchTransport transport = new RestClientTransport(lowClient, new JacksonJsonpMapper());
        client = new ElasticsearchClient(transport);
    }

    @BeforeEach
    void setUp() throws Exception {
        boolean exists = client.indices().exists(ExistsRequest.of(b -> b.index(INDEX))).value();
        assumeTrue(exists, "테스트 실행을 위해 '" + INDEX + "' 인덱스가 미리 생성되어 있어야 합니다.");

        repo = new ESAccommodationRepository(client);
        insertedIds = new ArrayList<>();
    }

    @AfterEach
    void tearDown() throws Exception {
        for (String id : insertedIds) {
            client.delete(DeleteRequest.of(b -> b.index(INDEX).id(id)));
        }
        client.indices().refresh(r -> r.index(INDEX));
    }


    private void indexDoc(String id, AccommodationDocument doc) throws IOException {
        IndexResponse resp = client.index(IndexRequest.of(b -> b
                        .index(INDEX)
                        .id(id)
                        .document(doc)
        ));
        insertedIds.add(id);
        assertThat(resp.result()).isIn(Result.Created, Result.Updated);
    }

    private void refresh() throws IOException {
        client.indices().refresh(r -> r.index(INDEX));
    }

    @Test
    @DisplayName("정렬 검색 - ROOM_PRICE_ASC로 최소 숙박가 오름차순 정렬")
    void 정렬검색_ROOM_PRICE_ASC() throws Exception {
        AccommodationDocument a = AccommodationDocument.builder()
                .id(1).name("A호텔").region("서울").address("주소1")
                .minStayPrice(15000).build();
        AccommodationDocument b = AccommodationDocument.builder()
                .id(2).name("B호텔").region("부산").address("주소2")
                .minStayPrice(10000).build();

        indexDoc("1", a);
        indexDoc("2", b);
        refresh();

        var out = repo.searchAccommodationsWithSort(null, SortOption.ROOM_PRICE_ASC, 0, 10);

        assertThat(out).extracting(AccommodationDocument::getId).containsExactly(2L, 1L);
    }

    @Test
    @DisplayName("최적 매칭 - name.keyword가 일치하면 그대로 반환")
    void 최적매칭() throws Exception {
        AccommodationDocument exact = AccommodationDocument.builder().id(10).name("강남").build();
        AccommodationDocument other = AccommodationDocument.builder().id(11).name("강남호텔").build();

        indexDoc("10", exact);
        indexDoc("11", other);
        refresh();

        String best = repo.findBestMatch("강남");

        assertThat(best).isEqualTo("강남");
    }

    @Test
    @DisplayName("자동완성 - ngram/edge_ngram 기반으로 부분/접두 일치 반환")
    void 자동완성() throws Exception {
        AccommodationDocument g1 = AccommodationDocument.builder().id(21).name("강남호텔 스테이").build();
        AccommodationDocument g2 = AccommodationDocument.builder().id(22).name("강동호텔").build();
        AccommodationDocument h1 = AccommodationDocument.builder().id(23).name("홍대호텔").build();

        indexDoc("21", g1);
        indexDoc("22", g2);
        indexDoc("23", h1);
        refresh();

        var results = repo.searchAccommodationNames("강남", 5);

        assertThat(results).isNotEmpty();
        assertThat(results.stream().map(r -> r.getText()))
                .anyMatch(txt -> txt.contains("강남"));
        assertThat(results.get(0).getScore()).isNotNull();
    }
}
