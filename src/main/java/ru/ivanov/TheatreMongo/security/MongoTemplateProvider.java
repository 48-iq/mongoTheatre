package ru.ivanov.theatremongo.security;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongoTemplateProvider {
    public MongoTemplate getMongoTemplate() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userDetails.getMongoTemplate();
    }
}
