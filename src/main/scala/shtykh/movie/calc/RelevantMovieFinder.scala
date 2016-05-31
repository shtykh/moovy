package shtykh.movie.calc

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.RoundRobinRouter
import shtykh.movie._

import scala.collection.mutable

/**
 * Created by shtykh on 13/05/16.
 */
class RelevantMovieFinder(amount: Int, graph: ActorRef, store: ActorRef, responder: ActorRef) extends Actor{
  val nrOfWorkers = 5 // todo
  implicit val ordering = new MovieRelevancyReversedOrdering
  val relevancies = new mutable.TreeSet[MovieRelevancy]
  val workerRouter = 
    context.actorOf(Props(new RelevancyCalculator(store)).withRouter(RoundRobinRouter(nrOfWorkers)), name = "workerRouter")
  var size = 0

  override def receive: Receive = {
    case GetRelevant(id, amount, responder) =>
      graph ! DistancesRequest(id, responder)
    case Distances(id, distances, responder) =>
      size = distances.size
      if (size == 0) responder ! 
        Set.empty 
      else distances.foreach{
        case (movieId, distance) => workerRouter ! CalculateRelevancy(movieId, distance, self)
      }
    case r : MovieRelevancy =>
      relevancies += r
      if (relevancies.size == size) responder ! relevancies.toList.slice(0, amount)
  }
}
