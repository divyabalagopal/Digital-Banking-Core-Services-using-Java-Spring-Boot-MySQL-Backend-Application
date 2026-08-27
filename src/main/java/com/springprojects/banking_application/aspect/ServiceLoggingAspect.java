package com.springprojects.banking_application.aspect;

import com.itextpdf.text.log.LoggerFactory;
import org.slf4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLoggingAspect {

    private static final Logger log = (Logger) LoggerFactory.getLogger(ServiceLoggingAspect.class);

    // POINTCUT: which methods do I care about?
    // "Any method, in any class, inside the service package"
    @Pointcut("execution(* com.springprojects.banking_application.service..*(..))")
    public void serviceLayerMethods() {}
    // This method itself does nothing — it's just a named handle for the pointcut expression above,
    // so other advice methods can reference "serviceLayerMethods()" instead of repeating the expression.

    // ADVICE: what do I do, and when?
    @Around("serviceLayerMethods()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();
        long start = System.currentTimeMillis();

        log.info("STARTING: {}", methodName);

        try {
            // This is the actual moment your REAL method (e.g., transferRequest) runs
            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - start;
            log.info("SUCCESS: {} completed in {}ms", methodName, duration);

            return result;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.error("FAILED: {} after {}ms — {}", methodName, duration, e.getMessage());
            throw e;   // re-throw, don't swallow the real exception
        }
    }
}
