package com.Fanggaozhiai.context;

//上下文 接收id
public class Context {
    private static final ThreadLocal<Integer> IdHolder = new ThreadLocal<>();

    public static void setUserId(Integer Id) {
        IdHolder.set(Id);
    }

    public static Integer getId() {
        return IdHolder.get();
    }

    public static void clear() {
        IdHolder.remove();  // 必须清理，防止内存泄漏
    }
}
