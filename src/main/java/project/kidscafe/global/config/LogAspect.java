package project.kidscafe.global.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import project.kidscafe.api.ApiRequest;
import project.kidscafe.api.ApiResponse;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("within(project.kidscafe.api.*.controller.*)")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void before(JoinPoint joinPoint) {

        log.info("########## Start Request, Controller = {} Method = {}", joinPoint.getSignature().getDeclaringType().getSimpleName()
                                                                        , joinPoint.getSignature().getName());

        Arrays.stream(joinPoint.getArgs())
                .map(arg -> ApiRequest.of(arg, arg.getClass()))
                .forEach(a -> log.info("########## Parameters = {}", a.getParams()));

    }

    @AfterReturning(value = "pointCut()", returning = "response")
    public void afterReturn(JoinPoint joinPoint, ApiResponse<?> response) {

        log.info("########## End {} Request", joinPoint.getSignature().getName());

        String message = response.getMessage();
        if(message != null) {
            log.info("########## Response message = {} ", message);
        }

        Object data = response.getData();
        if(data != null) {
            log.info("########## Response Data = {} ", data.toString());
        }
    }

}
