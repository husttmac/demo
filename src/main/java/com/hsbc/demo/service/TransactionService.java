package com.hsbc.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hsbc.demo.dto.TransactionRequestDTO;
import com.hsbc.demo.common.Page;
import com.hsbc.demo.common.exception.ServerException;
import com.hsbc.demo.dto.ModifyTransactionDTO;
import com.hsbc.demo.dto.TransactionDTO;

public interface TransactionService {

    TransactionDTO createTransaction(TransactionDTO transactionDto) throws ServerException, JsonProcessingException;

    void deleteTransaction(String id);

    TransactionDTO modifyTransaction(ModifyTransactionDTO transactionDto) throws JsonProcessingException;

    Page<TransactionDTO> listTransactions(TransactionRequestDTO query);
}