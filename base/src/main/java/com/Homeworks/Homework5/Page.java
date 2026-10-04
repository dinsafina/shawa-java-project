package com.Homeworks.Homework5;

import java.util.List;

public class Page <T>{

    private final List<T> items;
    private final int page;
    private final int pageSize;
    private final long totalCount;

    public Page(List<T> items, int page, int pageSize, long totalCount) {
        this.items = items;
        this.page = page;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
    }

    public List<T> getItems() {
        return items;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    @Override
    public String toString() {
        return "Page{" +
                "items=" + items +
                ", page=" + page +
                ", pageSize=" + pageSize +
                ", totalCount=" + totalCount +
                '}';
    }
}
