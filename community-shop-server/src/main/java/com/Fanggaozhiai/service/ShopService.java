package com.Fanggaozhiai.service;

import com.Fanggaozhiai.dto.ShopPut;
import com.Fanggaozhiai.dto.ShopRegister;
import com.Fanggaozhiai.entity.Merchant;

import java.util.List;

public interface ShopService {
    //修改商铺信息
    //name person phone
    void update(ShopPut shopPut);

    //删除商铺信息
    void delete(Integer id);

    //注册商铺
    //根据用户id 绑定商铺
    void register(ShopRegister shopRegister);

    //查询商铺信息
    List<Merchant> selectByUsId();
}
