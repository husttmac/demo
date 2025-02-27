package com.hsbc.demo.repository;

import com.hsbc.demo.common.Page;
import com.hsbc.demo.dto.TransactionRequestDTO;
import com.hsbc.demo.dto.TransactionDTO;

public interface TransactionRepository {

    TransactionDTO save(TransactionDTO transaction);

    TransactionDTO findByTid(String tid);

    void deleteByTid(String tid);

    TransactionDTO update(TransactionDTO dto);

    Page<TransactionDTO> queryList(TransactionRequestDTO query);
}
