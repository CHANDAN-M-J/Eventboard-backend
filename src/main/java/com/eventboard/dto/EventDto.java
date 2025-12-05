package com.eventboard.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventDto {

    private String title;
    private String description;
    private LocalDateTime dateTime;
    private String location;
    private String category;
}

