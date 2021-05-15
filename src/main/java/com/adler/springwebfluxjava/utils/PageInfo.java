package com.adler.springwebfluxjava.utils;

import reactor.core.publisher.Flux;

import java.util.List;

public class PageInfo {

    private long current;
    private long size;
    private long total;
    private Flux data;

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Flux getData() {
        return data;
    }

    public void setData(Flux data) {
        this.data = data;
    }
}
