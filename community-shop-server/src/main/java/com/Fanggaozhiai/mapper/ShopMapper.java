package com.Fanggaozhiai.mapper;

import com.Fanggaozhiai.dto.ShopPut;
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
    @Insert("INSERT INTO merchant(id, name, person, phone, us_id) VALUES(#{id},#{name},#{person},#{phone},#{usId}) ")
    void register(Merchant merchant);
}
