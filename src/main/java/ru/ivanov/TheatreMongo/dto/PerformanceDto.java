package ru.ivanov.theatremongo.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
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
    private List<String> ticketsList = new ArrayList<>();
    private Map<String, String> actors = new HashMap<>();
}
