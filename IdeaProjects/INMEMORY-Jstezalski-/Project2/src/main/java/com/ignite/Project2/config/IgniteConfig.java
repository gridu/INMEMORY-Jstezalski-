package com.ignite.Project2.config;

import com.ignite.Project2.model.Product;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class IgniteConfig {

    public static final String CACHE_NAME = "Products";

    @Bean
    public Ignite ignite() {
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(Arrays.asList("ignite-1", "ignite-2", "ignite-3"));

        TcpDiscoverySpi discoSpi = new TcpDiscoverySpi();
        discoSpi.setIpFinder(ipFinder);

        CacheConfiguration<String, Product> cacheConfiguration = new CacheConfiguration<>(CACHE_NAME);

        IgniteConfiguration configuration = new IgniteConfiguration();
        configuration.setDiscoverySpi(discoSpi);
        configuration.setClientMode(true);
        configuration.setPeerClassLoadingEnabled(true);
        configuration.setCacheConfiguration(cacheConfiguration);

        return Ignition.start(configuration);
    }

}
