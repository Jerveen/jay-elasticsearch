package com.jay.elasticsearch.repository;

import com.jay.elasticsearch.model.Country;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description: CountryRepository方法查询类
 * @Author: xyw
 * @CreateDt: 2019-05-31
 */
@Repository("countrySearchRepository")
public interface CountrySearchRepository extends ElasticsearchRepository<Country, String> {
}
