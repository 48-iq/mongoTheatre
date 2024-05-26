package ru.ivanov.theatremongo.service;

import lombok.RequiredArgsConstructor;
import ru.ivanov.theatremongo.dto.PerformanceDto;
import ru.ivanov.theatremongo.dto.TicketDto;
import ru.ivanov.theatremongo.model.Performance;
import ru.ivanov.theatremongo.model.Ticket;
import ru.ivanov.theatremongo.security.MongoTemplateProvider;

import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final MongoTemplateProvider mongoTemplateProvider;
    private final ModelMapper modelMapper;

    public TicketDto getTicketById(String ticketId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        BasicQuery query = new BasicQuery("{_id: ?0}", ticketId);
        Ticket ticket = mongoTemplate.findOne(query, Ticket.class);
        return modelMapper.map(ticket, TicketDto.class);
    }

    public List<TicketDto> getAllTickets() {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        return mongoTemplate.findAll(Ticket.class).stream()
                .map(ticket -> modelMapper.map(ticket, TicketDto.class))
                .toList();
    }

    public TicketDto saveTicket(TicketDto ticketDto) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Ticket ticket = modelMapper.map(ticketDto, Ticket.class);
        ticket = mongoTemplate.save(ticket);
        return modelMapper.map(ticket, TicketDto.class);
    }

    public void deleteTicket(String ticketId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        BasicQuery query = new BasicQuery("{_id: ?0}", ticketId);
        mongoTemplate.remove(query, Ticket.class);
    }
}
