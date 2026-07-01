package com.Fanggaozhiai.service;

import com.Fanggaozhiai.dto.shop.ShopPageParam;
import com.Fanggaozhiai.dto.shop.ShopPut;
import com.Fanggaozhiai.dto.shop.ShopRegister;
import com.Fanggaozhiai.entity.Merchant;
import com.Fanggaozhiai.result.PageResult;

import java.util.List;

/**
 * 商铺服务接口
 * 定义商铺相关的业务操作，包括注册、查询、修改、删除、权限校验等
 */
public interface ShopService {

    /**
     * 修改商铺信息
     * 只有商铺持有者才能修改（Service层做权限校验）
     *
     * @param shopPut 修改参数，包含 id、name、person、phone
     */
    void update(ShopPut shopPut);

    /**
     * 删除商铺
     * 级联删除商铺下的所有商品，只有商铺持有者才能删除
     *
     * @param id 商铺ID
     */
    void delete(Integer id);

    /**
     * 注册商铺
     * 自动绑定当前登录用户ID，从ThreadLocal获取
     *
     * @param shopRegister 注册参数，包含 name、person、phone
     */
    void register(ShopRegister shopRegister);

    /**
     * 查询当前用户的所有商铺
     * 从ThreadLocal获取当前登录用户ID
     *
     * @return 当前用户的商铺列表
     */
    List<Merchant> selectByUsId();

    /**
     * 分页查询商铺列表
     * 支持按商铺名称模糊查询
     *
     * @param shopPageParam 分页查询参数，包含 page、pageSize、name
     * @return 分页结果
     */
    PageResult<Merchant> list(ShopPageParam shopPageParam);

    /**
     * 进入商铺校验
     * 校验当前用户是否为该商铺的持有者
     *
     * @param id 商铺ID
     * @return 1有权限 0无权限或不存在
     */
    int enterShop(Integer id);
}