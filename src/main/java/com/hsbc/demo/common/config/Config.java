package com.hsbc.demo.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hsbc.demo.dto.TransactionDTO;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Configuration
public class Config {

    public static final String TRANSACTION_CACHE = "transactionsCache";

    @Bean
    public CaffeineCacheManager cacheManager(Caffeine<Object,Object> caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        cacheManager.setCacheNames(Collections.singletonList(TRANSACTION_CACHE));
        return cacheManager;
    }

    @Bean
    public Caffeine<Object, Object> caffeineCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .initialCapacity(100)
                .maximumSize(1000);
    }

    @Bean
    public Map<String, TransactionDTO> dbMap() {
        return new ConcurrentHashMap<>();
    }
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
