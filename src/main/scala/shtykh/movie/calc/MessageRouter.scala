package shtykh.movie.calc

import akka.actor.{Actor, ActorLogging, Props}
import shtykh.movie._

/**
 * Created by shtykh on 11/05/16.
 */
class MessageRouter extends Actor with ActorLogging {
  val graph = context.actorOf(Props[GraphController], name="graph")
  val store = context.actorOf(Props[StoreController], name="store")

  override def receive: Receive = {
    case m @ Add(movie, responder) =>
      log.debug(s"adding movie $movie")
      store ! m
    case m @ Get(id, responder) =>
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
      relevanceActor(id, amount) ! m
    case m @ NeighborRequest(movie, responder) =>
      log.debug(s"getting neighbors for $movie")
      graph ! m
    case m: MovieMessage =>
      m.responder ! "Go fuck yourself"
  }
  
  private def relevanceActor(id: Int, amount: Int) = {
    context.actorOf(Props(new RelevantMovieFinder(id, amount, graph, store)))
  }
}
