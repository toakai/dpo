package net.p5w.dp.common.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.annotation.DataSource;
import net.p5w.dp.common.support.orm.db.DynamicDataSource;

/**
 * 动态数据源切换切面
 * <p>
 * 拦截标注了 {@link net.p5w.dp.common.annotation.DataSource} 注解的方法，
 * 在方法执行前切换到指定数据源，执行完毕后清除 ThreadLocal 恢复默认。
 * </p>
 */
@Slf4j
@Aspect
@Component
@Order(-1)
public class DataSourceAspect {

    @Pointcut("@annotation(net.p5w.dp.common.annotation.DataSource)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        String dataSource = signature.getMethod().getAnnotation(DataSource.class).value();

        if (StringUtils.isNotBlank(dataSource)) {
            DynamicDataSource.setDataSource(dataSource);
            log.debug("切换数据源：{}", dataSource);
        }
        // value 为空时不设置，DynamicDataSource 会自动回退到默认数据源（DPO）

        try {
            return point.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
            log.debug("清除数据源绑定");
        }
    }
}
