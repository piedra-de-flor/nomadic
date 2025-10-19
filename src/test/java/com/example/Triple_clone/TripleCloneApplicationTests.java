package com.example.Triple_clone;

import com.example.Triple_clone.configuration.ElasticSearchConfig;
import com.example.Triple_clone.configuration.TestMailConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({ElasticSearchConfig.class, TestMailConfig.class})
class TripleCloneApplicationTests {
	@Test
	void contextLoads() {
	}
}
