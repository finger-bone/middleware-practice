package com.github.fingerbone;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.fingerbone.logistics.Ticket;

import reactor.core.publisher.Mono;

@Controller
public class DetailController {
    private final History history;
    
    public DetailController(@Autowired History history) {
        this.history = history;
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
    
}
