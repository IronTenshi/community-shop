package com.Fanggaozhiai.context;

import lombok.extern.slf4j.Slf4j;

//上下文 接收id
@Slf4j
public class Context {
    private static final ThreadLocal<Integer> IdHolder = new ThreadLocal<>();

    public static void setId(Integer Id) {
        log.info("设置用户id: {}", Id);
        IdHolder.set(Id);
    }

    public static Integer getId() {
        log.info("获取用户id: {}", IdHolder.get());
        return IdHolder.get();
    }

    public static void clear() {
        IdHolder.remove();  // 必须清理，防止内存泄漏
    }
}
