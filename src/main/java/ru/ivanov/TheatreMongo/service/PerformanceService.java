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
import ru.ivanov.theatremongo.dto.PerformanceDto;
import ru.ivanov.theatremongo.model.Performance;
import ru.ivanov.theatremongo.model.Ticket;
import ru.ivanov.theatremongo.security.MongoTemplateProvider;


@Service
@RequiredArgsConstructor
public class PerformanceService {
    private final MongoTemplateProvider mongoTemplateProvider;
    private final ModelMapper modelMapper;
    private final ActorService actorService;

    public PerformanceDto getPerformanceById(String actorId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Performance performance = mongoTemplate.findById(actorId, Performance.class);
        if (performance == null)
            throw new IllegalArgumentException("such performance doesn't exist");
        if (performance.getTicketsList() == null)
            performance.setTicketsList(new ArrayList<>());
        if (performance.getActors() == null)
            performance.setActors(new ArrayList<>());
        return modelMapper.map(performance, PerformanceDto.class);
    }

    public List<PerformanceDto> getAllPerformances() {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        return mongoTemplate.findAll(Performance.class).stream()
                .map(performance -> modelMapper.map(performance, PerformanceDto.class))
                .toList();
    }

    public PerformanceDto savePerformance(PerformanceDto performanceDto) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Performance performance = modelMapper.map(performanceDto, Performance.class);
        performance = mongoTemplate.save(performance);
        return modelMapper.map(performance, PerformanceDto.class);
    }

    public void updatePerformance(PerformanceDto performanceDto) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Performance performance = modelMapper.map(performanceDto, Performance.class);
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(performanceDto.getId())),
                Update.update("name", performance.getName())
                        .set("date", performance.getDate())
                        .set("ticketsList", performance.getTicketsList())
                        .set("actors", performance.getActors())
                        .set("time", performance.getTime()), Performance.class);
    }

    public void deletePerformance(String performanceId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(performanceId));
        Performance performance = mongoTemplate.findById(performanceId, Performance.class);
        if (performance == null)
            throw new IllegalArgumentException("such performance doesn't exist");
        if (performance.getTicketsList() != null){
            Query query2 = new Query();
            query2.addCriteria(Criteria.where("_id").in(performance.getTicketsList()));
            mongoTemplate.remove(query2, Ticket.class);
        }
        mongoTemplate.remove(query, Performance.class);
    }

    public void addActorToPerformance(String performanceId, String actorId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Performance performance = mongoTemplate.findById(performanceId, Performance.class);
        if (performance.getActors() == null)
            performance.setActors(new ArrayList<>());
        else 
            performance.setActors(new ArrayList<>(performance.getActors()));
        if (!performance.getActors().contains(actorId))
            performance.getActors().add(actorId);
        updatePerformance(modelMapper.map(performance, PerformanceDto.class));
    }

    public void addTicketToPerformance(String performanceId, String ticketId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Performance performance = mongoTemplate.findById(performanceId, Performance.class);
        if (performance.getTicketsList() == null)
            performance.setTicketsList(new ArrayList<>());
        
        if (!performance.getTicketsList().contains(ticketId))
            performance.getTicketsList().add(ticketId);
        updatePerformance(modelMapper.map(performance, PerformanceDto.class));
    }

    public void removeTicketFromPerformance(String performanceId, String ticketId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Performance performance = mongoTemplate.findById(performanceId, Performance.class);
        if (performance.getTicketsList().contains(ticketId))
            performance.getTicketsList().remove(ticketId);
        updatePerformance(modelMapper.map(performance, PerformanceDto.class));
    }

    public void removeActorFromPerformance(String performanceId, String actorId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Performance performance = mongoTemplate.findById(performanceId, Performance.class);
        if (performance.getActors() == null)
            performance.setActors(new ArrayList<>());
        else 
            performance.setActors(new ArrayList<>(performance.getActors()));
        if (performance.getActors().contains(actorId))
            performance.getActors().remove(actorId);
        updatePerformance(modelMapper.map(performance, PerformanceDto.class));
    }

    public List<PerformanceDto> getPerformancesByName(String name) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(name));
        return mongoTemplate.find(query, Performance.class).stream()
                .map(performance -> modelMapper.map(performance, PerformanceDto.class))
                .toList();
    }

    public Integer getIncome() {
        List<Ticket> tickets = mongoTemplateProvider.getMongoTemplate().findAll(Ticket.class);
        Integer income = 0;
        for (Ticket ticket : tickets) {
            income += ticket.getPrice();
        }
        return income;
    }

}
