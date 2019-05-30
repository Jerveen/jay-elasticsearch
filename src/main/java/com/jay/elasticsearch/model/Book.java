package com.jay.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Description: 书籍实体类
 * @Author: xyw
 * @CreateDt: 2019-05-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private String id;

    private String bookName;

    private String authorName;

    private BigDecimal bookPrice;

    private String bookDescription;
}
