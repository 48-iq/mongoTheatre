package ru.ivanov.theatremongo.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.ivanov.theatremongo.dto.TicketDto;
import ru.ivanov.theatremongo.model.Ticket;
import ru.ivanov.theatremongo.security.MongoTemplateProvider;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final MongoTemplateProvider mongoTemplateProvider;
    private final ModelMapper modelMapper;

    public TicketDto getTicketById(String ticketId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(ticketId));
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
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(ticketId));
        mongoTemplate.remove(query, Ticket.class);
    }
}
