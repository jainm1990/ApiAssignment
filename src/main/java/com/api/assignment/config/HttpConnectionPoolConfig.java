package com.api.assignment.config;

import com.api.assignment.exception.RestResponseErrorHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpConnectionPoolConfig {

    @Autowired
    private HttpHostConfiguration httpHostConfiguration;

    @Value("${api.connectionTimeOut}")
    Integer CONNECTION_TIMEOUT;
    @Value("${api.readTimeOut}")
    Integer READ_TIMEOUT;

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager result = new PoolingHttpClientConnectionManager();
        result.setMaxTotal(this.httpHostConfiguration.getMaxTotal());
        // Default max per route is used in case it's not set for a specific route
        result.setDefaultMaxPerRoute(this.httpHostConfiguration.getDefaultMaxPerRoute());
        return result;
    }

    @Bean
    public RequestConfig requestConfig() {
        RequestConfig result = RequestConfig.custom()
                .setConnectionRequestTimeout(READ_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .build();
        return result;
    }

    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager, RequestConfig requestConfig) {

        CloseableHttpClient result = null;
        return HttpClientBuilder
                .create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig).build();

    }

    @Bean
    public RestTemplate restTemplate(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RestResponseErrorHandler());
        return restTemplate;
    }
}
