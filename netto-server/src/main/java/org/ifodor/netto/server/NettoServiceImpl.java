package org.ifodor.netto.server;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.ifodor.netto.api.NettoGrpc.NettoImplBase;
import org.ifodor.netto.api.NettoService.Empty;
import org.ifodor.netto.api.Protocol.Command;
import org.ifodor.netto.api.Protocol.Datum;
import org.ifodor.netto.api.Protocol.PublishEnvelope;
import org.ifodor.netto.api.Protocol.StreamMessage;
import org.ifodor.netto.api.Protocol.Subscribe;
import org.ifodor.netto.api.Protocol.Subscription;

import io.grpc.stub.StreamObserver;

public class NettoServiceImpl extends NettoImplBase {

  public Map<String, Queue<StreamObserver<Datum>>> subscriptions = null;

  public NettoServiceImpl() {
    subscriptions = new ConcurrentHashMap<>();
  }

  @Override
  public StreamObserver<Command> listen(StreamObserver<StreamMessage> responseObserver) {
    return new StreamObserver<Command>() {
      @Override
      public void onNext(Command command) {
        if (command.getCmdCase().equals(Command.CmdCase.SUBSCRIBE)) {
          Subscription subscription = subscribe(command.getSubscribe());
        }
      }

      @Override
      public void onError(Throwable t) {
        // TODO Auto-generated method stub
      }

      @Override
      public void onCompleted() {
        // TODO Auto-generated method stub
      }
    };
  }

  private Subscription subscribe(Subscribe subscribe) {
    // TODO Auto-generated method stub
    return null;

  }

  @Override
  public void publish(PublishEnvelope request, StreamObserver<Empty> responseObserver) {
    responseObserver.onNext(Empty.getDefaultInstance());
  }
}
