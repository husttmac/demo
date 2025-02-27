package com.hsbc.demo.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hsbc.demo.common.Page;
import com.hsbc.demo.dto.TransactionRequestDTO;
import com.hsbc.demo.common.config.Config;
import com.hsbc.demo.common.exception.ServerException;
import com.hsbc.demo.common.exception.ServerExceptionEnum;
import com.hsbc.demo.common.utils.Lock;
import com.hsbc.demo.dto.ModifyTransactionDTO;
import com.hsbc.demo.dto.TransactionDTO;
import com.hsbc.demo.repository.TransactionRepository;
import com.hsbc.demo.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private static final Lock lock = new Lock();

    @Autowired
    Map<String, TransactionDTO> dbMap;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CaffeineCacheManager cacheManager;

    @Autowired
    Caffeine<Object,Object> caffeine;

    @Override
    public TransactionDTO createTransaction(TransactionDTO transaction) throws JsonProcessingException {
        log.info("createTransaction  {} ",objectMapper.writeValueAsString(transaction));
        //check tid
        if (transactionRepository.findByTid(transaction.getTid()) != null) {
            throw new ServerException(ServerExceptionEnum.DUPLICATE_ERROR);
        }
        if(!StringUtils.hasLength(transaction.getTid())){
            String id = UUID.randomUUID().toString();
            transaction.setTid(id);
        }
        try {
            //lock
            lock.tryLock(transaction.getAccountId(),10L,TimeUnit.SECONDS);
            transactionRepository.save(transaction);
            // update cache
            Objects.requireNonNull(cacheManager.getCache(Config.TRANSACTION_CACHE)).put("id", transaction);
            return transaction;
        } catch (InterruptedException | TimeoutException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            // release lock
            lock.unlock(transaction.getAccountId());
        }
    }

    @Override
    public void deleteTransaction(String tid) {
        log.info("createTransaction tid: {} ", tid);
        //check
        TransactionDTO dto= transactionRepository.findByTid(tid);
        if(dto == null){
            throw new ServerException(ServerExceptionEnum.NOT_FOUND_ERROR);
        }
        // delete cache
        cacheManager.getCache(Config.TRANSACTION_CACHE).evictIfPresent("id");
        try {
            lock.tryLock(dto.getAccountId(),10L,TimeUnit.SECONDS);
            transactionRepository.deleteByTid(tid);
        } catch (InterruptedException | TimeoutException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        finally {
            lock.unlock(dto.getAccountId());
        }
    }

    @Override
    public TransactionDTO modifyTransaction(ModifyTransactionDTO transaction) throws JsonProcessingException {
        log.info("modifyTransaction  {} ",objectMapper.writeValueAsString(transaction));
        TransactionDTO dto= transactionRepository.findByTid(transaction.getTid());
        if(dto == null){
            throw new ServerException(ServerExceptionEnum.NOT_FOUND_ERROR);
        }
        if (!Objects.equals(transaction.getAccountId(),dto.getAccountId())){
            throw new ServerException(ServerExceptionEnum.ACCOUNT_INVALID);
        }
        try {
            lock.tryLock(transaction.getAccountId(),10L,TimeUnit.SECONDS);
            dto.setAmount(transaction.getAmount());
            //update cache
            transactionRepository.update(dto);
            return dto;
        }catch (InterruptedException | TimeoutException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }finally {
            lock.unlock(dto.getAccountId());
        }
    }

    @Override
    public Page<TransactionDTO> listTransactions(TransactionRequestDTO query) {
        return transactionRepository.queryList(query);
    }
}