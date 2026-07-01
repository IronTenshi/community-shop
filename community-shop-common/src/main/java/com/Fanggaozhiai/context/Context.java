package com.Fanggaozhiai.context;

import lombok.extern.slf4j.Slf4j;

/**
 * 线程上下文工具类
 * 使用ThreadLocal存储当前请求的用户ID，确保线程安全
 * 由Filter在请求进入时设置，在请求结束时清除
 */
@Slf4j
public class Context {

    private static final ThreadLocal<Integer> IdHolder = new ThreadLocal<>();

    /**
     * 设置当前用户ID
     * 由Filter在token校验通过后调用
     *
     * @param Id 用户/员工ID
     */
    public static void setId(Integer Id) {
        log.info("设置用户id: {}", Id);
        IdHolder.set(Id);
    }

    /**
     * 获取当前用户ID
     * 业务代码通过此方法获取当前登录用户的ID
     *
     * @return 当前用户/员工ID
     */
    public static Integer getId() {
        log.info("获取用户id: {}", IdHolder.get());
        return IdHolder.get();
    }

    /**
     * 清除ThreadLocal
     * 防止内存泄漏，必须在请求处理完成后调用
     */
    public static void clear() {
        IdHolder.remove();
    }
}