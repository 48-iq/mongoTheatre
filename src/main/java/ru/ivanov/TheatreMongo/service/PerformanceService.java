package ru.ivanov.TheatreMongo.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Service;
import ru.ivanov.TheatreMongo.dto.PerformanceDto;
import ru.ivanov.TheatreMongo.model.Actor;
import ru.ivanov.TheatreMongo.model.Performance;
import ru.ivanov.TheatreMongo.security.MongoTemplateProvider;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PerformanceService {
    private final MongoTemplateProvider mongoTemplateProvider;
    private final ModelMapper modelMapper;

    public PerformanceDto getPerformanceById(String actorId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        BasicQuery query = new BasicQuery("{_id: ?0}", actorId);
        Performance performance = mongoTemplate.findOne(query, Performance.class);
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
        Performance performance= modelMapper.map(performanceDto, Performance.class);
        performance = mongoTemplate.save(performance);
        return modelMapper.map(performance, PerformanceDto.class);
    }

    public void deletePerformance(String performanceId) {
        MongoTemplate mongoTemplate = mongoTemplateProvider.getMongoTemplate();
        BasicQuery query = new BasicQuery("{_id: ?0}", performanceId);
        mongoTemplate.remove(query, Performance.class);
    }
}
