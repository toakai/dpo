package net.p5w.dp.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;

public class SignUtil {
    public static String sign(Map<String, String> params, String secret) {
        TreeMap<String, String> tree = new TreeMap<>(params);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : tree.entrySet()) {
            sb.append(e.getKey()).append(e.getValue());
        }
        sb.append(secret);
        return md5(sb.toString());
    }

    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(str.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() == 1) sb.append("0");
                sb.append(hex);
            }
            return sb.toString().toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }
}