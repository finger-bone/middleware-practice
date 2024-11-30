package com.github.fingerbone.logistics;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private String identifier;
    private TicketState ticketState;
    private List<TicketItem> ticketItems;
    public Ticket withNewState(TicketState newState) {
        return new Ticket(identifier, newState, ticketItems);
    }
}