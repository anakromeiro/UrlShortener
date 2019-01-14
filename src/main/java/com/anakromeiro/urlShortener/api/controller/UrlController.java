package com.anakromeiro.urlShortener.api.controller;

import com.anakromeiro.urlShortener.model.Url;
import com.anakromeiro.urlShortener.service.UrlService;
import com.anakromeiro.urlShortener.service.exception.DocumentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/url/")
public class UrlController {

    private UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @RequestMapping(value = "/shortUrl", method = RequestMethod.POST)
    public ResponseEntity requestShortUrl(@RequestBody String longUrl) {
        String shortenedUrl = urlService.getOrCreateShortUrl(longUrl);
        if (shortenedUrl != null) {
            return new ResponseEntity(shortenedUrl, HttpStatus.OK);
        } else {
            return new ResponseEntity("The server cannot shorten the URL.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @RequestMapping(value = "/originalUrl", method = RequestMethod.GET)
    public ResponseEntity getOriginalUrl(@RequestParam("shortUrl") String shortUrl) {
        try {
            String originalUrl = urlService.getOriginalUrl(shortUrl);
            return new ResponseEntity(originalUrl, HttpStatus.OK);
        } catch (DocumentNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/shortUrl/accesses", method = RequestMethod.GET)
    public ResponseEntity getNumberOfAccesses(@RequestParam("shortUrl") String shortUrl) {
        try {
            int numberOfAccesses = urlService.getNumberOfAccesses(shortUrl);
            return new ResponseEntity(numberOfAccesses, HttpStatus.OK);
        } catch (DocumentNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/originalUrl/requests", method = RequestMethod.GET)
    public ResponseEntity getNumberOfRequests(@RequestParam("originalUrl") String originalUrl) {
        try {
            int numberOfRequests = urlService.getNumberOfRequests(originalUrl);
            return new ResponseEntity(numberOfRequests, HttpStatus.OK);
        } catch (DocumentNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/shortUrl/inactive", method = RequestMethod.GET)
    public ResponseEntity getInactiveUrls() {

        List<Url> inactivesUrlList = new ArrayList<>();
        inactivesUrlList = urlService.getInactiveUrls();

        if (!Objects.isNull(inactivesUrlList) && !inactivesUrlList.isEmpty()) {
            return new ResponseEntity(inactivesUrlList, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

    }
}
