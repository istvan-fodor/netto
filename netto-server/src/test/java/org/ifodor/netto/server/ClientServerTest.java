package org.ifodor.netto.server;

import java.util.Properties;

import org.ifodor.netto.client.NettoClient;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SocketUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = {PropertyPlaceholderOverride.class, Netto.class})
public class ClientServerTest {

  @Value("${server.port}")
  private int port = 1;
  
  @After
  public void stop() throws InterruptedException {
    Thread.sleep(1000l);
  }
  
  @Test
  public void testDummy() {

  }

  //@Test
  public void testUpdate() throws InterruptedException {
    log.info("Starting Client-Server test");
    NettoClient client = new NettoClient("localhost", port);
    
    client.subscribe("helloworld", bytes -> {
      log.info("received message: {}", new String(bytes));
    });
    client.publish("Hello World - 1".getBytes(), "helloworld");
    client.publish("Hello World - 2".getBytes(), "helloworld");
    client.publish("Hello Isti - 1".getBytes(), "helloisti");
    Thread.sleep(500l);
    
    client.subscribe("helloisti", bytes -> {
      log.info("received message: {}", new String(bytes));
    });
    client.publish("Hello Isti - 2".getBytes(), "helloisti");
  }
}

@Profile("test")
@Configuration
class PropertyPlaceholderOverride {

  @Bean
  @Order(value = Ordered.HIGHEST_PRECEDENCE)
  public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
    
    Properties props = new Properties();
    props.setProperty("server.port", String.valueOf(SocketUtils.findAvailableTcpPort()));
    
    PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
    configurer.setIgnoreResourceNotFound(true);
    configurer.setProperties(props);
    return configurer;
  }
}
