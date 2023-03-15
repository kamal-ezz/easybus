package com.kamal.easybus.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.kamal.easybus.services(..))")
    public void loggingAdvice(){
        System.out.println("HELLO Advice");
    }

}
