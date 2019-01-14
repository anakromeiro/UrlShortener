package com.anakromeiro.urlShortener.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class ShortenerUrlService {

    private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Value("${config.base.url}")
    String BASE_URL;

    private static final int BASE = ALLOWED_CHARACTERS.length();

    /**
     * @param keyToUrl
     * @return
     */
    String generateShortUrl(long keyToUrl) {
        return convertBase10ToDefinedBase(keyToUrl, BASE);
    }

    /**
     * This method is responsible for converting a certain value in base 10 to a value in the predefined base.
     * The predefined base is the number of characters allowed in the shortened URL.
     * @param value The value to be converted.
     * @param toBase The base the value will be converted.
     * @return The converted value to informed base.
     */
    private String convertBase10ToDefinedBase(long value, int toBase) {
        StringBuilder convertedUrl = new StringBuilder();
        while (value > 0) {
            int remainder = (int) (value % toBase);
            convertedUrl.append(ALLOWED_CHARACTERS.charAt(remainder));
            value = value / toBase;
        }
        return convertedUrl.toString();
    }

    /**
     * This method is responsible for separating the shortened url, returning only the converted part, without the base
     * of shorten URL
     *
     * @param shortUrl The complete shortened URL.
     * @return The converted part of the URL.
     */
    String parseShortUrl(String shortUrl) {
        String[] splicedUrl = shortUrl.split("/");
        return splicedUrl[1];
    }

    /**
     * This method is responsible for validating the URL that will be shortened.
     *
     * @param url The URL that will be validated.
     * @return true if it is a valid URL, otherwise returns false.
     */
    boolean isUrlValid(String url) {
        return url.matches("(?i)\\b((?:[a-z][\\w-]+:(?:/{1,3}|[a-z0-9%])|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/?)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))*(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’])*)");
    }

    /**
     * This method is responsible for handling the URL. The method removes whitespace from the beginning and the end
     * and also converts everything to lowercase to store in the database.
     *
     * @param url The URL that will be processed.
     * @return The processed URL.
     */
    String processUrl(String url) {
        url = url.trim();
        url = url.toLowerCase();
        return url;
    }

    Date calculateInactivationDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -6);
        return calendar.getTime();
    }
}
