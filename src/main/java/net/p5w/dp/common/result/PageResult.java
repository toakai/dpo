package net.p5w.dp.common.result;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用分页响应体
 * <p>
 * 出参字段 pageNum / pageSize 与入参 {@link net.p5w.dp.common.query.PageQuery} 的
 * {@code page}/{@code size} 对应（入参用短名，出参用全名以保持语义清晰）。
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    /** 总记录数（使用 Long，防止大数据量溢出） */
    private Long total;

    /** 每页条数 */
    private Integer pageSize;

    /** 当前页码（从 1 开始） */
    private Integer pageNum;

    /** 总页数 */
    private Integer totalPage;

    /** 当前页数据列表 */
    private List<T> list;

    /**
     * 构建分页结果
     *
     * @param total    总记录数
     * @param pageSize 每页条数
     * @param pageNum  当前页码
     * @param list     当前页数据
     * @return 分页结果对象
     * @throws ArithmeticException 若总页数超出 int 范围（极端情况）
     */
    public static <T> PageResult<T> build(Long total, Integer pageSize, Integer pageNum, List<T> list) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setTotal(total);
        pageResult.setPageSize(pageSize);
        pageResult.setPageNum(pageNum);
        pageResult.setList(list);

        // 向上取整计算总页数；Math.toIntExact 在超出 int 范围时抛出异常，而非静默截断
        long totalPageLong = (total + pageSize - 1) / pageSize;
        pageResult.setTotalPage(Math.toIntExact(totalPageLong));

        return pageResult;
    }
}
