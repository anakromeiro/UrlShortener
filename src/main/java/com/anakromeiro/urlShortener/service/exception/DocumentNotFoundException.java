package com.anakromeiro.urlShortener.service.exception;

public class DocumentNotFoundException extends Exception {

    public DocumentNotFoundException(String erroMessage){
        super(erroMessage);
    }
}
