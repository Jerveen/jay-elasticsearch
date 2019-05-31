package com.jay.elasticsearch.controller;

import com.jay.elasticsearch.model.Country;
import com.jay.elasticsearch.service.CountryService;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: ElasticsearchRepository方式简单CRUD
 * @Author: xyw
 * @CreateDt: 2019-05-30
 */
@RestController
@RequestMapping("/elasticsearchRepositoryEs")
@Log4j2
public class ElasticsearchRepositoryBookController {

    @Autowired
    private CountryService countryService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @GetMapping("/getCountryByCountryName")
    public List<Country> getCountry(String countryName) {
        List<Country> countryList = countryService.listCountryByCountryName(countryName);
        return countryList;
    }

    @PostMapping("/addCountryDoc")
    public Country addCountryDoc(@RequestBody Country country) {
        Country addCountry = countryService.addCountry(country);
        return addCountry;
    }

    /**
     * 根据id删除book
     *
     * @param id
     */
    @DeleteMapping("/deleteCountryDoc/{id}")
    public void deleteCountryDoc(@PathVariable(value = "id") String id) {
        countryService.deleteCountry(id);
    }

    /**
     * 更新文档
     */
    @PutMapping("/updateCountryDoc")
    public Country updateCountryDoc(@RequestBody Country country) {
        Country updateCountry = countryService.updateCountry(country);
        return updateCountry;
    }

    @GetMapping("/getCountryById")
    public List<Country> getCountryById(String id) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.queryStringQuery(id)).build();
        List<Country> countryList = elasticsearchTemplate.queryForList(searchQuery, Country.class);
        return countryList;
    }

}
