package net.p5w.dp.mapper;

import net.p5w.dp.common.query.InvoiceInfoQuery;
import net.p5w.dp.entity.InvoiceInfo;

import java.util.List;

/**
 * 发票信息数据访问层
 */
public interface InvoiceInfoMapper {

    /**
     * 按条件分页查询发票列表（配合 PageHelper 使用）
     *
     * @param query 分页及筛选条件
     * @return 发票实体集合
     */
    List<InvoiceInfo> selectList(InvoiceInfoQuery query);

    /**
     * 根据主键查询发票详情
     *
     * @param id 发票主键
     * @return 发票实体，不存在则返回 null
     */
    InvoiceInfo selectById(Integer id);
}
