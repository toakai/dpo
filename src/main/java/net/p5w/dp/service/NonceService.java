package net.p5w.dp.service;

import java.util.Date;

import net.p5w.dp.entity.Nonce;

public interface NonceService {

    int deleteByPrimaryKey(Long id);

    int insert(Nonce record);

    Nonce selectByPrimaryKey(Long id);

    /**
     * 批量删除创建时间早于指定时间的 nonce 记录
     *
     * @param threshold 时间阈值
     * @return 删除的记录数
     */
    int deleteByCreateTimeBefore(Date threshold);
}
