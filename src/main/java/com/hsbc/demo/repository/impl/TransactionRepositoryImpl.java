package com.hsbc.demo.repository.impl;

import com.hsbc.demo.common.Page;
import com.hsbc.demo.dto.TransactionRequestDTO;
import com.hsbc.demo.common.config.Config;
import com.hsbc.demo.dto.TransactionDTO;
import com.hsbc.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    @Autowired
    Map<String, TransactionDTO> dbMap;

    @Autowired
    CaffeineCacheManager cacheManager;

    @Override
    public TransactionDTO save(TransactionDTO transaction) {
        dbMap.put(transaction.getTid(), transaction);
        return transaction;
    }

    @Override
    public TransactionDTO findByTid(String id) {
        TransactionDTO dto= cacheManager.getCache(Config.TRANSACTION_CACHE).get(id,TransactionDTO.class);
        if(dto == null){
            dto= dbMap.get(id);
        }
        return dto;
    }

    @Override
    public void deleteByTid(String id) {
        dbMap.remove(id);
    }

    @Override
    public TransactionDTO update(TransactionDTO dto) {
        Objects.requireNonNull(cacheManager.getCache(Config.TRANSACTION_CACHE)).evictIfPresent("id");
        dbMap.put(dto.getTid(), dto);
        Objects.requireNonNull(cacheManager.getCache(Config.TRANSACTION_CACHE)).putIfAbsent("id",dto);
        return dto;
    }

    @Override
    public Page<TransactionDTO> queryList(TransactionRequestDTO query) {
        List<TransactionDTO> list = new ArrayList<>(dbMap.values());
        // filter
        List<TransactionDTO> filteredTransactions = list.stream()
                .filter(dto -> {
                    if (query.getTid() != null && !query.getTid().isEmpty() && !query.getTid().equals(dto.getTid())) {
                        return false;
                    }
                    if (query.getType() != null && !query.getType().isEmpty() && !query.getType().equals(dto.getType())) {
                        return false;
                    }
                    if (query.getAmount() != null && !query.getAmount().equals(dto.getAmount())) {
                        return false;
                    }
                    if (query.getAccountId() != null && !query.getAccountId().isEmpty() && !query.getAccountId().equals(dto.getAccountId())) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
        filteredTransactions.sort(Comparator.comparing(TransactionDTO::getTid));
        filteredTransactions.sort(Comparator.comparing(TransactionDTO::getTid));
        int totalCount = filteredTransactions.size();
        int totalPages = (int) Math.ceil((double) filteredTransactions.size() / query.getPageSize());
        int pageNumber = query.getPageNumber() != 1 ? query.getPageNumber() : 1;
        int pageSize = query.getPageSize() != 10 ? query.getPageSize() : 10;
        int fromIndex = (pageNumber - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, filteredTransactions.size());
        List<TransactionDTO> data = filteredTransactions.subList(fromIndex, toIndex);
        return new Page<>(totalCount, totalPages,pageNumber,pageSize,data);
    }
}