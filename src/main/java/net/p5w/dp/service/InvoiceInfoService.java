package net.p5w.dp.service;

import net.p5w.dp.common.query.InvoiceInfoQuery;
import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.entity.InvoiceInfo;
import net.p5w.dp.vo.InvoiceInfoVO;

/**
 * 发票信息业务接口
 */
public interface InvoiceInfoService {

    /**
     * 分页查询发票列表
     *
     * @param query 分页及筛选条件
     * @return 分页VO结果
     */
    PageResult<InvoiceInfoVO> page(InvoiceInfoQuery query);

    /**
     * 根据主键查询发票详情
     *
     * @param id 发票主键
     * @return 发票VO详情
     */
    InvoiceInfoVO getById(Integer id);

    /**
     * 上传发票文件
     * <p>
     * 文件存储到本地磁盘，返回文件在服务器上的绝对路径。
     * 该路径需由调用方写入数据库 filerealpath 字段，以关联到发票记录。
     * </p>
     *
     * @param originalFilename 原始文件名（用于提取扩展名）
     * @param fileBytes        文件字节数组
     * @return 文件绝对路径，供后续写入数据库
     */
    String uploadFile(String originalFilename, byte[] fileBytes);

    /**
     * 根据发票ID获取文件路径
     *
     * @param id 发票主键
     * @return 文件绝对路径，不存在时返回 null
     */
    String getFilePath(Integer id);
}
