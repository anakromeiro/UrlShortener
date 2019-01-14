package com.anakromeiro.urlShortener.repository.impl;

import com.anakromeiro.urlShortener.model.Url;
import com.anakromeiro.urlShortener.repository.UrlRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class UrlRepositoryCustomImpl implements UrlRepositoryCustom {

    private MongoOperations mongoOperations;

    @Autowired
    public UrlRepositoryCustomImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void updateCounterById(String counter, long id) {
        Query query = Query.query(Criteria.where("id").is(id));
        Update update = new Update().inc(counter, 1);
        mongoOperations.updateFirst(query, update, Url.class);
    }

    @Override
    public void updateDateById(String dateField, long id) {
        Query query = Query.query(Criteria.where("id").is(id));
        Update update = new Update().currentDate(dateField);
        mongoOperations.updateFirst(query, update, Url.class);
    }
}
