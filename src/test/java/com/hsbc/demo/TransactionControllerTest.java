package com.hsbc.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsbc.demo.common.Page;
import com.hsbc.demo.controller.TransactionController;
import com.hsbc.demo.dto.ModifyTransactionDTO;
import com.hsbc.demo.dto.TransactionDTO;
import com.hsbc.demo.dto.TransactionRequestDTO;
import com.hsbc.demo.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testListTransactions() throws Exception {
        // 准备数据
        TransactionRequestDTO queryDTO = new TransactionRequestDTO();
        Page<TransactionDTO> page = new Page<>();
        page.setTotalCount(1);
        page.setData(Collections.singletonList(new TransactionDTO()));
        // 模拟 service 方法
        when(transactionService.listTransactions(any(TransactionRequestDTO.class))).thenReturn(page);
        // 执行请求并验证结果
        mockMvc.perform(post("/v1/transaction/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(queryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(1))
                .andExpect(jsonPath("$.data[0]").exists());
    }

    @Test
    public void testCreateTransaction() throws Exception {
        // 准备数据
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTid("1");
        transactionDTO.setAccountId("1");
        transactionDTO.setAmount(BigDecimal.valueOf(100d));
        transactionDTO.setType("type1");

        // 模拟 service 方法
        when(transactionService.createTransaction(any(TransactionDTO.class))).thenReturn(transactionDTO);

        // 执行请求并验证结果
        mockMvc.perform(post("/v1/transaction/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    public void testModifyTransaction() throws Exception {
        // 准备数据
        ModifyTransactionDTO modifyTransactionDTO = new ModifyTransactionDTO();
        modifyTransactionDTO.setTid("1");
        modifyTransactionDTO.setAccountId("1");
        modifyTransactionDTO.setAmount(BigDecimal.valueOf(10d));
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTid("1");

        // 模拟 service 方法
        when(transactionService.modifyTransaction(any(ModifyTransactionDTO.class))).thenReturn(transactionDTO);

        // 执行请求并验证结果
        mockMvc.perform(post("/v1/transaction/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyTransactionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    public void testDeleteTransaction() throws Exception {
        // 执行请求并验证结果
        mockMvc.perform(post("/v1/transaction/delete/1"))
                .andExpect(status().isNoContent());

        // 验证 service 方法是否被调用
        verify(transactionService, times(1)).deleteTransaction("1");
    }

    @Test
    public void testShowTransactionsPage() throws Exception {
        // 执行请求并验证结果
        mockMvc.perform(get("/v1/transaction/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().string("transactions"));
    }
}
