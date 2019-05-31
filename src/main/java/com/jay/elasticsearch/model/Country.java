package com.jay.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @Description: 城市实体类
 * @Author: xyw
 * @CreateDt: 2019-05-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * ElasticSearch索引对象必须标注@Document注解，indexName为索引名，type为索引类型
 * （PS：这是 ElasticSearch特性，同样的索引，可以分为不同的类型，来分别做索引）
 */
@Document(indexName = "country", type = "country")
public class Country {

    /**
     * 这里的id主键必须为String类型，且必须加@Id注解;
     * 否则创建索引时，无法将主键转换成索引id，这样索引id就是null
     */
    @Id
    private String id;

    private String countryName;

    private String countryDes;

    private String countryAdress;
}
