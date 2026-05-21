package net.p5w.dp.common.support.orm.db;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 * <p>
 * 基于 ThreadLocal 保存当前线程的数据源标识，
 * Spring 在每次获取连接时调用 {@link #determineCurrentLookupKey()} 决定使用哪个数据源。
 * 数据源切换由 {@link net.p5w.dp.common.aspect.DataSourceAspect} 在方法级别控制。
 * </p>
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final Logger log = LoggerFactory.getLogger(DynamicDataSource.class);

    /** 当前线程绑定的数据源标识 */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    public DynamicDataSource(DataSource defaultTargetDataSource, Map<String, DataSource> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(new HashMap<>(targetDataSources));
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }

    /**
     * 设置当前线程使用的数据源
     *
     * @param dataSource 数据源标识，见 {@link DataSourceEnum}
     */
    public static void setDataSource(String dataSource) {
        CONTEXT_HOLDER.set(dataSource);
        log.debug("切换数据源：{}", dataSource);
    }

    /**
     * 获取当前线程的数据源标识，未设置时使用默认数据源
     *
     * @return 数据源标识
     */
    public static String getDataSource() {
        String dataSource = CONTEXT_HOLDER.get();
        if (dataSource == null) {
            // 未指定时回退到主库
            setDataSource(DataSourceEnum.DPO.getDefault());
        }
        log.debug("当前数据源：{}", CONTEXT_HOLDER.get());
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清除当前线程的数据源标识（方法执行完毕后必须调用，防止 ThreadLocal 泄漏）
     */
    public static void clearDataSource() {
        CONTEXT_HOLDER.remove();
    }
}
