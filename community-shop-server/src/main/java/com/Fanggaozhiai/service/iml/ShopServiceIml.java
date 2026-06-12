package com.Fanggaozhiai.service.iml;

import com.Fanggaozhiai.context.Context;
import com.Fanggaozhiai.dto.ShopPageParam;
import com.Fanggaozhiai.dto.ShopPut;
import com.Fanggaozhiai.dto.ShopRegister;
import com.Fanggaozhiai.entity.Merchant;
import com.Fanggaozhiai.mapper.ShopMapper;
import com.Fanggaozhiai.result.PageResult;
import com.Fanggaozhiai.service.ShopService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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

    //模糊查询商铺信息
    //分页查询
    @Override
    public PageResult<Merchant> list(ShopPageParam shopPageParam) {
        //开启分页
        PageHelper.startPage(shopPageParam.getPage(),shopPageParam.getPageSize());
        //查询到所有数据
        List<Merchant> list = shopMapper.list(shopPageParam);
        //强转为 list 类型以用来返回到前端
        Page<Merchant> page = (Page<Merchant>) list;
        //返回分页数据
        return new PageResult<>(page.getTotal(),page.getResult());
    }

    @Override
    public int enterShop(Integer id) {
        //获取当前用户id
        Integer usId = Context.getId();
        Integer ussId = shopMapper.selectById(id);
        if(usId.equals(ussId)){
            log.info("进入商铺");
            return 1;
        }
        else {
            log.info("无权限进入商铺");
            return 0;
        }
    }
}
