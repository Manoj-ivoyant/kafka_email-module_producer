//package com.ivoyant.kafkaemailevent.config;
//
//import com.solace.spring.boot.autoconfigure.SolaceJmsProperties;
//import com.solacesystems.jms.SolJmsUtility;
//import com.solacesystems.jms.SupportedProperty;
//import jakarta.jms.ConnectionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jms.connection.CachingConnectionFactory;
//import org.springframework.jms.core.JmsTemplate;
//
//import javax.naming.InitialContext;
//import java.util.Properties;
//
//@Configuration
//public class SolacePubConfig {
//
//    @Autowired
//    private SolaceJmsProperties solaceJmsProperties;
//
//    @Value("${solace.jms.defaultPubDestinationName}")
//    private String defaultPubDestinationName;
//
//
//    @Bean
//    public ConnectionFactory connectionFactory() throws Exception {
//        Properties env = new Properties();
//        env.put(InitialContext.INITIAL_CONTEXT_FACTORY, "com.solacesystems.jndi.SolJNDIInitialContextFactory");
//        env.put(InitialContext.PROVIDER_URL, solaceJmsProperties.getHost());
//        env.put(SupportedProperty.SOLACE_JMS_VPN, solaceJmsProperties.getMsgVpn());
//        env.put(InitialContext.SECURITY_PRINCIPAL, solaceJmsProperties.getClientUsername());
//        env.put(InitialContext.SECURITY_CREDENTIALS, solaceJmsProperties.getClientPassword());
//        return SolJmsUtility.createConnectionFactory(env);
//    }
//
//    @Bean
//    public CachingConnectionFactory cachingConnectionFactory(ConnectionFactory connectionFactory) {
//        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory);
//        cachingConnectionFactory.setSessionCacheSize(10);
//        return cachingConnectionFactory;
//    }
//
//    @Bean
//    public JmsTemplate pubJmsTemplate(CachingConnectionFactory cachingConnectionFactory) {
//        JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
//        jmsTemplate.setPubSubDomain(true);
//        jmsTemplate.setExplicitQosEnabled(true);
//        jmsTemplate.setDeliveryPersistent(true);
//        jmsTemplate.setDefaultDestinationName(defaultPubDestinationName);
//        return jmsTemplate;
//    }
//}
