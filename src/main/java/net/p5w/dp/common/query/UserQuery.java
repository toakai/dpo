package net.p5w.dp.common.query;

import lombok.Data;

@Data
public class UserQuery extends PageQuery {
    // 用户独有查询条件
    private String username;
    private Integer status;
}