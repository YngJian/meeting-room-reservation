package com.vayne.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Yang Jian
 * @date : 2021/6/28 0028 22:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {
    private int code;
    private String msg;
}
