package shtykh.movie

import akka.actor.ActorRef

sealed trait BaseMovieMessage {
  val receiver: ActorRef
}

case class GetAll(receiver: ActorRef) extends BaseMovieMessage
case class Get(id: Int, receiver: ActorRef) extends BaseMovieMessage
case class GetProfile(id: Int, receiver: ActorRef) extends BaseMovieMessage
case class Add(movie: MovieInfo, receiver: ActorRef) extends BaseMovieMessage
case class AddRelations(id: Int, relations: Set[Int], receiver: ActorRef) extends BaseMovieMessage
case class GetRelevant(id: Int, amount: Int, receiver: ActorRef) extends BaseMovieMessage

case class MovieNotFound(id: Int, receiver: ActorRef) extends BaseMovieMessage

case class Distances(centerId: Int, distances: List[(Int, Int)], receiver: ActorRef) extends BaseMovieMessage
case class DistancesRequest(id: Int, receiver: ActorRef) extends BaseMovieMessage
case class MovieMessage(movie: Movie, receiver: ActorRef) extends BaseMovieMessage
case class CalculateRelevancy(movieId: Int, distance: Int, receiver: ActorRef) extends BaseMovieMessage

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
case class MovieRelevancy(id: Int, name: String, rating: Double, relevance: Double) extends BaseMovie with Id


object MovieProfile {
  def apply(base: Movie, relations: Set[Int]): MovieProfile =
    apply(base.id, base.name, base.rating, relations)
}

object MovieRelevancy {
  def apply(base: Movie, relevancy: Double): MovieRelevancy =
    apply(base.id, base.name, base.rating, relevancy)
  def unapply(base: Movie, relevancy: Double): (Int, String, Double, Double) =
    (base.id, base.name, base.rating, relevancy)
}

class MovieRelevancyReversedOrdering extends Ordering[MovieRelevancy] {
  override def compare(o1: MovieRelevancy, o2: MovieRelevancy): Int = (o2.relevance - o1.relevance).asInstanceOf[Int]
}

object Movie {
  def apply(id: Int, base: MovieInfo): Movie =
    apply(id, base.name, base.rating)
}

