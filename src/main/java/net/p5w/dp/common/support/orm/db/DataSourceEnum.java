package net.p5w.dp.common.support.orm.db;

/**
 * 数据源上下文
 *
 * @author Skyle
 * @ClassName: DataSourceEnum
 * @Description: TODO
 * @date 2018年5月1日 上午12:49:36
 */
public enum DataSourceEnum {
    DPO("dpo-data-source", true),
    ECOLOGY("ecology-data-source", false);

    private String name;

    private boolean master;

    DataSourceEnum(String name, boolean master) {
        this.name = name;
        this.master = master;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public String getDefault() {
        String defaultDataSource = "";
        for (DataSourceEnum dataSourceEnum : DataSourceEnum.values()) {
            if (!"".equals(defaultDataSource)) {
                break;
            }
            if (dataSourceEnum.master) {
                defaultDataSource = dataSourceEnum.getName();
            }
        }
        return defaultDataSource;
    }
}
