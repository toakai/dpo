package net.p5w.dp.common.query;

import java.util.Date;

import lombok.Data;

/**
 * 发票信息分页查询条件
 * <p>
 * 继承 {@link PageQuery} 获得分页能力（page / size），
 * 在此基础上扩展发票业务筛选字段。
 * </p>
 */
@Data
public class InvoiceInfoQuery extends PageQuery {

    /** 申请单号（模糊匹配） */
    private String requestCode;

    /** 收款人/报销人（模糊匹配） */
    private String skr;

    /** 发票类型（精确匹配） */
    private String fplx;

    /** 发票日期起始（含边界） */
    private Date fprqStart;

    /** 发票日期截止（含边界） */
    private Date fprqEnd;
}
