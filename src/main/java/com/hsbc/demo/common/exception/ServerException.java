package com.hsbc.demo.common.exception;

import lombok.Data;


@Data
public class ServerException extends RuntimeException{

    private ServerExceptionEnum error;

    public ServerException(ServerExceptionEnum error) {
        this.error = error;
    }
}
