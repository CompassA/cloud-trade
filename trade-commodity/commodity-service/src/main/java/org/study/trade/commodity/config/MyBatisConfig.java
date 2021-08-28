package org.study.trade.commodity.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * 装配mybatis
 * {@link org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration#sqlSessionFactory(javax.sql.DataSource)}
 * @author Tomato
 * Created on 2021.08.28
 */
@Configuration
@MapperScan(
        basePackages = {"org.study.trade.commodity.mapper"},
        sqlSessionFactoryRef = "tradeSqlSessionFactory",
        sqlSessionTemplateRef = "tradeSqlSessionTemplate")
public class MyBatisConfig {

    private final DataSource shardingDataSource;

    private final String tradeMapperLocation;

    public MyBatisConfig(DataSource shardingDataSource) {
        this.shardingDataSource = shardingDataSource;
        this.tradeMapperLocation = "classpath:mapper/*.xml";
    }

    @Bean
    @Primary
    public SqlSessionFactory tradeSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(shardingDataSource);
        factory.setVfs(SpringBootVFS.class);
        factory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(tradeMapperLocation));
        return factory.getObject();
    }

    @Bean
    @Primary
    public SqlSessionTemplate tradeSqlSessionTemplate(
            @Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
