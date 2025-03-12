package com.ngolik.authservice.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DataSourceLogger {
  private static final Logger logger = LoggerFactory.getLogger(DataSourceLogger.class);

  @Value("${spring.datasource.url}")
  private String datasourceUrl;

  @Value("${spring.datasource.username}")
  private String datasourceUsername;

  @Value("${spring.datasource.password}")
  private String datasourcePassword;

  @PostConstruct
  public void logDatasourceDetails() {
    logger.info("Datasource URL: {}", datasourceUrl);
    logger.info("Datasource Username: {}", datasourceUsername);
    logger.info("Datasource Password: {}", datasourcePassword);
  }
}

