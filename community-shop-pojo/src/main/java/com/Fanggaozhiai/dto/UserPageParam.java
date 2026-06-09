package com.Fanggaozhiai.dto;

import lombok.Data;

@Data
public class UserPageParam {
    private Integer page = 1;
    private Integer pageSize = 10;
    private String username;
}
