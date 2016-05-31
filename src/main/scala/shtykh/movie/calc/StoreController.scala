package shtykh.movie.calc

import akka.actor.{ActorRef, Actor, ActorLogging}
import shtykh.movie._

import scala.collection.parallel.mutable

/**
  * Created by shtykh on 10/05/16.
  */
class StoreController(graph: ActorRef) extends Actor with ActorLogging {
  def initStore(): mutable.ParHashMap[Int, Movie] = { // TODO
    store = new mutable.ParHashMap[Int, Movie]
    add(MovieInfo("Terminator", 12))
    add(MovieInfo("Terminator II", 120))
    store
  }

  var store: mutable.ParHashMap[Int, Movie] = initStore()

  def returnToSender(id: Int, responder: ActorRef, value: Any): Unit = {
    get(id) match {
      case Some(m: Movie) => sender ! (MovieMessage(m, responder), value)
      case _ => responder ! MovieNotFound
    }
  }

  override def receive = {
    case Add(movie, responder) => responder ! MovieAdded(add(movie))
    case (Get(id, responder), a: Any) => returnToSender(id, responder, a)
    case Get(id, responder) => sendIfGotten(id, sender(), responder)
    case GetProfile(id, responder) => sendIfGotten(id, graph, responder)
    case GetAll(responder) => responder ! getAll
  }
  
  def sendIfGotten(id: Int, recepient: ActorRef, responder: ActorRef) = {
    get(id) match {
      case Some(m: Movie) => recepient ! MovieMessage(m, responder)
      case _ => responder ! MovieNotFound
    }
  }
  def get(id: Int) = store get id
  def getAll: List[Movie] = store.values.toList

  def add(movie: MovieInfo): Int = {
    val id = StoreController.nextId()
    store += (id -> Movie(id, movie))
    id
  }
}

object StoreController{
  var id: Int = 0
  def nextId(): Int = {
    id = id + 1
    id
  }
}
