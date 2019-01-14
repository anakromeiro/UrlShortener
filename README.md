# UrlShortener
API for short URL creation

#Docker
./mvnw install dockerfile:build

docker-compose up

#Endpoints
/api/v1/url/originalUrl - GET the original url given a shortened url.
/api/v1/url/originalUrl/requests - GET the number of times a url was requested to be shortened
/api/v1/url/shortUrl - POST given a URL, to verify that it is already in the database. If it is already in the database, the URL is returned, otherwise it generates a new shortened URL.
/api/v1/url/shortUrl/accesses - GET the number of times a shortened url has been accessed.
/api/v1/url/shortUrl/inactive - GET alist of urls that have not been accessed in the last 6 months

#Swagger
localhost:8080/swagger-ui.html
