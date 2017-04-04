package org.ifodor.netto.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicLong;

import org.ifodor.netto.api.Protocol.StreamMessage;
import org.ifodor.netto.api.Protocol.Subscribe;
import org.ifodor.netto.api.Protocol.Subscription;

import io.grpc.stub.StreamObserver;

public class SubscriptionHandler {

  private AtomicLong atomicLong = new AtomicLong();

  private static transient byte[] tokenKey;
  
  static {
    tokenKey = new byte[256];
    new SecureRandom().nextBytes(tokenKey);
  }

  public Subscription subscribe(Subscribe subscribe, StreamObserver<StreamMessage> sink) {
    Subscription ret = createSubscription();
    return ret;
  }

  private Subscription createSubscription() {
    String key = generateKey();
    String signature = getSignature(key);
    return Subscription.newBuilder().setSubscriptionId(key).setToken(signature).build();
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
    return Base64.getEncoder().encodeToString(digest);
  }

  private String generateKey() {
    return String.valueOf(atomicLong.incrementAndGet());
  }

}
