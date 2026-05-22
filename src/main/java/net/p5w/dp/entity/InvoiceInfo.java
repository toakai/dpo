package net.p5w.dp.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 发票报销信息表
 *
 * @author p5w
 */
@Data
public class InvoiceInfo {

    /** 主键 */
    private Integer id;

    /** requestid */
    private Integer requestId;

    /** 申请单号 */
    private String requestCode;

    /** 收款人/报销人 */
    private String skr;

    /** 报销说明 */
    private String bxsm;

    /** 发票代码/编号数字部分 */
    private Integer fp;

    /** 发票日期 */
    private Date fprq;

    /** 金额(含税) */
    private BigDecimal jey;

    /** 财务确认金额 */
    private BigDecimal cwqrjey;

    /** 不含税金额 */
    private BigDecimal bhsjey;

    /** 进项税额 */
    private BigDecimal jxsey;

    /** 发票类型 */
    private String fplx;

    /** 支付方式/消费类型 */
    private String zfsx;

    /** 图片ID */
    private Integer imageId;

    /** 销方名称 */
    private String seller;

    /** 服务类型 */
    private String invoiceServiceType;

    /** 购方名称 */
    private String purchaser;

    /** 不含税价 */
    private BigDecimal priceWithoutTax;

    /** 税率 */
    private Float taxRate;

    /** 税额 */
    private BigDecimal tax;

    /** 含税总价 */
    private BigDecimal taxIncludedPrice;

    /** 文件名 */
    private String imagefilename;

    /** 文件MIME类型 */
    private String imagefiletype;

    /** 文件服务器路径 */
    private String filerealpath;

    /** 文件大小(字节) */
    private String filesize;
}
