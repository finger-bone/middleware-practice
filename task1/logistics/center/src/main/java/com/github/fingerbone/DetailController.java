package com.github.fingerbone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.fingerbone.logistics.Ticket;
import com.github.fingerbone.logistics.TicketItem;
import com.github.fingerbone.logistics.TicketState;
import com.github.fingerbone.message.Message;
import com.github.fingerbone.message.MessageQueueClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@Slf4j
public class DetailController {
    private final History history;
    private final MessageQueueClient messageQueueClient;
    
    public DetailController(@Autowired History history, @Autowired MessageQueueClient messageQueueClient) {
        this.history = history;
        this.messageQueueClient = messageQueueClient;
    }

    @GetMapping
    public Mono<String> detailPage() {
        return Mono.just("detail");
    }

    @ResponseBody
    @GetMapping("/info")
    public Mono<List<Ticket>> getHistory() {
        return Mono.just(history.getTickets());
    }

    @GetMapping("/capacity")
    @ResponseBody Mono<Integer> getRemainingCapacity() {
        return messageQueueClient.getRemainingCapacity();
    }
    
    @ResponseBody
    @PostMapping("/ticket")
    public Mono<Boolean> addTicket() {

        // Generate random ticket data
        String identifier = UUID.randomUUID().toString(); // Generate a unique identifier
        TicketState ticketState = TicketState.PENDING; // Randomly select a ticket state

        // Generate random ticket items
        List<TicketItem> ticketItems = Arrays.asList(
            generateRandomTicketItem(),
            generateRandomTicketItem()
        );
        // Create a new ticket
        Ticket ticket = new Ticket(identifier, ticketState, ticketItems);
        log.info("Generated ticket: " + ticket);
        
        return messageQueueClient.sendMessage(Message.fromObject(ticket, new ArrayList<>(){{
            add("logistics");
            add("warehouse");
        }}).get().toRecord());
    }

    private TicketItem generateRandomTicketItem() {
        // Generate random ticket item data
        String itemId = UUID.randomUUID().toString();
        String itemName = "Item-" + ThreadLocalRandom.current().nextInt(100, 1000); // Random item name
        Integer itemCount = ThreadLocalRandom.current().nextInt(1, 100); // Random item count
        Integer totalPrice = ThreadLocalRandom.current().nextInt(10, 500); // Random price
        String destination = "Destination-" + ThreadLocalRandom.current().nextInt(1, 10); // Random destination

        return new TicketItem(itemId, itemName, itemCount, totalPrice, destination);
    }
}
