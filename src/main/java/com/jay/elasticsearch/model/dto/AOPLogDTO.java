package com.jay.elasticsearch.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 日志说明Dto对象
 * @Author: xyw
 * @CreateDt: 2019-04-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AOPLogDTO implements Serializable {
    private String className;
    private String methodName;
    private String methodParam;
    private String fromIp;
    private String timeSpent;
    private String returnMsg;
}
