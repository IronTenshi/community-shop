package com.Fanggaozhiai.mapper;

import com.Fanggaozhiai.dto.shop.ShopPageParam;
import com.Fanggaozhiai.dto.shop.ShopPut;
import com.Fanggaozhiai.entity.Merchant;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShopMapper {

    //修改商铺信息
    //name person phone
    void update(ShopPut shopPut);

    //删除商铺信息 根据id
    @Delete("delete from merchant where id = #{id}")
    void delete(Integer id);

    //查询个人商铺信息
    @Select("select * from merchant where us_id = #{usId}")
    List<Merchant> selectByUsId(Integer usId);

    //注册商铺
    @Insert("INSERT INTO merchant(name, person, phone, us_id) VALUES(#{name},#{person},#{phone},#{usId}) ")
    void register(Merchant merchant);

    //查询所有商铺信息
    @Select("select * from merchant")
    List<Merchant> selectAll();

    //根据id查询商铺 信息
    //校验是否与usid一致
    @Select("select us_id from merchant where id = #{id}")
    Integer selectById(Integer id);

    //分页查询
    //输入name
    List<Merchant> list(ShopPageParam shopPageParam);

//    //查询店铺返回店铺值
//    @Select("select * from merchant where id = #{id}")
//    Merchant select(Integer id);
}
