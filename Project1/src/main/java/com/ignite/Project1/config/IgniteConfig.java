package com.ignite.Project1.config;

import com.ignite.Project1.model.Product;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.store.cassandra.CassandraCacheStoreFactory;
import org.apache.ignite.cache.store.cassandra.datasource.DataSource;
import org.apache.ignite.cache.store.cassandra.persistence.KeyValuePersistenceSettings;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.cache.expiry.Duration;
import javax.cache.expiry.TouchedExpiryPolicy;

@Configuration
public class IgniteConfig {

    private static final String CACHE_NAME = "Products";

    @Bean
    public DataSource dataSource() {
        DataSource dataSource = new DataSource();
        dataSource.setContactPoints("localhost");
        dataSource.setPort(9042);
        return dataSource;
    }

    @Bean
    public KeyValuePersistenceSettings persistenceSettings() {
        return new KeyValuePersistenceSettings(new ClassPathResource("persistence-settings.xml"));
    }

    @Bean
    public CassandraCacheStoreFactory<String, Product> cacheStoreFactory() {
        CassandraCacheStoreFactory<String, Product> cacheStoreFactory = new CassandraCacheStoreFactory<>();
        cacheStoreFactory.setPersistenceSettings(persistenceSettings());
        cacheStoreFactory.setDataSource(dataSource());

        return cacheStoreFactory;
    }

    @Bean
    public CacheConfiguration<String, Product> cacheConfiguration() {
        CacheConfiguration<String, Product> cacheConfiguration = new CacheConfiguration<>(CACHE_NAME);

        cacheConfiguration.setCacheStoreFactory(cacheStoreFactory());
        cacheConfiguration.setReadThrough(true);
        cacheConfiguration.setWriteThrough(true);
        cacheConfiguration.setWriteBehindEnabled(false);
        cacheConfiguration.setStatisticsEnabled(true);
        cacheConfiguration.setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));

        return cacheConfiguration;
    }

    @Bean
    public IgniteConfiguration igniteConfiguration() {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

        igniteConfiguration.setCacheConfiguration(cacheConfiguration());

        return igniteConfiguration;
    }

    @Bean
    public Ignite ignite() {
        IgniteConfiguration config = igniteConfiguration();
        return Ignition.start(config);
    }

    @Bean
    public IgniteCache<String, Product> productCache() {
        return ignite().cache(CACHE_NAME);
    }
}