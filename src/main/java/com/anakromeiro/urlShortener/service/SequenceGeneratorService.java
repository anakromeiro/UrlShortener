package com.anakromeiro.urlShortener.service;

import com.anakromeiro.urlShortener.model.DBSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SequenceGeneratorService {

    private MongoOperations mongoOperations;

    @Autowired
    public SequenceGeneratorService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    /**
     * Generate the next number of informed sequence and update collection
     * @param sequenceName the sequence
     * @return the next number of the sequence
     */
    long generateSequence(String sequenceName){
        Query query = Query.query(Criteria.where("id").is(sequenceName));
        Update update = new Update().inc("sequence", 1);
        DBSequence counter = mongoOperations.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true).upsert(true), DBSequence.class);

        return !Objects.isNull(counter) ? counter.getSequence() : 1;
    }
}
