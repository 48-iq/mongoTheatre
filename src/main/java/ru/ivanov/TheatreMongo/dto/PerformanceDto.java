package ru.ivanov.TheatreMongo.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PerformanceDto {
    private String id;
    private String date;
    private String name;
    private String time;
    private String description;
    private List<String> ticketsList;
    private Map<String, String> actors;
}
