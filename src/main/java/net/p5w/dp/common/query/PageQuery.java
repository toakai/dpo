package net.p5w.dp.common.query;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class PageQuery {

    // 定义常量
    public static final int MAX_PAGE_SIZE = 100;

    // 和前端传参完全一致！
    private Integer page = 1;
    private Integer size = 10;

    public void setPage(Integer page) {
        log.debug("[调试] setPage: {}", page);
        if (page != null && page >= 1) {
            this.page = page;
        }
    }

    public void setSize(Integer size) {
        log.debug("[调试] setSize: {}", size);

        if (size == null) {
            this.size = 10;
            return;
        }

        if (size > MAX_PAGE_SIZE) {
            log.warn("每页size超过最大限制" + MAX_PAGE_SIZE + "，抛出异常！");
            throw new IllegalArgumentException("每页最大支持" + MAX_PAGE_SIZE + "条数据");
        }

        if (size < 1) {
            this.size = 10;
            return;
        }

        this.size = size;
    }
}