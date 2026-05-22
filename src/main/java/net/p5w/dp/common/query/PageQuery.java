package net.p5w.dp.common.query;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 通用分页查询基类
 * <p>
 * 所有需要分页的 Query 对象均继承此类。
 * 前端入参字段名为 {@code page}（页码）和 {@code size}（每页条数），
 * 内部通过 {@link #getPageNum()} / {@link #getPageSize()} 别名访问。
 * setter 中内置参数合法性校验，非法值自动使用默认值或抛出异常。
 * </p>
 *
 * @see net.p5w.dp.common.result.PageResult
 */
@Slf4j
public class PageQuery {

    /** 单页最大条数限制，防止恶意大分页拖垮数据库 */
    public static final int MAX_PAGE_SIZE = 100;

    /** 当前页码，从 1 开始，默认第 1 页（前端入参名：page） */
    private Integer page = 1;

    /** 每页条数，默认 10 条（前端入参名：size） */
    private Integer size = 10;

    /**
     * 获取当前页码（别名，语义更清晰）
     */
    public Integer getPageNum() {
        return page;
    }

    /**
     * 获取每页条数（别名，语义更清晰）
     */
    public Integer getPageSize() {
        return size;
    }

    public Integer getPage() {
        return page;
    }

    /**
     * 设置当前页码
     * <p>非法值（null 或小于 1）保持默认值 1，不抛出异常（容忍前端漏传）</p>
     */
    public void setPage(Integer page) {
        log.debug("[分页] setPage: {}", page);
        if (page != null && page >= 1) {
            this.page = page;
        }
    }

    public Integer getSize() {
        return size;
    }

    /**
     * 设置每页条数
     * <p>超过 {@link #MAX_PAGE_SIZE} 时抛出异常；小于 1 时回退为默认值 10</p>
     *
     * @throws IllegalArgumentException 当 size 超过最大限制时
     */
    public void setSize(Integer size) {
        log.debug("[分页] setSize: {}", size);

        if (size == null) {
            this.size = 10;
            return;
        }

        if (size > MAX_PAGE_SIZE) {
            log.warn("size={} 超过最大限制 {}，拒绝请求", size, MAX_PAGE_SIZE);
            throw new IllegalArgumentException("每页最大支持 " + MAX_PAGE_SIZE + " 条数据");
        }

        if (size < 1) {
            this.size = 10;
            return;
        }

        this.size = size;
    }
}
