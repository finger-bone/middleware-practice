package com.github.fingerbone.logistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketItem {
    private String itemId;
    private String itemName;
    private Integer itemCount;
    private Integer totalPrice;
    private String destination;
}
