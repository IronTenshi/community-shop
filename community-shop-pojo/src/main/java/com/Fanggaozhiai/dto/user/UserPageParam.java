package com.Fanggaozhiai.dto.user;

import lombok.Data;

/**
 * 用户分页查询参数
 */
@Data
public class UserPageParam {

    /** 页码，默认1 */
    private Integer page = 1;

    /** 每页数量，默认10 */
    private Integer pageSize = 10;

    /** 用户名（模糊查询） */
    private String username;
}