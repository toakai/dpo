package net.p5w.dp.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 用户+订单列表 VO（一对多嵌套）
 * <p>
 * 用于分页查询用户时，同时嵌套带出该用户下的所有订单。
 * </p>
 */
@Data
public class UserOrderVO {

    private Long id;

    private String username;

    private String realName;

    private String phone;

    private Date createTime;

    /** 该用户下的订单列表 */
    private List<OrderVO> orders;
}
