package net.p5w.dp.vo;

import java.util.Date;

import lombok.Data;

/**
 * 用户列表视图返回对象
 * 仅返回前端展示所需字段，屏蔽敏感、冗余字段
 */
@Data
public class UserVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 登录账号
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 创建时间
     */
    private Date createTime;
}