package net.p5w.dp.service;

import net.p5w.dp.entity.AuthClient;
public interface AuthClientService{

    AuthClient selectByPrimaryKey(Long id);

    int updateByPrimaryKey(AuthClient record);

    boolean checkSign(String appKey, String sign, String timestamp, String nonce, String paramsJson);

}
