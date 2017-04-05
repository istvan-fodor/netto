package org.ifodor.netto.server;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import lombok.extern.slf4j.Slf4j;
import scala.concurrent.Await;
import scala.concurrent.duration.FiniteDuration;

@Slf4j
@Configuration
@SpringBootApplication
public class Netto {

  public static final String REGISTRY_NAME = "registry";

  public static final Object monitor = new Object();

  public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
    ConfigurableApplicationContext run = SpringApplication.run(Netto.class, args);
    run.registerShutdownHook();
    log.info("{} Server Started!", Netto.class.getSimpleName());
    synchronized (monitor) {
      monitor.wait();
    }
    shutdown(run);
  }

  private static void shutdown(ConfigurableApplicationContext run) throws InterruptedException, TimeoutException {
    ActorSystem actorSystem = run.getBean(ActorSystem.class);
    actorSystem.actorSelection("/*").tell(PoisonPill.getInstance(), null);
    run.getBean(NettoServer.class).stop();
    Await.ready(actorSystem.terminate(), FiniteDuration.create(1, TimeUnit.MINUTES));
    run.stop();
  }

  @Bean
  public ApplicationRunner runner() {
    return args -> {
    };
  }

  @Bean
  public ActorSystem actorSystem() {
    return ActorSystem.create("netto");
  }

  @Bean
  @Qualifier(REGISTRY_NAME)
  public ActorRef registry(ActorSystem system) {
    return system.actorOf(Props.create(Registry.class), REGISTRY_NAME);
  }
}
