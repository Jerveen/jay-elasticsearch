package com.jay.elasticsearch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @Description: ElasticsearchRepository方式配置类
 * @Author: xyw
 * @CreateDt: 2019-05-30
 */
@Configuration
// 这个注解也可以在启动类上使用,效果一样
@EnableElasticsearchRepositories(basePackages = "com.jay.elasticsearch.repository")
public class ElasticsearchRepositoryConfig {
    // https://docs.spring.io/spring-data/elasticsearch/docs/3.0.7.RELEASE/reference/html/
    // https://www.elastic.co/guide/en/elasticsearch/client/java-api/5.6/index.html
}
