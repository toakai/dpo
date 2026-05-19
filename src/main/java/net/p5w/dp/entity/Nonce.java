package net.p5w.dp.entity;

import java.util.Date;

import lombok.Data;

@Data
public class Nonce {
    private Long id;

    private String nonce;

    private Date createTime;
}