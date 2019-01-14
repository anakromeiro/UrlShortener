package com.anakromeiro.urlShortener.repository;

import com.anakromeiro.urlShortener.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface UrlRepository extends MongoRepository<Url, String>, UrlRepositoryCustom {

    Url findByShortUrl(String shorUrl);
    Url findByOriginalUrl(String originalUrl);
    List<Url> findAllByLastAccessBeforeOrLastAccessIsNullAndCreatedDateBefore(Date date, Date dateCreation);
}
