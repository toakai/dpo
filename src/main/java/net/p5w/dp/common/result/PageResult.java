package net.p5w.dp.common.result;

import java.util.List;

import lombok.Data;

@Data
public class PageResult<T> {
    private long total;       // 总记录数
    private long size;        // 每页条数
    private long current;     // 当前页
    private long pages;       // 总页数
    private List<T> records;  // 数据列表

    // 快速构建分页结果
    public static <T> PageResult<T> build(long total, long size, long current, List<T> records) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setTotal(total);
        pageResult.setSize(size);
        pageResult.setCurrent(current);
        pageResult.setPages((total + size - 1) / size);
        pageResult.setRecords(records);
        return pageResult;
    }
}