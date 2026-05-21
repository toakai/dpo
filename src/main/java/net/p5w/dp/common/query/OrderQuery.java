package net.p5w.dp.common.query;

import lombok.Data;

@Data
public class OrderQuery extends PageQuery {
    private Long userId;
    private String orderNo;
    private Byte status;

}