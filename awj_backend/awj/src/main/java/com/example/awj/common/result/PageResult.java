package com.example.awj.common.result;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private long total;
    private int pages;
    private int current;
    private int size;
    private List<T> records;

    public PageResult() {}

    public PageResult(long total, long current, long size, List<T> records) {
        this.total = total;
        this.current = (int) current;
        this.size = (int) size;
        this.records = records;
        this.pages = (int) Math.ceil((double) total / size);
    }
}
