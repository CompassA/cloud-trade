package org.study.trade.commodity.config;

import com.google.common.base.Preconditions;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.apache.shardingsphere.spring.boot.datasource.DataSourcePropertiesSetterHolder;
import org.apache.shardingsphere.spring.boot.util.DataSourceUtil;
import org.apache.shardingsphere.spring.boot.util.PropertyUtil;
import org.apache.shardingsphere.underlying.common.config.inline.InlineExpressionParser;
import org.apache.shardingsphere.underlying.common.exception.ShardingSphereException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * {@link org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration}
 * @author Tomato
 * Created on 2021.09.03
 */
@Configuration
public class ShardingConfig implements EnvironmentAware {

    private static final String CONFIG_PREFIX = "spring.shardingsphere.datasource.";

    private static final String JNDI_NAME = "jndi-name";

    /**
     * 组装好的配置文件中的数据源
     */
    private final Map<String, DataSource> dataSourceMap = new LinkedHashMap<>();

    @Bean
    @Primary
    public DataSource selfDefineShardingSource() throws SQLException {
        // 逻辑库表 commodity 的配置
        TableRuleConfiguration commodityTableConfig = new TableRuleConfiguration(
                "commodity",
                "product-db-${0..1}.commodity_${0..7}"
        );
        commodityTableConfig.setDatabaseShardingStrategyConfig(
                new InlineShardingStrategyConfiguration(
                        "shop_id",
                        "product-db-${shot_id % 2}"
                )
        );
        commodityTableConfig.setTableShardingStrategyConfig(
                new InlineShardingStrategyConfiguration(
                        "id",
                        "commodity_${id % 8}"
                )
        );

        // 逻辑库表 commodity_snapshot 的配置
        TableRuleConfiguration commoditySnapshotConfig = new TableRuleConfiguration(
                "commodity_snapshot",
                "product-db-${0..1}.commodity_snapshot_${0..7}"
        );
        commoditySnapshotConfig.setDatabaseShardingStrategyConfig(
                new InlineShardingStrategyConfiguration(
                        "id",
                        "product-db-${id / 8 % 2}")
        );
        commoditySnapshotConfig.setTableShardingStrategyConfig(
                new InlineShardingStrategyConfiguration(
                        "id",
                        "commodity_snapshot_${id % 8}"
                )
        );

        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        Collection<TableRuleConfiguration> tableRuleConfigs = shardingRuleConfig.getTableRuleConfigs();
        tableRuleConfigs.add(commodityTableConfig);
        tableRuleConfigs.add(commoditySnapshotConfig);

        return ShardingDataSourceFactory.createDataSource(
                dataSourceMap,
                shardingRuleConfig,
                new Properties()
        );
    }

    /**
     * copy from {@link org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration}
     */
    @Override
    public void setEnvironment(Environment environment) {
        for (String each : getDataSourceNames(environment, CONFIG_PREFIX)) {
            try {
                dataSourceMap.put(each, getDataSource(environment, CONFIG_PREFIX, each));
            } catch (final ReflectiveOperationException ex) {
                throw new ShardingSphereException("Can't find datasource type!", ex);
            } catch (final NamingException namingEx) {
                throw new ShardingSphereException("Can't find JNDI datasource!", namingEx);
            }
        }
    }

    /**
     * copy from {@link org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration}
     */
    private List<String> getDataSourceNames(final Environment environment, final String prefix) {
        StandardEnvironment standardEnv = (StandardEnvironment) environment;
        standardEnv.setIgnoreUnresolvableNestedPlaceholders(true);
        return null == standardEnv.getProperty(prefix + "name")
                ? new InlineExpressionParser(standardEnv.getProperty(prefix + "names")).splitAndEvaluate() : Collections.singletonList(standardEnv.getProperty(prefix + "name"));
    }

    /**
     * copy from {@link org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration}
     */
    @SuppressWarnings("unchecked")
    private DataSource getDataSource(final Environment environment, final String prefix, final String dataSourceName) throws ReflectiveOperationException, NamingException {
        Map<String, Object> dataSourceProps = PropertyUtil.handle(environment, prefix + dataSourceName.trim(), Map.class);
        Preconditions.checkState(!dataSourceProps.isEmpty(), "Wrong datasource properties!");
        if (dataSourceProps.containsKey(JNDI_NAME)) {
            return getJndiDataSource(dataSourceProps.get(JNDI_NAME).toString());
        }
        DataSource result = DataSourceUtil.getDataSource(dataSourceProps.get("type").toString(), dataSourceProps);
        DataSourcePropertiesSetterHolder.getDataSourcePropertiesSetterByType(dataSourceProps.get("type").toString()).ifPresent(
                dataSourcePropertiesSetter -> dataSourcePropertiesSetter.propertiesSet(environment, prefix, dataSourceName, result));
        return result;
    }

    /**
     * copy from {@link org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration}
     */
    private DataSource getJndiDataSource(final String jndiName) throws NamingException {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setResourceRef(true);
        bean.setJndiName(jndiName);
        bean.setProxyInterface(DataSource.class);
        bean.afterPropertiesSet();
        return (DataSource) bean.getObject();
    }
}
