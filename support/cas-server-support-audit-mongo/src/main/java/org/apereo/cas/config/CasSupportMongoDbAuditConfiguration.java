package org.apereo.cas.config;

import lombok.val;

import lombok.extern.slf4j.Slf4j;
import org.apereo.cas.audit.AuditTrailExecutionPlanConfigurer;
import org.apereo.cas.audit.MongoDbAuditTrailManager;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.mongo.MongoDbConnectionFactory;
import org.apereo.inspektr.audit.AuditTrailManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This is {@link CasSupportMongoDbAuditConfiguration}.
 *
 * @author Misagh Moayyed
 * @since 5.2.0
 */
@Configuration("casSupportMongoDbAuditConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
@Slf4j
public class CasSupportMongoDbAuditConfiguration {

    @Autowired
    private CasConfigurationProperties casProperties;

    @Bean
    public AuditTrailManager mongoDbAuditTrailManager() {
        val mongo = casProperties.getAudit().getMongo();
        val factory = new MongoDbConnectionFactory();
        val mongoTemplate = factory.buildMongoTemplate(mongo);
        factory.createCollection(mongoTemplate, mongo.getCollection(), mongo.isDropCollection());
        val mgmr = new MongoDbAuditTrailManager(mongoTemplate, mongo.getCollection());
        mgmr.setAsynchronous(mongo.isAsynchronous());
        return mgmr;
    }

    @Bean
    public AuditTrailExecutionPlanConfigurer mongoDbAuditTrailExecutionPlanConfigurer() {
        return plan -> plan.registerAuditTrailManager(mongoDbAuditTrailManager());
    }
}
