package ru.ivanov.TheatreMongo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "performances")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Performance {
    @Id
    private String id;
    private String date;
    private String name;
    private String time;
    private String description;
    private List<String> ticketsList;
    private List<String> actors;
}
