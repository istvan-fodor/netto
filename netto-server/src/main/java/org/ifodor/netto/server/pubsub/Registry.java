package org.ifodor.netto.server.pubsub;

import java.util.HashMap;
import java.util.Map;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

public class Registry extends AbstractActor {

  private final Map<String, ActorRef> localPublishers;

  private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

  public Registry() {
    localPublishers = new HashMap<>();
  }

  @Override
  public Receive createReceive() {
    return ReceiveBuilder.create().match(ChannelObserverPair.class, this::register).build();
  }

  private void register(ChannelObserverPair pair) {
    if (!localPublishers.containsKey(pair.getChannel())) {
      log.debug("Creating publisher actor for channel {}", pair.getChannel());
      ActorRef channelChild = getContext().actorOf(Props.create(Publisher.class, pair.getChannel()),
          pair.base64ChannelName());
      localPublishers.put(pair.getChannel(), channelChild);
    }
    log.debug("Registering subscriber for channel {}", pair.getChannel());
    ActorRef publisher = localPublishers.get(pair.getChannel());
    publisher.tell(pair, self());
    sender().tell("OK", self());
  }

}
