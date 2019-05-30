package com.jay.elasticsearch.controller;

import com.jay.elasticsearch.constant.EsConstant;
import com.jay.elasticsearch.model.Book;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @Description: TransportClient方式简单CRUD
 * @Author: xyw
 * @CreateDt: 2019-05-30
 */
@RestController
@RequestMapping("/transportClientEs")
@Log4j2
public class TransportClientBookController {

    @Autowired
    private TransportClient transportClient;

    /**
     * 根据id查询
     *
     * @param id
     */
    @GetMapping("/bookById")
    public ResponseEntity<Map<String, Object>> get(@RequestParam("id") String id) {
        GetResponse result = transportClient.prepareGet(EsConstant.ES_INDEX, EsConstant.ES_TYPE, id).get();
        return new ResponseEntity<>(result.getSource(), HttpStatus.OK);
    }

    /**
     * 根据bookNmae查询
     *
     * @param bookName
     */
    @GetMapping("/bookByBookName")
    public ResponseEntity<Map<String, Object>> getBookByName(@RequestParam("bookName") String bookName) {
//        SearchResponse searchResponse = this.transportClient.prepareSearch("book").setTypes("novel")
//                .setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("id", "123456"))
//                        .must(QueryBuilders.matchQuery("bookDescription", "测试")))
//                .get();
        SearchResponse searchResponse = this.transportClient.prepareSearch(EsConstant.ES_INDEX).setTypes((EsConstant.ES_TYPE))
                .setQuery(QueryBuilders.matchQuery("bookDescription", "成功"))
                .get();
        // 查询的总数(命中数)
        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.getTotalHits();
        System.out.println("总记录数: " + totalHits);
        // 遍历查询的结果
        Iterator<SearchHit> iterator = hits.iterator();
        Map<String, Object> source = new HashMap<>();
        while (iterator.hasNext()) {
            source.putAll(iterator.next().getSource());
        }
        return new ResponseEntity<>(source, HttpStatus.OK);
    }

    /**
     * 添加文档
     *
     * @param book
     * @return
     */
    @PostMapping("/bookAdd")
    public ResponseEntity<String> add(@RequestBody Book book) {
        try {
            // 构造ES的文档，这里注意startObject()开始构造，结束构造一定要加上endObject()
            XContentBuilder content = XContentFactory.jsonBuilder().startObject().
                    field("id", book.getId())
                    .field("bookName", book.getBookName())
                    .field("authorName", book.getAuthorName())
                    .field("bookPrice", book.getBookPrice())
                    .field("bookDescription", book.getBookDescription())
                    .endObject();
            IndexResponse result = transportClient.prepareIndex(EsConstant.ES_INDEX, EsConstant.ES_TYPE)
                    .setSource(content).get();
            return new ResponseEntity<>(result.getId(), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("add index is error", e, this.getClass());
        }
        return null;
    }

    /**
     * 根据id删除book
     *
     * @param id
     */
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @DeleteMapping("/bookDelete/{id}")
    public ResponseEntity<String> delete(@PathVariable(value = "id") String id) {
        DeleteResponse result = transportClient.prepareDelete(EsConstant.ES_INDEX, EsConstant.ES_TYPE, id).get();
        return new ResponseEntity<>(result.getResult().toString(), HttpStatus.OK);
    }

    /**
     * 更新文档，这里的Book可以不管他，这样做是为了解决PUT请求的问题，随便搞
     */
    @PutMapping("/bookUpdate")
    public ResponseEntity<String> update(@RequestBody Book book) {
        System.out.println(book);
        // 根据id查询
        UpdateRequest updateRequest = new UpdateRequest(EsConstant.ES_INDEX, EsConstant.ES_TYPE, book.getId());
        try {
            XContentBuilder contentBuilder = XContentFactory.jsonBuilder().startObject();
            if (StringUtils.isNotBlank(book.getBookName())) {
                contentBuilder.field("bookName", book.getBookName());
            }
            if (StringUtils.isNotBlank(book.getAuthorName())) {
                contentBuilder.field("authorName", book.getAuthorName());
            }
            if (book.getBookPrice() != null) {
                contentBuilder.field("bookPrice", book.getBookPrice());
            }
            if (StringUtils.isNotBlank(book.getBookDescription())) {
                contentBuilder.field("bookDescription", book.getBookDescription());
            }
            contentBuilder.endObject();
            updateRequest.doc(contentBuilder);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("search index is error", e, this.getClass());
        }
        // 进行更新
        UpdateResponse updateResponse = new UpdateResponse();
        try {
            updateResponse = transportClient.update(updateRequest).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            log.error("update index is error", e, this.getClass());
        }
        return new ResponseEntity<>(updateResponse.getResult().toString(), HttpStatus.OK);
    }
}
