package org.peter.chat.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.peter.chat.enums.ChatStatus;
import org.peter.chat.exception.BusinessException;
import org.peter.chat.utils.ResultBean;
import org.springframework.stereotype.Component;

/**
 * 控制器异常处理器
 */
@Component
@Aspect
public class ControllerExceptionHandlerAop {
    /**
     * execution(* com.sample.service.impl..*.*(..))
     * execution（）
     * 表达式的主体；
     * 第一个”*“符号
     * 表示返回值的类型任意；
     * com.sample.service.impl	AOP所切的服务的包名，即，我们的业务部分
     * 包名后面的”..“	表示当前包及子包
     * 第二个”*“	表示类名，*即所有类。此处可以自定义，下文有举例
     * .*(..)	表示任何方法名，括号表示参数，两个点表示任何参数类型
     */
    @Pointcut("execution(* org.peter.chat.controller..*.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object handlerControllerMethod(ProceedingJoinPoint pjp) {
        ResultBean<?> resultBean;
        try {
            resultBean = (ResultBean<?>) pjp.proceed();
        } catch (Throwable throwable) {
            resultBean = handException(throwable);
        }
        return resultBean;
    }

    private ResultBean<?> handException(Throwable e) {
        ResultBean<?> resultBean;

        if (e instanceof BusinessException) {
            resultBean = new ResultBean<>().failed(e.getMessage());
        } else {
            // 发生其他异常,不返回给前端,但是会打印日志
            resultBean = new ResultBean<>()
                    .failed(ChatStatus.APP_RUNTIME_EXCEPTION.getMessage());
        }
        return resultBean;
    }
}
