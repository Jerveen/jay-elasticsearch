package com.jay.elasticsearch.aop;

import com.jay.elasticsearch.model.dto.AOPLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 切面外部接口日志类
 * @Author: xyw
 * @CreateDt: 2019-04-09
 */
@Configuration
@Aspect // 声明了切面类
@Slf4j
public class LogAspect {

    @Around("within(@org.springframework.web.bind.annotation.RestController *)") // 申明了一个表达式 织入目标特性
    public Object simpleAop(final ProceedingJoinPoint point) {
        Object object = new Object();
        try {
            long start = System.currentTimeMillis();
            String sign = point.getSignature().toString();// 获取类名cui
            String methodName = point.getSignature().getName();// 获取方法名称
            StringBuffer parameter = new StringBuffer("{");// 获取参数
            for (int i = 0; i < point.getArgs().length; i++) {
                parameter.append(String.valueOf(point.getArgs()[i])).append(",");
            }
            parameter.append("}");
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            object = point.proceed();// 让目标方法继续执行
            long end = System.currentTimeMillis();
            long time = end - start;
            AOPLogDTO logDTO = new AOPLogDTO(sign, methodName, parameter.toString(), getIP(request), String.valueOf(time), String.valueOf(object));
            log.info("[aopLog]-[controller]-[info]:" + logDTO.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[aopLog]-[controller]-[exception]:" + e.getStackTrace());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error("[aopLog]-[controller]-[exception]:" + throwable.getLocalizedMessage());
        }
        return object;
    }

    /**
     * 获取IP地址
     *
     * @param request
     * @return
     */
    public String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
