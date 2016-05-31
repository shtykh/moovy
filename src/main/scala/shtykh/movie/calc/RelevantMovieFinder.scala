package shtykh.movie.calc

import akka.actor.{ActorRef, Actor}
import shtykh.movie._

/**
 * Created by shtykh on 13/05/16.
 */
class RelevantMovieFinder(id: Int, amount: Int, graph: ActorRef, store: ActorRef) extends Actor{
  override def receive: Receive = {
    case m @ GetRelevant(int, amount, responder) =>
      graph ! DistancesRequest(id, responder)
    case Distances(id, distances, responder) =>
      responder ! firstRelevant(distances)
  }
  def firstRelevant(tuples: List[(Int, Int)]): List[Movie] = ??? // todo
}
