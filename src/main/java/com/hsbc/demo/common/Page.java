package com.hsbc.demo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page<T> {
    private int totalCount;
    private int totalPage;
    private int pageNumber;
    private int pageSize;
    List<T> data;
}
