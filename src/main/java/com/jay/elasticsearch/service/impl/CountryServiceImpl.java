package com.jay.elasticsearch.service.impl;

import com.jay.elasticsearch.model.Country;
import com.jay.elasticsearch.repository.CountrySearchRepository;
import com.jay.elasticsearch.service.CountryService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Description: CountryService实现类
 * @Author: xyw
 * @CreateDt: 2019-05-31
 */
@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountrySearchRepository countrySearchRepository;


    @Override
    public Country addCountry(Country country) {
        Country saveCountry = countrySearchRepository.save(country);
        return saveCountry;
    }

    @Override
    public Country updateCountry(Country country) {
        Country updateCountry = countrySearchRepository.save(country);
        return updateCountry;
    }

    @Override
    public List<Country> listCountryByCountryName(String countryName) {
        // 拼接查询条件
        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(countryName);
        // 执行查询
        Iterable<Country> searchResult = countrySearchRepository.search(builder);
        // 处理查询结果
        Iterator<Country> iterator = searchResult.iterator();
        List<Country> countryList = new ArrayList<>();
        while (iterator.hasNext()) {
            countryList.add(iterator.next());
        }
        return countryList;
    }

    @Override
    public void deleteCountry(String id) {
        countrySearchRepository.deleteById(id);
    }
}
