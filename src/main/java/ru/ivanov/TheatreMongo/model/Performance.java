package ru.ivanov.theatremongo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "performances")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Performance {
    @Id
    @Indexed
    private String id;
    private String date;
    @Indexed
    private String name;
    private String time;
    private String description;
    private List<String> ticketsList = new ArrayList<>();
    private List<String> actors = new ArrayList<>();
}
