package org.study.trade.commodity.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.study.trade.commodity.config.constant.DbSequenceKey;
import org.study.trade.commodity.sequence.DbSequenceGenerator;
import org.study.trade.commodity.sequence.SequencePOMapper;
import org.study.trade.common.sequence.SequenceGenerator;

import javax.sql.DataSource;

/**
 * 装配SequenceDB数据源
 * @author Tomato
 * Created on 2021.08.26
 */
@Configuration
@MapperScan(
        basePackages = {"org.study.trade.commodity.sequence"},
        sqlSessionFactoryRef = "sequenceSqlSessionFactory",
        sqlSessionTemplateRef = "sequenceSqlSessionTemplate")
public class SequenceConfig {

    private final ShardingDataSource shardingDataSource;

    private final String sequenceMapperLocation;

    public SequenceConfig(DataSource shardingDataSource) {
        this.shardingDataSource = (ShardingDataSource) shardingDataSource;
        this.sequenceMapperLocation = "classpath:sequence/*.xml";
    }

    @Bean
    public SqlSessionFactory sequenceSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(
                shardingDataSource.getDataSourceMap().get("sequence")
        );
        factory.setVfs(SpringBootVFS.class);
        factory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(sequenceMapperLocation));
        return factory.getObject();
    }

    @Bean
    public SqlSessionTemplate sequenceSqlSessionTemplate(
            @Qualifier("sequenceSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public SequenceGenerator dbSequenceGenerator(final SequencePOMapper mapper) {
        return new DbSequenceGenerator(DbSequenceKey.COMMODITY_KEY, mapper);
    }
}
