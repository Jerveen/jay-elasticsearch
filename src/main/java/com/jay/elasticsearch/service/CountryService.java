package com.jay.elasticsearch.service;

import com.jay.elasticsearch.model.Country;

import java.util.List;

/**
 * @Description: CountryServiceImpl
 * @Author: xyw
 * @CreateDt: 2019-05-31
 */
public interface CountryService {

    /**
     * 添加文档
     *
     * @param country
     * @return
     */
    Country addCountry(Country country);

    /**
     * 更新文档
     *
     * @param country
     * @return
     */
    Country updateCountry(Country country);

    /**
     * 通过城市名搜索文档
     *
     * @param countryName
     * @return
     */
    List<Country> listCountryByCountryName(String countryName);

    /**
     * 通过id删除文档
     *
     * @param id
     */
    void deleteCountry(String id);
}
