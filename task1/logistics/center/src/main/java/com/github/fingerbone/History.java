package com.github.fingerbone;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.github.fingerbone.logistics.Ticket;

@Component
public class History {
    private final List<Ticket> tickets = new ArrayList<>();

    public void add(Ticket ticket) {
        tickets.add(ticket);
    }

    public List<Ticket> getTickets() {
        return new ArrayList<>(tickets);
    }
}
