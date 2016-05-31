package shtykh.movie

import akka.actor.ActorRef

sealed trait MovieMessage {
  val responder: ActorRef
}

case class GetAll(responder: ActorRef) extends MovieMessage
case class Get(id: Int, responder: ActorRef) extends MovieMessage
case class Add(movie: MovieInfo, responder: ActorRef) extends MovieMessage
case class AddRelations(id: Int, relations: Set[Int], responder: ActorRef) extends MovieMessage
case class GetRelevant(id: Int, amount: Int, responder: ActorRef) extends MovieMessage

case class MovieNotFound(id: Int, responder: ActorRef) extends MovieMessage

case class Distances(centerId: Int, distances: List[(Int, Int)], responder: ActorRef) extends MovieMessage
case class DistancesRequest(id: Int, responder: ActorRef) extends MovieMessage
case class NeighborRequest(movie: Movie, responder: ActorRef) extends MovieMessage
case class CalculateRelevancy(rating: Double, radius: Int, responder: ActorRef) extends MovieMessage

case class NotImplementedMessage(actor: String, message: Any)

case class MovieAdded(id: Int)

sealed trait BaseMovie {
  val name: String
  val rating: Double
}
sealed trait Id {
  val id: Int
}
case class MovieProfile(id: Int, name: String, rating: Double, relations: Set[Int]) extends BaseMovie with Id
case class Movie(id: Int, name: String, rating: Double) extends BaseMovie with Id
case class MovieInfo(name: String, rating: Double) extends BaseMovie


object MovieProfile {
  def apply(base: Movie, relations: Set[Int]): MovieProfile =
    apply(base.id, base.name, base.rating, relations)
}

object Movie {
  def apply(id: Int, base: MovieInfo): Movie =
    apply(id, base.name, base.rating)
}

