package org.peter.chat.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.peter.chat.exception.BusinessException;
import org.springframework.stereotype.Component;

/**
 * 处理日志的切面
 */
@Slf4j
@Component
@Aspect
public class LogAop {
    @Pointcut("execution(* org.peter.chat.service.app.impl.*.*(..))")
    private void service() {
    }

    @Around("service()")
    public Object handler(ProceedingJoinPoint pjp) throws Throwable {
        return doLog(pjp);
    }

    // 执行打日志逻辑
    private Object doLog(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        try {
            // 执行目标方法
            result = pjp.proceed();
        } catch (Throwable e) {
            // 如果属于应用异常,打印日志
            if (e instanceof BusinessException) {
                log.warn(((BusinessException) e).getSnapshot(), e);
            } else {
                // 发生运行时异常
                log.warn(e.getMessage(), e);
            }
            // 继续将异常抛给controller层
            throw e;
        }
        return result;
    }
}
