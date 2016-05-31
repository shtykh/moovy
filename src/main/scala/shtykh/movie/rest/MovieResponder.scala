package shtykh.movie.rest

import akka.actor.{ActorLogging, Actor, PoisonPill}
import shtykh.movie._
import spray.http.StatusCodes
import spray.routing.RequestContext

/**
 * Created by shtykh on 11/05/16.
 */
class MovieResponder(requestContext:RequestContext) extends Actor with ActorLogging {
  import Json4sSupport._

  def receive = {
    case movieProfile : MovieProfile =>
      requestContext.complete(movieProfile)
      suicide()
    case MovieAdded(id) =>
      requestContext.complete(id.toString)
      suicide()
    case s: String =>
      requestContext.complete(s)
    case MovieNotFound =>
      requestContext.complete(StatusCodes.NotFound)
      suicide()
    case movies: List[Movie] =>
      requestContext.complete(movies)
    case e: Throwable =>
      requestContext.complete(StatusCodes.ServerError)
    case _ =>
      requestContext.complete(StatusCodes.NotImplemented)
  }

  private def suicide() = self ! PoisonPill

}
