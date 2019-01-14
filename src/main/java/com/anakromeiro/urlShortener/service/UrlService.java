package com.anakromeiro.urlShortener.service;

import com.anakromeiro.urlShortener.model.Url;
import com.anakromeiro.urlShortener.repository.UrlRepository;
import com.anakromeiro.urlShortener.service.exception.DocumentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UrlService {

    private SequenceGeneratorService sequenceGeneratorService;

    private ShortenerUrlService shortenerUrlService;

    private UrlRepository repository;

    @Autowired
    public UrlService(SequenceGeneratorService sequenceGeneratorService, ShortenerUrlService shortenerUrlService, UrlRepository repository) {
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.shortenerUrlService = shortenerUrlService;
        this.repository = repository;
    }

    /**
     * This method is responsible for, given a URL, to verify that it is already in the database.
     * If it is already in the database, the URL is returned, otherwise it generates a new shortened URL.
     * @param longUrl The URL that will be shortened
     * @return The shortened URL
     */
    public String getOrCreateShortUrl(String longUrl) {

        if (shortenerUrlService.isUrlValid(longUrl)) {
            longUrl = shortenerUrlService.processUrl(longUrl);

            StringBuilder stringBuilder;
            stringBuilder = new StringBuilder();
            stringBuilder.append(shortenerUrlService.BASE_URL);
            stringBuilder.append(saveNewUrl(longUrl).getShortUrl());
            return stringBuilder.toString();
        }

        return null;
    }

    /**
     * This method is responsible for getting the original url given a shortened url.
     * @param shortUrl The shortened url.
     * @return The original url.
     * @throws DocumentNotFoundException A exception case the document not be found in database
     */
    public String getOriginalUrl(String shortUrl) throws DocumentNotFoundException{

        Url url = findByShortUrl(shortenerUrlService.parseShortUrl(shortUrl));

        if (url != null) {
            repository.updateCounterById("numberOfAccesses", url.getId());
            repository.updateDateById("lastAccess", url.getId());
            return url.getOriginalUrl();
        }
        throw new DocumentNotFoundException("This URL does not exists.");
    }

    /**
     * Gets the number of times a shortened url has been accessed.
     * @param shortUrl The url to be consulted
     * @return the number of times the url has been accessed.
     * @throws DocumentNotFoundException A exception case the document not be found in database
     */
    public int getNumberOfAccesses(String shortUrl) throws DocumentNotFoundException{
        Url url = repository.findByShortUrl(shortenerUrlService.parseShortUrl(shortUrl));
        if (url == null) {
            throw new DocumentNotFoundException("This URL does not exists.");
        }
        return url.getNumberOfAccesses();
    }

    /**
     * Gets the number of times a url was requested to be shortened
     * @param originalUrl The url to be consulted
     * @return the number of times the url has been requested.
     * @throws DocumentNotFoundException A exception case the document not be found in database
     */
    public int getNumberOfRequests(String originalUrl) throws DocumentNotFoundException{
        Url url = repository.findByOriginalUrl(originalUrl);
        if (url == null) {
            throw new DocumentNotFoundException("This URL does not exists.");
        }
        return url.getNumberOfRequests();
    }

    /**
     * Gets a list of urls that have not been accessed in the last X months
     * @return a list of urls considered inactives
     */
    public List getInactiveUrls(){
        Date inactiveDate = shortenerUrlService.calculateInactivationDate();
        return repository.findAllByLastAccessBeforeOrLastAccessIsNullAndCreatedDateBefore(inactiveDate, inactiveDate);
    }

    private Url saveNewUrl(String longUrl) {
        Url existentShortUrl = findByOriginalUrl(longUrl);

        if (existentShortUrl != null) {
            repository.updateCounterById("numberOfRequests", existentShortUrl.getId());
            return existentShortUrl;
        }

        long uniqueId = sequenceGeneratorService.generateSequence(Url.SEQUENCE);
        String shortUrl = shortenerUrlService.generateShortUrl(uniqueId);

        Url url = new Url();

        url.setId(uniqueId);
        url.setCreatedDate(new Date());
        url.setNumberOfAccesses(0);
        url.setOriginalUrl(longUrl);
        url.setShortUrl(shortUrl);

        url = repository.save(url);
        return url;
    }

    private Url findByShortUrl(String shortUrl) {
        return repository.findByShortUrl(shortUrl);
    }

    private Url findByOriginalUrl(String originalUrl) {
        return repository.findByOriginalUrl(originalUrl);
    }

}
