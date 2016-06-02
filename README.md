# Moovy
## Overview
The application allows user to store, update and retrieve information about movies, and relations between them via RestAPI or via UI.

None of these technologies weren't harmed during development: Scala, Akka, Spray, SBT, Swagger.
## API description
Name | Comment | Method | Consumes | Produces
-----| ------ |  -------|----------|---------
/movie/{movieId} | Get a Movie(id, name, rating and related ids) by its id |  GET | movieId(Int) | MovieProfile(id, name, rating, relations)
/movie | Get all the Movies(id, name, rating) in a List |  GET | - | List[Movie]
/movie/{movieId}/relations | Make a movie with specified id related to all of movies of ids in list (and vice versa) |  POST | movieId(Int), relatedMovieIds(Set[Int]) | Message if update gone well
/movie | Add a new movie and get its id |  POST | MovieInfo(name, rating, relations) | id(String)
/movie/{movieId}/relevant | Get not more than *numberOfFilms* of the most relevant to *movieId* Movies(id, name, rating, relevance) |  GET | movieId(Int), numberOfFilms(Int) | List[Movie]
## How to install
to be written
## Actor system
It might be not so useful but here is an idea of how all of API methods work work.
![Routes](https://raw.githubusercontent.com/shtykh/moovy/master/Routes.png)
