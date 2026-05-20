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
     * 总记录数 —— 使用Long类型，防止大数据量溢出
     */
    private Long total;

    /**
     * 每页条数 —— 改为Integer，与项目分页参数类型统一
     */
    private Integer pageSize;

    /**
     * 当前页码 —— 改为Integer，与项目分页参数类型统一
     */
    private Integer pageNum;

    /**
     * 总页数 —— 改为Integer，分页场景不会超出int范围
     */
    private Integer totalPage;

    /**
     * 分页数据集合
     */
    private List<T> list;

    /**
     * 构建分页结果
     *
     * @param total    总记录数
     * @param pageSize 每页条数
     * @param pageNum  当前页码
     * @param list     数据列表
     * @return 分页结果对象
     */
    public static <T> PageResult<T> build(Long total, Integer pageSize, Integer pageNum, List<T> list) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setTotal(total);
        pageResult.setPageSize(pageSize);
        pageResult.setPageNum(pageNum);
        pageResult.setList(list);

        // 向上取整计算总页数
        long totalPageLong = (total + pageSize - 1) / pageSize;
        // 【安全转换】如果超出int范围会抛出异常，而非静默错误
        Integer totalPage = Math.toIntExact(totalPageLong);
        pageResult.setTotalPage(totalPage);

        return pageResult;
    }
}