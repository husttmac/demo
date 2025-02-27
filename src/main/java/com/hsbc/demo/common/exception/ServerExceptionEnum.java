package com.hsbc.demo.common.exception;

import lombok.Getter;


@Getter
public enum ServerExceptionEnum {
    DUPLICATE_ERROR("1", "transaction id duplicate"),
    NOT_FOUND_ERROR("2", "transaction id not found"),
    ACCOUNT_INVALID("3", "account id invalid"),
    ;
    private final String code;
    private final String msg;

    ServerExceptionEnum(String code, String msg) {
        this.msg = msg;
        this.code = code;
    }
}
