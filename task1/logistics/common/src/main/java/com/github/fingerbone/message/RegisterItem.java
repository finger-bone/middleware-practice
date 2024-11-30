package com.github.fingerbone.message;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterItem {
    private String identifier;
    private String callback;
    private List<String> topics;
}