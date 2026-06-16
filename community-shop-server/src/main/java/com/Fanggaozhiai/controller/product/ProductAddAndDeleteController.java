package com.Fanggaozhiai.controller.product;

import com.Fanggaozhiai.entity.Product;
import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.ProductService;
import com.Fanggaozhiai.utils.AliyuOSSUtil;
import com.aliyuncs.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 商品添加与删除控制器
 * 负责处理商品的添加、图片上传等操作
 *
 * 路径设计说明：
 * 使用 "/users/products" 作为基础路径，使其被 UserPermissionFilter 拦截
 * UserPermissionFilter 拦截 "/users/*" 路径，会校验请求头中的 token
 * 只有携带有效用户 token 的请求才能访问本控制器中的接口
 *
 * 商铺ID（merId）通过URL路径参数传递，而非请求体
 * 前端进入商铺管理页面时，URL格式为 /shops/{merId}/xxx
 * 添加商品时，将 merId 放在URL路径中，后端自动提取并注入
 * 这样前端无需在请求体中手动传递 merId，也避免了前端篡改的风险
 */
@Slf4j
@RestController
@RequestMapping("/users/products")
public class ProductAddAndDeleteController {

    @Autowired
    private AliyuOSSUtil aliyuOSSUtil;

    @Autowired
    private ProductService productService;

    /**
     * 添加商品接口
     * 只有商铺的持有者才能成功添加商品
     *
     * 请求流程：
     * 1. 请求首先经过 UserPermissionFilter 过滤器
     * 2. 过滤器从请求头中获取 token，校验用户是否登录
     * 3. 校验通过后，将用户ID存入 Context（ThreadLocal）
     * 4. 进入本方法，从URL路径中提取商铺ID（merId），接收请求体中的商品信息
     * 5. 将 merId 注入到 Product 对象中
     * 6. 调用 ProductService.add() 进行权限校验和商品添加
     * 7. ProductService.add() 中会校验当前用户是否为该商铺持有者
     *
     * 前端调用示例：
     * POST /users/products/1
     * Header: token=xxx
     * Body: { "name": "苹果", "describe": "新鲜", "img": "https://xxx.jpg", "stage": 0 }
     * （注意：Body中不需要传 merId，它从URL路径 /1 中自动提取）
     *
     * @param merId   商铺ID，从URL路径中自动提取，标识要向哪个商铺添加商品
     * @param product 商品信息，包含：
     *                - name: 商品名称（必填）
     *                - describe: 商品描述（必填）
     *                - img: 商品图片URL（必填，先调用/upload接口上传图片获取）
     *                - stage: 商品状态（可选，0有货 1无货）
     * @return 操作结果，成功返回 success，失败返回 error（无权限或商铺不存在）
     */
    @PostMapping("/{merId}")
    public Result add(@PathVariable Integer merId, @RequestBody Product product) {
        log.info("收到添加商品请求，目标商铺ID: {}, 商品名称: {}", merId, product.getName());
        // 将URL路径中的商铺ID注入到商品对象中
        // 前端无需在请求体中传递 merId，由后端从URL自动提取，更加安全
        product.setMerId(merId);
        // 调用服务层添加商品
        // 服务层会自动进行权限校验：判断当前登录用户是否为该商铺的持有者
        productService.add(product);
        log.info("商品添加请求处理完成");
        return Result.success();
    }

    /**
     * 上传商品图片到阿里云OSS接口
     * 图片上传接口不需要权限校验，因为上传图片本身不涉及商铺数据操作
     * 前端应先调用此接口上传图片，获取图片URL后再调用添加商品接口
     *
     * 前端调用示例：
     * POST /users/products/upload
     * Content-Type: multipart/form-data
     * Body: file=图片文件
     *
     * @param file 上传的图片文件（multipart/form-data格式）
     * @return 操作结果，成功返回图片的OSS访问URL（可用于商品img字段）
     * @throws IOException 文件读取异常
     * @throws ClientException OSS上传异常
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException, ClientException {
        log.info("收到图片上传请求，文件名: {}", file.getOriginalFilename());
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            log.error("上传失败：文件名为空");
            return Result.error("上传失败");
        }
        // 调用阿里云OSS工具类上传文件
        String url = aliyuOSSUtil.upload(file.getBytes(), originalFilename);
        log.info("图片上传成功，访问URL: {}", url);
        return Result.success(url);
    }
}
