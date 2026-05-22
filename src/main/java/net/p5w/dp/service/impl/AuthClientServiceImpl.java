package net.p5w.dp.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.util.SignUtil;
import net.p5w.dp.entity.AuthClient;
import net.p5w.dp.entity.Nonce;
import net.p5w.dp.mapper.AuthClientMapper;
import net.p5w.dp.mapper.NonceMapper;
import net.p5w.dp.service.AuthClientService;

@Slf4j
@Service
public class AuthClientServiceImpl implements AuthClientService {

    @Resource
    private AuthClientMapper authClientMapper;

    @Resource
    private NonceMapper nonceMapper;

    /** 签名/nonce 有效期（毫秒），默认 5 分钟，从配置文件读取 */
    @Value("${auth.expire-time:300000}")
    private long expireTime;

    @Override
    public AuthClient selectByPrimaryKey(Long id) {
        return authClientMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(AuthClient record) {
        return authClientMapper.updateByPrimaryKey(record);
    }

    @Override
    public boolean checkSign(String appKey, String sign, String timestamp, String nonce, String params) {
        // 1.查询客户端密钥
        AuthClient client = authClientMapper.getByAppKey(appKey);
        log.debug("client: {}", client);
        if (client == null || client.getStatus() != 1) {
            return false;
        }

        // 2.校验时间戳是否过期
        long currentTime = System.currentTimeMillis();
        long reqTime;
        try {
            reqTime = Long.parseLong(timestamp);
        } catch (Exception e) {
            log.warn("时间戳格式错误，timestamp: {}", timestamp);
            return false;
        }
        if (Math.abs(currentTime - reqTime) > expireTime) {
            log.warn("时间戳过期，currentTime: {}, reqTime: {}", currentTime, reqTime);
            return false;
        }

        // 3.存入nonce实现防重放，重复直接失败
        Nonce nonceEntity = new Nonce();
        nonceEntity.setNonce(nonce);
        nonceEntity.setCreateTime(new Date());
        try {
            nonceMapper.insert(nonceEntity);
        } catch (Exception e) {
            // 唯一索引冲突=重复请求
            log.warn("重复请求，nonce: {}", nonce);
            return false;
        }

        // 4.后端重算签名对比
        String buildStr = appKey + timestamp + nonce + (params == null ? "" : params) + client.getAppSecret();
        String serverSign = SignUtil.md5(buildStr);
        log.debug("serverSign: {}, sign: {}", serverSign, sign);
        return serverSign.equalsIgnoreCase(sign);
    }
}
