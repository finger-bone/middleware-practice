package com.github.fingerbone;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.fingerbone.logistics.Ticket;
import com.github.fingerbone.logistics.TicketState;
import com.github.fingerbone.message.Message;
import com.github.fingerbone.message.MessageQueueClient;
import com.github.fingerbone.message.MessageQueueListener;
import com.github.fingerbone.message.MessageRecord;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class WarehouseListener extends MessageQueueListener {
    private final History history;
    private final MessageQueueClient mqClient;

    public WarehouseListener(@Autowired History history, @Autowired MessageQueueClient mqClient) {
        super("warehouse", "http://warehouse-service/receive-message", new ArrayList<>() {{
            add("logistics");
            add("warehouse");
        }});
        this.history = history;
        this.mqClient = mqClient;
        this.mqClient.register(this).subscribe(success -> {
            if(!success) {
                throw new RuntimeException("Failed to register listener");
            }
        });
    }

    @Override
    public Mono<Void> onReceive(MessageRecord message) {
        Optional<Ticket> maybeTicket = Message.fromRecord(message).getPayload(Ticket.class);
        if(maybeTicket.isEmpty()) {
            return Mono.empty();
        }
        Ticket ticket = maybeTicket.get();
        history.add(ticket);
        Ticket newTicket = ticket.withNewState(TicketState.DELIVERING);
        return mqClient.sendMessage(
            Message.fromObject(newTicket, new ArrayList<>() {{
                add("logistics");
                add("delivery");
            }}).get().toRecord()
        ).then();
    }
}
