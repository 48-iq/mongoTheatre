package ru.ivanov.theatremongo.dto;

import lombok.*;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TicketDto {
    private String id;
    private Integer row;
    private Integer place;
    private Integer price;
}
