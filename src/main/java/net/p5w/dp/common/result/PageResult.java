package net.p5w.dp.common.result;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用分页返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 每页条数
     */
    private Long pageSize;

    /**
     * 当前页码
     */
    private Long pageNum;

    /**
     * 总页数
     */
    private Long totalPage;

    /**
     * 分页数据集合
     */
    private List<T> list;

    /**
     * 构建分页结果
     */
    public static <T> PageResult<T> build(Long total, Long pageSize, Long pageNum, List<T> list) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setTotal(total);
        pageResult.setPageSize(pageSize);
        pageResult.setPageNum(pageNum);
        pageResult.setList(list);
        // 向上取整计算总页数
        long totalPage = (total + pageSize - 1) / pageSize;
        pageResult.setTotalPage(totalPage);
        return pageResult;
    }
}