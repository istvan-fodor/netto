package org.ifodor.netto.server;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.ifodor.netto.api.NettoGrpc.NettoImplBase;
import org.ifodor.netto.api.NettoService.Empty;
import org.ifodor.netto.api.Protocol.Command;
import org.ifodor.netto.api.Protocol.PublishEnvelope;
import org.ifodor.netto.api.Protocol.StreamMessage;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettoServiceImpl extends NettoImplBase {

  private final MessageHandler subscriptionHandler;

  public NettoServiceImpl() {
    subscriptionHandler = new MessageHandler();
    
    
  }

  @Override
  public StreamObserver<Command> listen(StreamObserver<StreamMessage> responseObserver) {
    return new StreamObserver<Command>() {
      @Override
      public void onNext(Command command) {
        if (command.getCmdCase().equals(Command.CmdCase.SUBSCRIBE)) {
          log.info("Subscribe");
          subscriptionHandler.subscribe(command.getSubscribe(), responseObserver);
        } else if (command.getCmdCase().equals(Command.CmdCase.UNSUBSCRIBE)) {
          // TODO: implement unsubscribe
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

  @Override
  public void publish(PublishEnvelope request, StreamObserver<Empty> responseObserver) {
    log.info("Publish");
    responseObserver.onNext(Empty.getDefaultInstance());
    subscriptionHandler.publish(request);
  }
}
