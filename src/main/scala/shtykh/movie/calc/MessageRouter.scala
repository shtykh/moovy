package shtykh.movie.calc

import akka.actor.{ActorRef, Actor, ActorLogging, Props}
import shtykh.movie._

/**
 * Created by shtykh on 11/05/16.
 */
class MessageRouter extends Actor with ActorLogging {
  val graph = context.actorOf(Props[GraphController], name="graph")
  val store = context.actorOf(Props(new StoreController(graph)), name="store")

  override def receive: Receive = {
    case m @ Add(movie, responder) =>
      log.debug(s"adding movie $movie")
      store ! m
    case m @ GetProfile(id, responder) =>
      log.debug(s"getting movie by $id")
      store ! m
    case m @ GetAll(responder) =>
      log.debug(s"getting all the movies")
      store ! m
    case m @ AddRelations(id, relations, responder) =>
      log.debug(s"add relations")
      graph ! m
    case m @ GetRelevant(id, amount, responder) =>
      log.debug(s"getting relevant by $id")
      relevanceActor(amount, responder) ! m
    case m: BaseMovieMessage =>
      m.receiver ! "Go fuck yourself"
  }
  
  private def relevanceActor(amount: Int, responder: ActorRef) = {
    context.actorOf(Props(new RelevantMovieFinder(amount, graph, store, responder)))
  }
}
