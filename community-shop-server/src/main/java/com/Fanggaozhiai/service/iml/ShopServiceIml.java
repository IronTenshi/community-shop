package com.Fanggaozhiai.service.iml;

import com.Fanggaozhiai.context.Context;
import com.Fanggaozhiai.dto.ShopPut;
import com.Fanggaozhiai.dto.ShopRegister;
import com.Fanggaozhiai.entity.Merchant;
import com.Fanggaozhiai.mapper.ShopMapper;
import com.Fanggaozhiai.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShopServiceIml implements ShopService {

    @Autowired
    private ShopMapper shopMapper;
    //修改商铺信息
    //name person phone
    @Override
    public void update(ShopPut shopPut) {
        shopMapper.update(shopPut);
    }

    //删除商铺信息
    @Override
    public void delete(Integer id) {
        shopMapper.delete(id);
    }

    //注册商铺
    @Override
    public void register(ShopRegister shopRegister) {
        //获取当前用户id
        Integer usId = Context.getId();
        log.info("用户id :{}", usId);
        Merchant merchant = new Merchant();
        merchant.setName(shopRegister.getName());
        merchant.setPerson(shopRegister.getPerson());
        merchant.setPhone(shopRegister.getPhone());
        merchant.setUsId(usId);
        shopMapper.register(merchant);
    }

    @Override
    public List<Merchant> selectByUsId() {
        //获取当前用户id
        Integer usId = Context.getId();
        return shopMapper.selectByUsId(usId);
    }
}
