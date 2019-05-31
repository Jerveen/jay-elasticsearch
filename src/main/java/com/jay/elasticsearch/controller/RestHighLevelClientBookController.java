package com.jay.elasticsearch.controller;

import com.jay.elasticsearch.constant.EsConstant;
import com.jay.elasticsearch.model.Book;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 创建索引
     *
     * @param index
     * @param type
     * @return
     */
    @GetMapping("/addIndex")
    public boolean addIndex(String index, String type) {
        try {
            IndexRequest indexRequest = new IndexRequest(index, type);
            // 设置index的mapping
            buildIndexMapping(indexRequest);
            // 同步执行
            // restHighLevelClient.index(indexRequest);
            // 异步执行
            restHighLevelClient.indexAsync(indexRequest, new ActionListener<IndexResponse>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {
                    log.info("create index success, index info is :" + indexResponse.toString());
                }

                @Override
                public void onFailure(Exception e) {
                    log.error("create index is error", e, this.getClass());
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("create index is error", e, this.getClass());
            return false;
        }
    }

    @GetMapping("/existsIndex")
    public boolean indexExists(String indexName, String type, String id) {
        GetRequest getIndexRequest = new GetRequest(indexName, type, id);
        getIndexRequest.index(indexName);
        try {
            // 同步执行
            boolean exists = restHighLevelClient.exists(getIndexRequest);
            // 异步执行
            /*restHighLevelClient.existsAsync(getIndexRequest, new ActionListener<Boolean>() {
                @Override
                public void onResponse(Boolean aBoolean) {
                    log.info("create index success, index info is :" + aBoolean);
                }

                @Override
                public void onFailure(Exception e) {
                    log.error("create index is error", e, this.getClass());
                }
            });*/
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("exists index is error", e, this.getClass());
            return false;
        }
    }

    @DeleteMapping("/deleteIndex")
    public String deleteIndex(String indexName, String type, String id) {
        DeleteRequest deleteRequest = new DeleteRequest(indexName, type, id);
        try {
            // 同步和异步亦可
            DeleteResponse delete = restHighLevelClient.delete(deleteRequest);
            return delete.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("delete index is error", e, this.getClass());
            return "delete index is error" + e.toString();
        }
    }

    @PutMapping("/updateIndex")
    public void updateIndex(String indexName, String type, String id) {
        try {
            UpdateRequest updateRequest = new UpdateRequest(indexName, type, id)
                    .doc("updated", new Date(), "reason", "daily update");
            // 同步和异步亦可
            restHighLevelClient.update(updateRequest);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("delete index is error", e, this.getClass());
        }
    }

    @PostMapping("/addBookDoc")
    public ResponseEntity<String> addBookDoc(@RequestBody Book book) {
        try {
            // 构造ES的文档，这里注意startObject()开始构造，结束构造一定要加上endObject()
            XContentBuilder content = XContentFactory.jsonBuilder().startObject().
                    field("id", book.getId())
                    .field("bookName", book.getBookName())
                    .field("authorName", book.getAuthorName())
                    .field("bookPrice", book.getBookPrice())
                    .field("bookDescription", book.getBookDescription())
                    .endObject();
            IndexRequest indexRequest = new IndexRequest("books", "books").id(book.getId()).source(content);
            IndexResponse result = restHighLevelClient.index(indexRequest);
            return new ResponseEntity<>(result.getId(), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("add book doc is error", e, this.getClass());
        }
        return null;
    }

    /**
     * 根据id删除book
     *
     * @param id
     */
    @DeleteMapping("/deleteBookDoc/{id}")
    public ResponseEntity<String> deleteBookDoc(@PathVariable(value = "id") String id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest("books", "books", id);
            // 同步&异步
            DeleteResponse delete = restHighLevelClient.delete(deleteRequest);
            return new ResponseEntity<>(delete.getResult().toString(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("delete doc is error", e, this.getClass());
            return new ResponseEntity<>("delete doc is error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 更新文档
     */
    @PutMapping("/updateBookDoc")
    public ResponseEntity<String> updateBookDoc(@RequestBody Book book) {
        System.out.println(book);
        // 根据id查询
        UpdateRequest updateRequest = new UpdateRequest("books", "books", book.getId());
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
            log.error("search doc is error", e, this.getClass());
        }
        // 进行更新
        UpdateResponse updateResponse = new UpdateResponse();
        try {
            updateResponse = restHighLevelClient.update(updateRequest);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("update doc is error", e, this.getClass());
        }
        return new ResponseEntity<>(updateResponse.getResult().toString(), HttpStatus.OK);
    }

    /**
     * 设置index的mapping
     *
     * @param request
     */
    private void buildIndexMapping(IndexRequest request) {
        Map<String, Object> jsonMap = new HashMap<>();
        Map<String, Object> id = new HashMap<>();
        id.put("type", "text");
        Map<String, Object> bookName = new HashMap<>();
        bookName.put("type", "text");
        Map<String, Object> authorName = new HashMap<>();
        authorName.put("type", "text");
        Map<String, Object> bookPrice = new HashMap<>();
        bookPrice.put("type", "double");
        Map<String, Object> bookDescription = new HashMap<>();
        bookDescription.put("type", "text");
        Map<String, Object> properties = new HashMap<>();
        properties.put("id", id);
        properties.put("bookName", bookName);
        properties.put("authorName", authorName);
        properties.put("bookPrice", bookPrice);
        properties.put("bookDescription", bookDescription);
        Map<String, Object> book = new HashMap<>();
        book.put("properties", properties);
        jsonMap.put("books", book);
        request.source(jsonMap);
    }
}