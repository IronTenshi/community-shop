package com.Fanggaozhiai.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    //总记录数
    private long total;
    //当前页数据
    private List<T> rows;
}
