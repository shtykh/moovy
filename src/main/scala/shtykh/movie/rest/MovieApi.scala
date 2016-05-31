package shtykh.movie.rest

import javax.ws.rs.Path

import akka.actor.Props
import com.wordnik.swagger.annotations._
import shtykh.movie._
import shtykh.movie.calc.MessageRouter
import spray.routing.{HttpService, RequestContext}

@Api(value = "/movie", description = "Operations about movies.")
trait MovieApi extends HttpService {
  import Json4sSupport._
  
  val routes = readRoute ~ addRelationsRoute ~ addRoute ~ allRoute ~ relevantRoute
  val router = actorRefFactory.actorOf(Props[MessageRouter], name = "router")

  @ApiOperation(
    value = "Find a movie by ID",
    notes = "Returns a movie based on ID",
    httpMethod = "GET",
    response = classOf[MovieProfile])
  @ApiImplicitParams(Array(
  new ApiImplicitParam(
    name = "movieId",
    value = "ID of movie that needs to be fetched",
    required = true,
    dataType = "integer",
    paramType = "path")
  ))
  @ApiResponses(Array(
  new ApiResponse(code = 404, message = "Movie not found"),
  new ApiResponse(code = 400, message = "Invalid ID supplied")
  ))
  def readRoute =
      path("movie" / IntNumber) {
        id => {
          get { requestContext =>
            router ! Get(id, responder(requestContext))
          }
      }
  }

  @Path("/{movieId}/relations")
  @ApiOperation(
    value = "Updates a movie relations",
    nickname = "updateRelations",
    httpMethod = "POST",
    consumes = "application/json",
    response = classOf[String])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      name = "movieId",
      value = "ID of movie that needs to be updated",
      required = true,
      dataType = "string",
      paramType = "path"),
    new ApiImplicitParam(
      name = "relationList",
      value = "List of ids of related movies",
      required = false,
      dataType = "List[Int]",
      paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Dictionary does not exist.")
  ))
  def addRelationsRoute =
      {
      path("movie" / IntNumber / "relations") {
        movieId => entity(as[Set[Int]]) {
          list =>
        post { requestContext =>
            router ! AddRelations(movieId, list, responder(requestContext))
        }
      }
    }
  }

  @Path("/{movieId}/relevant")
  @ApiOperation(
    value = "N most relevant movies",
    notes = "",
    nickname = "relevant",
    httpMethod = "GET",
    produces = "application/json",
    response = classOf[List[Movie]])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      name = "movieId",
      value = "ID of movie",
      required = true,
      dataType = "string",
      paramType = "path"),
    new ApiImplicitParam(
      name = "amount",
      value = "Number of most relevant movies to be shown",
      required = true,
      dataType = "integer",
      paramType = "query")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Dictionary does not exist.")
  ))
  def relevantRoute =
    path("movie" / IntNumber / "relevant") {
      id => {
        parameters('amount.as[Int]) {
          amount =>
        get {requestContext =>
          router ! GetRelevant(id, amount, responder(requestContext))
        }
      }
    }
  }

  @ApiOperation(
    value = "Add a new movie", 
    nickname = "addMovie", 
    httpMethod = "POST", 
    consumes = "application/json")
  @ApiImplicitParams(Array(
  new ApiImplicitParam(
    name = "body", 
    value = "Movie object that needs to be added to the store", 
    dataType = "MovieInfo",
    required = true, 
    paramType = "body")
  ))
  @ApiResponses(Array(
  new ApiResponse(code = 405, message = "Invalid input")
  ))
  def addRoute =
        entity(as[MovieInfo]) {
          movie => {
        post { requestContext =>
            router ! Add(movie, responder(requestContext))
          }
        }
      }

  @ApiOperation(
    value = "All movies", 
    nickname = "getAll", 
    httpMethod = "GET", 
    produces = "application/json", 
    response = classOf[List[Movie]])
  def allRoute = get {
    path("movie") { requestContext =>
      router ! GetAll(responder(requestContext))
    }
  }

  private def responder(requestContext:RequestContext) = {
    actorRefFactory.actorOf(Props(new MovieResponder(requestContext)))
  }
}