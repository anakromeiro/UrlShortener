package com.anakromeiro.urlShortener.repository;

public interface UrlRepositoryCustom {

    void updateCounterById(String counter, long id);
    void updateDateById(String dateField, long id);
}
