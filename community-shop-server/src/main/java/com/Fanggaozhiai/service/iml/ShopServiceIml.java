package com.Fanggaozhiai.service.iml;

import com.Fanggaozhiai.context.Context;
import com.Fanggaozhiai.dto.shop.ShopPageParam;
import com.Fanggaozhiai.dto.shop.ShopPut;
import com.Fanggaozhiai.dto.shop.ShopRegister;
import com.Fanggaozhiai.entity.Merchant;
import com.Fanggaozhiai.mapper.ProductMapper;
import com.Fanggaozhiai.mapper.ShopMapper;
import com.Fanggaozhiai.result.PageResult;
import com.Fanggaozhiai.service.ShopService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 商铺服务实现类
 * 负责商铺相关的业务逻辑，包括增删改查、权限校验、级联删除
 */
@Slf4j
@Service
public class ShopServiceIml implements ShopService {

    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private ProductMapper productMapper;

    /**
     * 修改商铺信息
     * 校验是否为商铺持有者本人操作
     *
     * @param shopPut 修改参数，包含 id(商铺ID)、name、person、phone
     */
    @Override
    public void update(ShopPut shopPut) {
        Integer shopOwnerId = shopMapper.selectById(shopPut.getId());
        if(shopOwnerId == null){
            throw new RuntimeException("商铺不存在");
        }
        if(!Objects.equals(Context.getId(), shopOwnerId)){
            throw new RuntimeException("无权限修改");
        }
        shopMapper.update(shopPut);
    }

    /**
     * 删除商铺
     * 校验流程：
     * 1. 商铺必须存在
     * 2. 必须是商铺持有者本人操作
     * 3. 先删除该商铺下所有商品，再删除商铺
     *
     * @param id 商铺ID
     */
    @Transactional
    @Override
    public void delete(Integer id) {
        Integer shopOwnerId = shopMapper.selectById(id);
        if(shopOwnerId == null){
            throw new RuntimeException("商铺不存在");
        }
        if(!Objects.equals(Context.getId(), shopOwnerId)){
            throw new RuntimeException("无权限删除");
        }
        // 级联删除商铺下的商品
        productMapper.deleteByMerId(id);
        shopMapper.delete(id);
    }

    /**
     * 注册商铺
     * 自动绑定当前登录用户ID，从ThreadLocal获取
     *
     * @param shopRegister 注册参数，包含 name、person、phone
     */
    @Override
    public void register(ShopRegister shopRegister) {
        Integer usId = Context.getId();
        log.info("用户id :{}", usId);
        Merchant merchant = new Merchant();
        merchant.setName(shopRegister.getName());
        merchant.setPerson(shopRegister.getPerson());
        merchant.setPhone(shopRegister.getPhone());
        merchant.setUsId(usId);
        shopMapper.register(merchant);
    }

    /**
     * 查询当前用户的所有商铺
     *
     * @return 当前用户的商铺列表
     */
    @Override
    public List<Merchant> selectByUsId() {
        Integer usId = Context.getId();
        return shopMapper.selectByUsId(usId);
    }

    /**
     * 分页模糊查询商铺列表
     * 支持按商铺名称模糊匹配
     *
     * @param shopPageParam 分页查询参数，包含 page、pageSize、name
     * @return 分页结果
     */
    @Override
    public PageResult<Merchant> list(ShopPageParam shopPageParam) {
        PageHelper.startPage(shopPageParam.getPage(),shopPageParam.getPageSize());
        List<Merchant> list = shopMapper.list(shopPageParam);
        Page<Merchant> page = (Page<Merchant>) list;
        return new PageResult<>(page.getTotal(),page.getResult());
    }

    /**
     * 进入商铺校验
     * 校验当前用户是否为该商铺的持有者
     *
     * @param id 商铺ID
     * @return 1有权限 0无权限或不存在
     */
    @Override
    public int enterShop(Integer id) {
        Integer usId = Context.getId();
        Integer ussId = shopMapper.selectById(id);
        if (ussId == null) {
            log.info("商铺不存在");
            return 0;
        }
        if (usId.equals(ussId)) {
            log.info("进入商铺");
            return 1;
        } else {
            log.info("无权限进入商铺");
            return 0;
        }
    }
}