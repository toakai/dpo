package net.p5w.dp.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据源注解
 *
 * @author Skyle
 * @ClassName: DataSource
 * @Description: TODO
 * @date 2018年4月30日 下午3:50:01
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

    String value() default "";
}
