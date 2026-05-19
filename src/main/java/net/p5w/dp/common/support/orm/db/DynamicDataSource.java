package net.p5w.dp.common.support.orm.db;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 *
 * @author Skyle
 * @ClassName: DynamicDataSource
 * @Description: TODO
 * @date 2018年4月30日 下午10:31:30
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    public DynamicDataSource(DataSource defaultTargetDataSource, Map<String, DataSource> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(new HashMap<>(targetDataSources));
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String currDataSource = getDataSource();
        return currDataSource;
    }

    /**
     * 设置数据源
     *
     * @param dataSource
     */
    public static void setDataSource(String dataSource) {
        CONTEXT_HOLDER.set(dataSource);
        System.err.println("[将当前数据源改为]：" + dataSource);
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public static String getDataSource() {
        String dataSource = CONTEXT_HOLDER.get();
        // 如果没有指定数据源，使用默认数据源
        if (null == dataSource) {
            DynamicDataSource.setDataSource(DataSourceEnum.DPO.getDefault());
        }
        System.err.println("[获取当前数据源的类型为]：" + dataSource);
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清除数据源
     */
    public static void clearDataSource() {
        CONTEXT_HOLDER.remove();
    }
}
