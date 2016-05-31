package shtykh.movie.calc

import akka.actor.{Actor, ActorLogging}
import shtykh.movie._

import scala.collection.parallel.mutable

/**
  * Created by shtykh on 10/05/16.
  */
class StoreController extends Actor with ActorLogging {
  def initStore(): mutable.ParHashMap[Int, Movie] = { // TODO
    store = new mutable.ParHashMap[Int, Movie]
    add(MovieInfo("Terminator", 12))
    add(MovieInfo("Terminator II", 120))
    store
  }

  var store: mutable.ParHashMap[Int, Movie] = initStore()
  
  override def receive = {
    case Add(movie, responder) =>
      responder ! MovieAdded(add(movie))
    case Get(id, responder) =>
      get(id) match {
        case Some(m: Movie) => sender ! NeighborRequest(m, responder)
        case _ => responder ! MovieNotFound
      }
    case GetAll(responder) =>
      responder ! getAll
  }
  
  def get(id: Int) = store get id
  def getAll: List[Movie] = store.values.toList
  def add(movie: MovieInfo): Int = {
    val id = movie.name.length
    store += (id -> Movie(id, movie))
    id
  }

}
