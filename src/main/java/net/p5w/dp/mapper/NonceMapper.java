package net.p5w.dp.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import net.p5w.dp.entity.Nonce;

public interface NonceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Nonce record);

    Nonce selectByPrimaryKey(Long id);

    /**
     * 批量删除创建时间早于指定时间的 nonce 记录
     *
     * @param threshold 时间阈值，删除 create_time &lt; threshold 的记录
     * @return 删除的记录数
     */
    int deleteByCreateTimeBefore(@Param("threshold") Date threshold);
}