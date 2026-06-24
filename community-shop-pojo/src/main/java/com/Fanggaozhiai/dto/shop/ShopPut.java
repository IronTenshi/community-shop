package com.Fanggaozhiai.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopPut {
    private Integer id;
    private String name;
    private String person;
    private String phone;
}
