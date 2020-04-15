package com.example.fiveinarowserver.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Player {
    private int id;
    private String name;
    private Character color;
}
