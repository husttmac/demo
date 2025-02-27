package com.hsbc.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hsbc.demo.dto.ModifyTransactionDTO;
import com.hsbc.demo.common.Page;
import com.hsbc.demo.dto.TransactionDTO;
import com.hsbc.demo.dto.TransactionRequestDTO;
import com.hsbc.demo.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/transaction")
public class TransactionController {
    @Autowired
    TransactionService service;

    @Operation(summary = "list record", description = "return records")
    @PostMapping("/list")
    public ResponseEntity<Page<TransactionDTO>> list(@RequestBody @Valid TransactionRequestDTO transactionQuery) {
        Page<TransactionDTO> transactionDtos = service.listTransactions(transactionQuery);
        return new ResponseEntity<>(transactionDtos, HttpStatus.OK);
    }

    @PostMapping("/create")
    @Operation(summary = "create a record", description = "return id")
    public ResponseEntity<TransactionDTO> create(@RequestBody @NotNull @Valid TransactionDTO transactionDto) throws JsonProcessingException {
        TransactionDTO dto = service.createTransaction(transactionDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "modify record", description = "modify")
    @PostMapping("/modify")
    public ResponseEntity<TransactionDTO> modify(@RequestBody @NotNull @Valid ModifyTransactionDTO transactionDto) throws JsonProcessingException {
        return new ResponseEntity<>(service.modifyTransaction(transactionDto),HttpStatus.OK);
    }

    @Operation(summary = "delete record", description = "delete")
    @PostMapping("delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
