package com.hsbc.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@lombok.Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyTransactionDTO {
    @NotEmpty(message = "id cannot be empty")
    private String tid;
    @NotEmpty(message = "accountId cannot be empty")
    private String accountId;
    @DecimalMin("0")
    private BigDecimal amount;

}
