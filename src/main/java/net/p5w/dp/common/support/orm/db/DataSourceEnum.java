package net.p5w.dp.common.support.orm.db;

import lombok.Getter;

/**
 * 数据源枚举
 * <p>
 * 定义项目中所有数据源标识，与 {@link DynamicDataSourceConfig} 中注册的 key 对应。
 * </p>
 *
 * @see DynamicDataSource
 */
@Getter
public enum DataSourceEnum {

    /** 主库（默认数据源） */
    DPO("dpo-data-source", true),

    /** 从库（Ecology） */
    ECOLOGY("ecology-data-source", false);

    /** 数据源标识，对应 DynamicDataSource.targetDataSources 的 key */
    private final String name;

    /** 是否为主库 */
    private final boolean master;

    DataSourceEnum(String name, boolean master) {
        this.name = name;
        this.master = master;
    }

    /**
     * 获取默认数据源标识（master = true 的数据源）
     *
     * @return 默认数据源标识
     */
    public String getDefault() {
        for (DataSourceEnum item : values()) {
            if (item.master) {
                return item.getName();
            }
        }
        return "";
    }
}
