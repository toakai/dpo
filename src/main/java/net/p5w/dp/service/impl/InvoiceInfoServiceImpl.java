package net.p5w.dp.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.query.InvoiceInfoQuery;
import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.entity.InvoiceInfo;
import net.p5w.dp.mapper.InvoiceInfoMapper;
import net.p5w.dp.service.InvoiceInfoService;
import net.p5w.dp.vo.InvoiceInfoVO;

/**
 * 发票信息 Service 实现类
 */
@Slf4j
@Service
public class InvoiceInfoServiceImpl implements InvoiceInfoService {

    /** 文件上传根目录（从配置文件读取，各环境按需配置） */
    @Value("${file.upload-path}")
    private String uploadPath;

    @Resource
    private InvoiceInfoMapper invoiceInfoMapper;

    /**
     * 分页查询发票列表（返回脱敏 VO）
     */
    @Override
    public PageResult<InvoiceInfoVO> page(InvoiceInfoQuery query) {
        log.info("分页查询发票：pageNum={}, pageSize={}", query.getPageNum(), query.getPageSize());

        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<InvoiceInfo> invoiceList = invoiceInfoMapper.selectList(query);
        PageInfo<InvoiceInfo> pageInfo = new PageInfo<>(invoiceList);

        List<InvoiceInfoVO> voList = invoiceList.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.build(pageInfo.getTotal(), pageInfo.getPageSize(), pageInfo.getPageNum(), voList);
    }

    /**
     * 根据主键查询发票详情
     */
    @Override
    public InvoiceInfoVO getById(Integer id) {
        InvoiceInfo invoice = invoiceInfoMapper.selectById(id);
        if (invoice == null) {
            log.warn("发票不存在，id={}", id);
            return null;
        }
        return convertToVO(invoice);
    }

    /**
     * 上传发票文件到本地磁盘
     * <p>
     * 存储路径格式：{upload-path}/invoice/{yyyy}/{MM}/{dd}/{uuid}.{ext}
     * </p>
     */
    @Override
    public String uploadFile(String originalFilename, byte[] fileBytes) {
        Assert.hasText(originalFilename, "文件名不能为空");
        Assert.notNull(fileBytes, "文件内容不能为空");
        Assert.state(fileBytes.length > 0, "文件内容不能为空");

        // 1. 提取文件扩展名（小写，防路径穿越）
        String originalName = new File(originalFilename).getName();
        String ext = "";
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < originalName.length() - 1) {
            ext = originalName.substring(dotIndex + 1).toLowerCase();
        }

        // 2. 构建日期目录
        String today = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String subDir = uploadPath + "/invoice/" + today;

        // 3. 创建目录
        File dir = new File(subDir);
        if (!dir.exists() && !dir.mkdirs()) {
            log.error("创建文件上传目录失败，path={}", subDir);
            throw new RuntimeException("创建文件上传目录失败");
        }

        // 4. 生成唯一文件名
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String savedFilename = uuid + (ext.isEmpty() ? "" : "." + ext);
        File targetFile = new File(subDir, savedFilename);

        // 5. 写入磁盘
        try (FileOutputStream fos = new FileOutputStream(targetFile)) {
            fos.write(fileBytes);
            fos.flush();
        } catch (IOException e) {
            log.error("文件写入磁盘失败，targetFile={}", targetFile.getAbsolutePath(), e);
            throw new RuntimeException("文件上传失败：" + e.getMessage());
        }

        String absolutePath = targetFile.getAbsolutePath();
        log.info("文件上传成功，originalFilename={}, savedPath={}", originalFilename, absolutePath);
        return absolutePath;
    }

    /**
     * 根据发票ID获取文件绝对路径
     */
    @Override
    public String getFilePath(Integer id) {
        InvoiceInfo invoice = invoiceInfoMapper.selectById(id);
        if (invoice == null) {
            return null;
        }
        return invoice.getFilerealpath();
    }

    /**
     * InvoiceInfo 实体转 InvoiceInfoVO（统一转换入口）
     */
    private InvoiceInfoVO convertToVO(InvoiceInfo invoice) {
        InvoiceInfoVO vo = new InvoiceInfoVO();
        BeanUtils.copyProperties(invoice, vo);
        return vo;
    }
}
