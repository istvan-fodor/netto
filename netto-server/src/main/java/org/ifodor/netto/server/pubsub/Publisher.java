package org.ifodor.netto.server.pubsub;

import java.util.ArrayList;
import java.util.List;

import org.ifodor.netto.api.Protocol.Datum;
import org.ifodor.netto.api.Protocol.StreamMessage;
import org.springframework.util.Assert;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import io.grpc.stub.StreamObserver;

public class Publisher extends AbstractActor {

  private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

  private final String channel;

  private final List<StreamObserver<StreamMessage>> observers;

  public Publisher(String channel) {
    this.channel = channel;
    this.observers = new ArrayList<>();
  }

  @Override
  public Receive createReceive() {
    return ReceiveBuilder.create().match(ChannelObserverPair.class, this::register)
        .match(Datum.class, this::publish).build();
  }

  protected void register(ChannelObserverPair pair) {
    log.debug("Registering subscriber to channel {}", pair.getChannel());
    if (!pair.getChannel().equals(channel)) {
      log.error("Tried to subscribe to channel {} in {} channel's actor! Dropping message!", pair.getChannel(),
          channel);
    } else {
      observers.add(pair.getStreamObserver());
    }
  }

  protected void publish(Datum pair) {
    StreamMessage msg = StreamMessage.newBuilder().setData(pair).build();
    observers.forEach(observer -> observer.onNext(msg));
  }
}
