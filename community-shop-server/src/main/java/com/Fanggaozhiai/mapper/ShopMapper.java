package com.Fanggaozhiai.mapper;

import com.Fanggaozhiai.dto.shop.ShopPageParam;
import com.Fanggaozhiai.dto.shop.ShopPut;
import com.Fanggaozhiai.entity.Merchant;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商铺数据访问层接口
 * 负责商铺相关的数据库操作
 * 对应的XML映射文件位于：resources/com/Fanggaozhiai/mapper/ShopMapper.xml
 */
@Mapper
public interface ShopMapper {

    /**
     * 修改商铺信息
     * 支持修改 name、person、phone
     *
     * @param shopPut 修改参数，包含 id、name、person、phone
     */
    void update(ShopPut shopPut);

    /**
     * 根据ID删除商铺
     *
     * @param id 商铺ID
     */
    @Delete("delete from merchant where id = #{id}")
    void delete(Integer id);

    /**
     * 查询当前用户的所有商铺
     *
     * @param usId 用户ID
     * @return 商铺列表
     */
    @Select("select * from merchant where us_id = #{usId}")
    List<Merchant> selectByUsId(Integer usId);

    /**
     * 注册商铺
     *
     * @param merchant 商铺信息，包含 name、person、phone、usId
     */
    @Insert("INSERT INTO merchant(name, person, phone, us_id) VALUES(#{name},#{person},#{phone},#{usId}) ")
    void register(Merchant merchant);

    /**
     * 查询所有商铺
     *
     * @return 所有商铺列表
     */
    @Select("select * from merchant")
    List<Merchant> selectAll();

    /**
     * 根据商铺ID查询持有者用户ID
     * 用于权限校验：判断当前用户是否为该商铺的持有者
     *
     * @param id 商铺ID
     * @return 商铺持有者的用户ID
     */
    @Select("select us_id from merchant where id = #{id}")
    Integer selectById(Integer id);

    /**
     * 分页查询商铺列表
     * 支持按商铺名称模糊查询
     *
     * @param shopPageParam 查询参数，包含 page、pageSize、name
     * @return 商铺列表
     */
    List<Merchant> list(ShopPageParam shopPageParam);
}