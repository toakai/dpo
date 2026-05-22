package net.p5w.dp.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.exception.BizException;
import net.p5w.dp.common.query.InvoiceInfoQuery;
import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.common.result.Result;
import net.p5w.dp.common.result.ResultCode;
import net.p5w.dp.service.InvoiceInfoService;
import net.p5w.dp.vo.InvoiceInfoVO;/**
 * 发票信息控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/invoice")
public class InvoiceInfoController extends BaseController {

    /** 上传文件大小上限（单位：字节），从配置文件读取，默认 10MB */
    @Value("${file.max-size:10485760}")
    private long maxFileSize;

    @Resource
    private InvoiceInfoService invoiceInfoService;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 分页查询发票列表
     * <p>请求示例：GET /api/invoice/page?page=1&amp;size=10&amp;skr=张三&amp;fprqStart=2026-01-01</p>
     *
     * @param query 分页及筛选条件
     * @return 分页数据
     */
    @GetMapping("/page")
    public Result<PageResult<InvoiceInfoVO>> page(InvoiceInfoQuery query) {
        logStart("发票分页查询", query);
        PageResult<InvoiceInfoVO> pageResult = invoiceInfoService.page(query);
        logEnd("发票分页查询");
        return pageSuccess(pageResult);
    }

    /**
     * 查询发票详情
     *
     * @param id 发票主键
     * @return 发票详情 VO
     */
    @GetMapping("/{id}")
    public Result<InvoiceInfoVO> getDetail(@PathVariable Integer id) {
        logStart("发票详情查询", id);
        InvoiceInfoVO vo = invoiceInfoService.getById(id);
        logEnd("发票详情查询");
        return success(vo);
    }

    /**
     * 上传发票附件
     * <p>请求示例：POST /api/invoice/upload，Content-Type: multipart/form-data</p>
     *
     * @param file 上传的文件（form 字段名: file）
     * @return 文件绝对路径（用于后续写入数据库 filerealpath 字段）
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        logStart("发票文件上传", file.getOriginalFilename());

        // 校验文件非空
        if (file == null || file.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        // 校验文件大小
        if (file.getSize() > maxFileSize) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        try {
            String filePath = invoiceInfoService.uploadFile(file.getOriginalFilename(), file.getBytes());
            logEnd("发票文件上传");
            return success(filePath);
        } catch (IOException e) {
            log.error("读取上传文件失败", e);
            throw new BizException(ResultCode.SERVER_ERROR);
        }
    }

    /**
     * 下载发票附件
     * <p>请求示例：GET /api/invoice/download/1</p>
     *
     * @param id      发票主键
     * @param response HTTP 响应（直接写入文件流）
     */
    @GetMapping("/download/{id}")
    public void download(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logStart("发票文件下载", id);

        String filePath = invoiceInfoService.getFilePath(id);
        if (filePath == null || filePath.isEmpty()) {
            log.warn("发票文件路径为空，id={}", id);
            writeErrorResponse(request, response, ResultCode.NOT_FOUND);
            return;
        }

        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            log.warn("发票文件不存在，filePath={}", filePath);
            writeErrorResponse(request, response, ResultCode.NOT_FOUND);
            return;
        }

        // 设置响应头
        String encodedName;
        try {
            encodedName = URLEncoder.encode(file.getName(), "UTF-8").replace("+", "%20");
        } catch (java.io.UnsupportedEncodingException e) {
            // UTF-8 必定支持，此处不会触发
            encodedName = file.getName();
        }
        response.setContentType("application/octet-stream");
        response.setContentLengthLong(file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedName + "\"");

        // 写入文件流
        try (java.io.FileInputStream fis = new java.io.FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
        } catch (IOException e) {
            log.error("发票文件下载失败，filePath={}", filePath, e);
        }

        logEnd("发票文件下载");
    }

    /**
     * 写入 JSON 格式的错误响应，手动注入 requestId（绕过 ResponseBodyAdvice 时需要）
     */
    private void writeErrorResponse(HttpServletRequest request, HttpServletResponse response, ResultCode code) throws IOException {
        response.setStatus(code.getCode());
        response.setContentType("application/json;charset=UTF-8");
        String requestId = (String) request.getAttribute("requestId");
        Result<?> result = Result.fail(code);
        result.setRequestId(requestId);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
