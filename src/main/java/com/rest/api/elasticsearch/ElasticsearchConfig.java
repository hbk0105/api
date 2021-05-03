package com.rest.api.elasticsearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.rest.api.elasticsearch")
@ComponentScan//(basePackages = {"com.rest.api.elasticsearch"})
public class ElasticsearchConfig {

    /*
        https://juntcom.tistory.com/137

        https://twofootdog.tistory.com/53

        https://mkyong.com/spring-boot/spring-boot-spring-data-elasticsearch-example/

        https://coding-start.tistory.com/165?category=757916

        https://gomip.tistory.com/19

        https://www.elastic.co/kr/downloads/past-releases/elasticsearch-7-10-1
    */
    @Bean
    public RestHighLevelClient client() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder().connectedTo("localhost:9200").build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }
}
