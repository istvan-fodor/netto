package org.ifodor.netto.server.pubsub;

import java.util.HashMap;
import java.util.Map;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import akka.japi.pf.ReceiveBuilder;
import scala.concurrent.duration.Duration;

public class Registry extends AbstractActor {

  public static final String OK = "OK";

  private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

  private final Map<String, ActorRef> localPublishers;

  private boolean acceptingNewSubscribes;

  private ActorRef shutdownAllCaller;

  private static SupervisorStrategy strategy =
      new OneForOneStrategy(-1, Duration.Inf(), DeciderBuilder.matchAny(o -> SupervisorStrategy.resume()).build());

  public Registry() {
    localPublishers = new HashMap<>();
    acceptingNewSubscribes = true;
  }

  @Override
  public Receive createReceive() {
    return ReceiveBuilder.create().match(ChannelObserverPair.class, this::register)
        .match(FinnishedProtocol.CompleteAll.class, this::completeChildren)
        .match(FinnishedProtocol.CompletedAll.class, this::childCompletedAll).build();
  }

  protected void register(ChannelObserverPair pair) {
    if (acceptingNewSubscribes) {
      if (!localPublishers.containsKey(pair.getChannel())) {
        createNewSubscriber(pair);
      }
      sendSubscribeRequest(pair);
      sender().tell(OK, self());
    } else {
      sender().tell("Not accepting new subscriptions!", self());
    }
  }

  protected void sendSubscribeRequest(ChannelObserverPair pair) {
    log.debug("Registering subscriber for channel {}", pair.getChannel());
    ActorRef publisher = localPublishers.get(pair.getChannel());
    publisher.tell(pair, self());
  }

  protected void createNewSubscriber(ChannelObserverPair pair) {
    log.debug("Creating publisher actor for channel {}", pair.getChannel());
    ActorRef channelChild =
        getContext().actorOf(Props.create(Publisher.class, pair.getChannel()), pair.base64ChannelName());
    localPublishers.put(pair.getChannel(), channelChild);
  }

  protected void completeChildren(FinnishedProtocol.CompleteAll msg) {
    log.info("Sending CompleteAll to all children");
    acceptingNewSubscribes = false;
    shutdownAllCaller = sender();
    localPublishers.values().forEach(publisher -> publisher.tell(msg, self()));
  }

  protected void childCompletedAll(FinnishedProtocol.CompletedAll msg) {
    log.info("Channel {} closed all observers.", msg.getChannel());
    localPublishers.remove(msg.getChannel());
    sender().tell(PoisonPill.getInstance(), self());
    if (localPublishers.isEmpty()) {
      shutdownAllCaller.tell(FinnishedProtocol.CompletedAll.builder().channel("ALL").build(), self());
    }
  }

  @Override
  public SupervisorStrategy supervisorStrategy() {
    return strategy;
  }

}
