package org.ifodor.netto.server;

import java.util.Base64;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.ifodor.netto.api.NettoGrpc.NettoImplBase;
import org.ifodor.netto.api.NettoService.Empty;
import org.ifodor.netto.api.Protocol.Command;
import org.ifodor.netto.api.Protocol.PublishEnvelope;
import org.ifodor.netto.api.Protocol.StreamMessage;
import org.ifodor.netto.api.Protocol.Subscription;
import org.ifodor.netto.server.pubsub.ChannelObserverPair;
import org.ifodor.netto.server.pubsub.FinnishedProtocol.CompleteAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.PoisonPill;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import scala.concurrent.duration.FiniteDuration;

@Slf4j
@Component
public class NettoServiceImpl extends NettoImplBase {

  private ActorSystem system;

  private ActorRef registry;

  @Autowired
  public NettoServiceImpl(ActorSystem system, @Qualifier("registry") ActorRef registry) {
    this.system = system;
    this.registry = registry;
  }

  @Override
  public StreamObserver<Command> listen(StreamObserver<StreamMessage> responseObserver) {
    return new StreamObserver<Command>() {

      @Override
      public void onNext(Command command) {
        if (command.getCmdCase().equals(Command.CmdCase.SUBSCRIBE)) {
          log.info("Subscribe");
          Inbox reply = Inbox.create(system);
          reply.send(registry, ChannelObserverPair.builder().channel(command.getSubscribe().getChannel())
              .streamObserver(responseObserver).build());
          try {
            String ok = (String) reply.receive(FiniteDuration.create(1l, TimeUnit.MINUTES));
            if (ok.equals("OK")) {
              responseObserver
                  .onNext(StreamMessage.newBuilder().setSubscription(Subscription.getDefaultInstance()).build());
            } else {
              responseObserver.onCompleted();
            }
          } catch (TimeoutException e) {
            throw new RuntimeException(e);
          }
        } else if (command.getCmdCase().equals(Command.CmdCase.UNSUBSCRIBE)) {

        }
      }

      @Override
      public void onError(Throwable t) {

      }

      @Override
      public void onCompleted() {
        responseObserver.onCompleted();
        log.info("Completed!");
      }
    };
  }

  @Override
  public void publish(PublishEnvelope request, StreamObserver<Empty> responseObserver) {
    log.info("Publish");
    responseObserver.onNext(Empty.getDefaultInstance());
    responseObserver.onCompleted();
    publishDatum(request);
  }

  protected void publishDatum(PublishEnvelope request) {
    for (String channel : request.getChannelsList()) {
      ActorSelection actorSelection = system
          .actorSelection(String.format("/user/registry/%s", Base64.getEncoder().encodeToString(channel.getBytes())));
      request.getDataList().forEach(datum -> actorSelection.tell(datum, null));
    }
  }

  public void closeAll() {
    Inbox reply = Inbox.create(system);
    reply.send(registry, CompleteAll.getInstance());
    try {
      reply.receive(FiniteDuration.apply(5l, TimeUnit.MINUTES));
    } catch (TimeoutException e) {
      log.warn("Received timeout from Registry actor. Forcing shutdown anyway.");
    } finally {
      registry.tell(PoisonPill.getInstance(), null);
    }
  }


}
