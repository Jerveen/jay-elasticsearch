package com.jay.elasticsearch.config;

import lombok.extern.log4j.Log4j2;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

/**
 * @Description: TransportClient方式声明类
 * @Author: xyw
 * @CreateDt: 2019-05-30
 */
@Configuration
@Log4j2
public class TransportClientConfig {

    @Bean
    public TransportClient getTransportClient() {
        log.info("transportClient init is begin");
        TransportClient transportClient = null;
        try {

            // 指定集群名，默认的是elasticsearch，如果在配置文件中修改则一定要添加
            Settings settings = Settings.builder()
                    // 指定集群名
                    .put("cluster.name", "jay-es")
                    // 增加嗅探机制，找到ES集群
                    .put("client.transport.sniff", true)
                    .build();

            // 配置自定义的settings信息
            transportClient = new PreBuiltTransportClient(settings);

            // ES的TCP端口为9300,而不是之前练习的HTTP端口9200, 可以配置了一个节点的地址然添加进去,也可以配置多个从节点添加进去再返回
            InetSocketTransportAddress nodeMaster = new InetSocketTransportAddress(InetAddress.getByName("118.25.153.239"), 9300);
            InetSocketTransportAddress nodeOne = new InetSocketTransportAddress(InetAddress.getByName("118.25.153.239"), 9301);
            // 设置连接节点信息
            transportClient.addTransportAddress(nodeMaster);
            transportClient.addTransportAddress(nodeOne);

            // 返回连接对象
            return transportClient;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("transportClient init is error", this.getClass(), e);
        }
        return transportClient;
    }
}
