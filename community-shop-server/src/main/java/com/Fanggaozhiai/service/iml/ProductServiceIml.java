package com.Fanggaozhiai.service.iml;

import com.Fanggaozhiai.context.Context;
import com.Fanggaozhiai.dto.product.ProductPageParam;
import com.Fanggaozhiai.entity.Product;
import com.Fanggaozhiai.mapper.ProductMapper;
import com.Fanggaozhiai.mapper.ShopMapper;
import com.Fanggaozhiai.result.PageResult;
import com.Fanggaozhiai.service.ProductService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品服务实现类
 * 负责商品相关的业务逻辑处理
 */
@Slf4j
@Service
public class ProductServiceIml implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ShopMapper shopMapper;

    /**
     * 分页查询商品列表
     * 支持按商品名称进行模糊查询
     *
     * @param productPageParam 分页查询参数，包含页码、页大小和商品名称
     * @return 分页结果，包含总记录数和当前页商品列表
     */
    @Override
    public PageResult<Product> list(ProductPageParam productPageParam) {
        // 开启分页插件，自动对后续的SQL查询进行分页处理
        PageHelper.startPage(productPageParam.getPage(), productPageParam.getPageSize());
        // 执行查询
        List<Product> list = productMapper.list(productPageParam);
        // 将查询结果强转为Page类型，以便获取分页信息（总记录数等）
        Page<Product> page = (Page<Product>) list;
        // 封装并返回分页结果
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    /**
     * 添加商品
     * 核心业务逻辑：只有商铺的持有者（即该商铺对应的用户）才能添加商品
     * 权限校验流程：
     * 1. 从ThreadLocal上下文中获取当前登录用户的ID
     * 2. 根据商品所属的商铺ID（merId）查询该商铺的持有者用户ID
     * 3. 比对两者是否一致，不一致则抛出无权限异常
     * 4. 校验通过后才执行商品添加操作
     *
     * @param product 待添加的商品信息，包含名称、描述、图片、所属商铺ID等
     */
    @Override
    public void add(Product product) {
        // 从上下文中获取当前登录用户的ID
        // Context使用ThreadLocal存储，由UserPermissionFilter在请求进入时设置
        Integer currentUserId = Context.getId();
        log.info("当前登录用户ID: {}, 尝试添加商品到商铺ID: {}", currentUserId, product.getMerId());

        // 根据商品关联的商铺ID，查询该商铺对应的用户ID（即商铺持有者）
        Integer shopOwnerId = shopMapper.selectById(product.getMerId());
        log.info("商铺ID: {} 的持有者为用户ID: {}", product.getMerId(), shopOwnerId);

        // 权限校验：判断当前用户是否为该商铺的持有者
        if (shopOwnerId == null) {
            // 商铺不存在的情况
            log.error("商铺不存在，商铺ID: {}", product.getMerId());
            throw new RuntimeException("商铺不存在，无法添加商品");
        }

        if (!currentUserId.equals(shopOwnerId)) {
            // 当前用户不是该商铺的持有者，拒绝操作
            log.warn("权限拒绝：用户ID {} 尝试操作商铺ID {}，但该商铺持有者为用户ID {}",
                    currentUserId, product.getMerId(), shopOwnerId);
            throw new RuntimeException("无权限：您不是该商铺的持有者，无法添加商品");
        }

        // 权限校验通过，执行商品添加
        log.info("权限校验通过，用户ID {} 正在向商铺ID {} 添加商品", currentUserId, product.getMerId());
        productMapper.add(product);
        log.info("商品添加成功");
    }

    /**
     * 删除商品
     * 权限校验：只有商品所属商铺的持有者才能删除
     * 校验流程：
     * 1. 根据商品ID查询商品信息，获取所属商铺ID
     * 2. 查询该商铺的持有者用户ID
     * 3. 比对当前登录用户ID与商铺持有者ID
     * 4. 校验通过后执行删除
     *
     * @param id 商品ID
     */
    @Override
    public void delete(Integer id) {
        Integer currentUserId = Context.getId();
        log.info("当前登录用户ID: {}, 尝试删除商品ID: {}", currentUserId, id);

        // 查询商品信息，获取所属商铺ID
        Product product = productMapper.selectById(id);
        if (product == null) {
            log.error("商品不存在，商品ID: {}", id);
            throw new RuntimeException("商品不存在");
        }

        // 查询该商铺的持有者
        Integer shopOwnerId = shopMapper.selectById(product.getMerId());
        if (shopOwnerId == null) {
            log.error("商铺不存在，商铺ID: {}", product.getMerId());
            throw new RuntimeException("商铺不存在");
        }

        if (!currentUserId.equals(shopOwnerId)) {
            log.warn("权限拒绝：用户ID {} 尝试删除商品ID {}，但该商铺持有者为用户ID {}",
                    currentUserId, id, shopOwnerId);
            throw new RuntimeException("无权限：您不是该商铺的持有者，无法删除商品");
        }

        productMapper.deleteById(id);
        log.info("商品删除成功，商品ID: {}", id);
    }

    /**
     * 修改商品上下架状态
     * 商家可以随时设置商品为有货(0)或无货(1)
     * 权限校验：只有商品所属商铺的持有者才能修改
     *
     * @param id    商品ID
     * @param stage 商品状态，0有货（上架），1无货（下架）
     */
    @Override
    public void updateStage(Integer id, Integer stage) {
        Integer currentUserId = Context.getId();
        log.info("当前登录用户ID: {}, 修改商品ID: {} 的状态为: {}", currentUserId, id, stage);

        if (stage == null || (stage != 0 && stage != 1)) {
            throw new RuntimeException("商品状态参数无效，只能为0（有货）或1（无货）");
        }

        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        Integer shopOwnerId = shopMapper.selectById(product.getMerId());
        if (shopOwnerId == null) {
            throw new RuntimeException("商铺不存在");
        }

        if (!currentUserId.equals(shopOwnerId)) {
            throw new RuntimeException("无权限：您不是该商铺的持有者，无法修改商品状态");
        }

        productMapper.updateStage(id, stage);
        log.info("商品状态修改成功，商品ID: {}, 新状态: {}", id, stage == 0 ? "有货" : "无货");
    }
}
