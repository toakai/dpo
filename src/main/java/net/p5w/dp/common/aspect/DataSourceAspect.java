package net.p5w.dp.common.aspect;

import java.lang.reflect.Method;

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
import net.p5w.dp.common.support.orm.db.DataSourceEnum;
import net.p5w.dp.common.support.orm.db.DynamicDataSource;

/**
 * 数据源切面处理
 *
 * @author Skyle
 * @ClassName: DataSourceAspect
 * @Description: TODO
 * @date 2018年4月30日 下午3:51:25
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
        Method method = signature.getMethod();
        DataSource ds = method.getAnnotation(DataSource.class);

        String dataSource = ds.value();
        if (StringUtils.isBlank(dataSource)) {
            DynamicDataSource.setDataSource(DataSourceEnum.ECOLOGY.getName());
            log.debug("set datasource is null, use datasource : {}", dataSource);
            System.err.println("set datasource is null, use datasource : " + dataSource);
        } else {
            DynamicDataSource.setDataSource(dataSource);
            log.debug("use datasource : {}", dataSource);
        }

        try {
            return point.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
            log.debug("clear datasource...");
        }
    }
}
