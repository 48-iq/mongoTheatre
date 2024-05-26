package ru.ivanov.theatremongo.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private List<String> actors = new ArrayList<>();
}
