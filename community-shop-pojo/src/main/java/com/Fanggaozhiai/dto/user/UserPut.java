package com.Fanggaozhiai.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPut {
    private Integer id;
    private String username;
    private String password;
    private String phone;
    private String address;
}
