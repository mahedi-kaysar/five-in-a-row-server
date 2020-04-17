package com.example.fiveinarowserver.repository.entity;

import com.example.fiveinarowserver.model.board.BoardDiscColor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Player {
    /**
     * 
     */
    private int id;
    
    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private BoardDiscColor color;
}
