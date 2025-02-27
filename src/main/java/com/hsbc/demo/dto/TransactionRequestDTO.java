package com.hsbc.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hsbc.demo.common.PageRequest;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequestDTO extends PageRequest {
    private String tid;
    private String accountId;
    @DecimalMin("0")
    private BigDecimal amount;
    @Pattern(regexp = "type1|type2|type3",message = "type must be in type1,type2,type3")
    private String type;

}
