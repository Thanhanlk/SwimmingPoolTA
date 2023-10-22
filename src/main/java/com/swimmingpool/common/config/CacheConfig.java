package com.swimmingpool.common.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableCaching
@ConfigurationProperties(prefix = "cache")
@ConditionalOnProperty(prefix = "cache", name = "enable")
@Setter
@RequiredArgsConstructor
public class CacheConfig {

    private String host;
    private int port;
    private String password;
    private List<CacheProperty> properties = new ArrayList<>();

    @Data
    public static class CacheProperty {
        private String name;
        private Integer ttl;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(this.host, this.port);
        configuration.setPassword(this.password);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofMillis(5))
                .computePrefixWith(cacheName -> "SWP:".concat(cacheName).concat(":"))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
        redisCacheConfiguration.usePrefix();
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = this.properties.stream()
                .collect(Collectors.toMap(p -> p.name, p -> RedisCacheConfiguration.defaultCacheConfig()
                        .disableCachingNullValues()
                        .entryTtl(Duration.ofMinutes(p.ttl))
                        .computePrefixWith(name -> "SWP:".concat(p.name).concat(":"))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()))));
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }
}
