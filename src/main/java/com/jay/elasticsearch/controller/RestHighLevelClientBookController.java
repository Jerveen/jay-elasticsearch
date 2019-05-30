package com.jay.elasticsearch.controller;

import lombok.extern.log4j.Log4j2;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: RestHighLevelClientf方式Controller
 * @Author: xyw
 * @CreateDt: 2019-05-30
 */
@RestController
@RequestMapping("/restHighLevelClientEs")
@Log4j2
public class RestHighLevelClientBookController {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
}