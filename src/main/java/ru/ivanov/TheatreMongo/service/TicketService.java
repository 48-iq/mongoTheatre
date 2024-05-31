package ru.ivanov.theatremongo.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.ivanov.theatremongo.dto.TicketDto;
import ru.ivanov.theatremongo.model.Performance;
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

    public void updateTicket(TicketDto ticketDto) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Ticket ticket = modelMapper.map(ticketDto, Ticket.class);
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(ticketDto.getId())),
                Update.update("price", ticket.getPrice())
                        .set("row", ticket.getRow())
                        .set("place", ticket.getPlace()),
                Ticket.class);
    }

    public List<TicketDto> getTicketsByPerformanceId(String performanceId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Performance performance = mongoTemplate.findById(performanceId, Performance.class);
        if (performance == null) 
            throw new IllegalArgumentException("such performance doesn't exist");
        if (performance.getTicketsList() == null)
            performance.setTicketsList(new ArrayList<>());
        else 
            performance.setTicketsList(new ArrayList<>(performance.getTicketsList()));
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(performance.getTicketsList()));
        List<Ticket> tickets = mongoTemplate.find(query, Ticket.class);
        if (tickets == null)
            return null;
        return tickets.stream()
            .map(mapper -> modelMapper.map(mapper, TicketDto.class))
            .toList();
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
