package com.hsbc.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * domain model
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDTO {

    @Schema(description = "transaction id", example = "1")
    private String tid;

    @NotBlank(message = "accountId cannot be blank")
    @Schema(description = "accout id", example = "1")
    private String accountId;

    @DecimalMin("0")
    @Schema(description = "transaction amount", example = "1")
    private BigDecimal amount;

    @NotBlank(message = "type cannot be blank")
    @Pattern(regexp = "type1|type2|type3",message = "type must be in type1,type2,type3")
    private String type;

}
