package com.example.Triple_clone;

import com.example.Triple_clone.configuration.ElasticSearchConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(ElasticSearchConfig.class)
class TripleCloneApplicationTests {
	@Test
	void contextLoads() {
	}
}
