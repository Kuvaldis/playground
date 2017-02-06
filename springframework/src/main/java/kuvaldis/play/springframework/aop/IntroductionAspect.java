package kuvaldis.play.springframework.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareParents;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class IntroductionAspect {

    // value defines types with new parent (UsageTracked in our case)
    @DeclareParents(value = "kuvaldis.play.springframework.aop.*Service", defaultImpl = DefaultUsageTracked.class)
    public static UsageTracked mixin;

    @Pointcut("execution(* kuvaldis.play.springframework.aop.*.*(..))")
    public void businessService() {}

    // is called whenever join point is called. In our case it's SomeService::call
    @Before("kuvaldis.play.springframework.aop.IntroductionAspect.businessService() && this(usageTracked) && target(someService)")
    public void recordUsage(final UsageTracked usageTracked, final SomeService someService) {
        // here usageTracked is proxy, target of which is SomeService
        usageTracked.incrementUseCount();
        System.out.println(usageTracked.getClass());
        System.out.println(someService.getClass());
    }
}
