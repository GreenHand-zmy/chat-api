package org.peter.chat.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.peter.chat.enums.ChatStatus;
import org.peter.chat.exception.BusinessException;
import org.peter.chat.utils.ResultBean;
import org.peter.chat.web.ControllerExceptionHandler;
import org.springframework.stereotype.Component;

/**
 * 控制器异常处理器
 * 拦截控制器方法,但是有缺陷,要是用户访问不存在的api,不会经过aop代理
 * 所以将它废弃,用spring 内置controllerAdvice可以代替
 * @see ControllerExceptionHandler
 */
//@Component
@Aspect
@Deprecated
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
            // 发生其他异常,不返回具体报错信息给前端,后台会打印日志
            resultBean = new ResultBean<>()
                    .failed(ChatStatus.APP_RUNTIME_EXCEPTION.getMessage());
        }
        return resultBean;
    }
}
