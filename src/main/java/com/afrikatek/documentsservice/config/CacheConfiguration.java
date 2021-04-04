package com.afrikatek.documentsservice.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.afrikatek.documentsservice.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.afrikatek.documentsservice.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.afrikatek.documentsservice.domain.User.class.getName());
            createCache(cm, com.afrikatek.documentsservice.domain.Authority.class.getName());
            createCache(cm, com.afrikatek.documentsservice.domain.User.class.getName() + ".authorities");
            createCache(cm, com.afrikatek.documentsservice.domain.Address.class.getName());
            createCache(cm, com.afrikatek.documentsservice.domain.Applicant.class.getName());
            createCache(cm, com.afrikatek.documentsservice.domain.Applicant.class.getName() + ".addresses");
            createCache(cm, com.afrikatek.documentsservice.domain.Applicant.class.getName() + ".countryOfBirths");
            createCache(cm, com.afrikatek.documentsservice.domain.DemographicDetails.class.getName());
            createCache(cm, com.afrikatek.documentsservice.domain.MarriageDetails.class.getName());
            createCache(cm, com.afrikatek.documentsservice.domain.NextOfKeen.class.getName());
            createCache(cm, com.afrikatek.documentsservice.domain.Guardian.class.getName());
            createCache(cm, com.afrikatek.documentsservice.domain.Passport.class.getName());
            createCache(cm, com.afrikatek.documentsservice.domain.Declaration.class.getName());
            createCache(cm, com.afrikatek.documentsservice.domain.Appointment.class.getName());
            createCache(cm, com.afrikatek.documentsservice.domain.Appointment.class.getName() + ".appointmentSlots");
            createCache(cm, com.afrikatek.documentsservice.domain.AppointmentSettings.class.getName());
            createCache(cm, com.afrikatek.documentsservice.domain.AppointmentSlot.class.getName());
            createCache(cm, com.afrikatek.documentsservice.domain.AppointmentSlot.class.getName() + ".applicants");
            createCache(cm, com.afrikatek.documentsservice.domain.Country.class.getName());
            createCache(cm, com.afrikatek.documentsservice.domain.Holiday.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
