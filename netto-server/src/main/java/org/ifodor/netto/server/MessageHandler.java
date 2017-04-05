package org.ifodor.netto.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.ifodor.netto.api.Protocol.PublishEnvelope;
import org.ifodor.netto.api.Protocol.StreamMessage;
import org.ifodor.netto.api.Protocol.Subscribe;
import org.ifodor.netto.api.Protocol.Subscription;
import org.ifodor.netto.api.Protocol.Unsubscribe;

import io.grpc.stub.StreamObserver;

public class MessageHandler {

  private AtomicLong atomicLong = new AtomicLong();

  private static transient byte[] tokenKey;

  private final List<SubscriptionObserverPair> subscriptions;

  static {
    tokenKey = new byte[256];
    new SecureRandom().nextBytes(tokenKey);
  }

  public MessageHandler() {
    subscriptions = new CopyOnWriteArrayList<>();
  }

  public void subscribe(Subscribe subscribe, StreamObserver<StreamMessage> subscriber) {
    Subscription subscription = createSubscription(subscribe);
    subscriber.onNext(StreamMessage.newBuilder().setSubscription(subscription).build());
    subscriptions.add(SubscriptionObserverPair.builder().subscription(subscription).streamObserver(subscriber).build());
  }

  // TODO: reimplement in Akka + Cluster
  public void publish(PublishEnvelope envelope) {
    subscriptions.stream().filter(s -> envelope.getChannelsList().contains(s.getSubscription().getChannel()))
        .forEach(subscription -> envelope.getDataList().forEach(
            datum -> subscription.getStreamObserver().onNext(StreamMessage.newBuilder().setData(datum).build())));
  }

  public void unsubscribe(Unsubscribe unsubscribe) {
    // TODO unsubscribe
  }

  private Subscription createSubscription(Subscribe subscribe) {
    String key = generateKey();
    String signature = getSignature(key);
    return Subscription.newBuilder().setSubscriptionId(key).setToken(signature).setChannel(subscribe.getChannel())
        .build();
  }

  private String getSignature(String key) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    md.update(tokenKey);
    byte[] digest = md.digest(key.getBytes());
    return "v1." + Base64.getEncoder().encodeToString(digest);
  }

  private String generateKey() {
    return String.valueOf(atomicLong.incrementAndGet());
  }



}
