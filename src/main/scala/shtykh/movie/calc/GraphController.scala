package shtykh.movie.calc

import java.util.NoSuchElementException

import akka.actor.{ActorLogging, Actor}
import shtykh.movie._

import scalax.collection.GraphEdge._
import scalax.collection.mutable.Graph
import scalax.collection.GraphPredef._

/**
  * Created by shtykh on 10/05/16.
  */
class GraphController extends Actor with ActorLogging {
  val graph = Graph[Int, UnDiEdge]()

  override def receive = {
    case m @ AddRelations(id, relations, responder) =>
      responder ! addRelations(id, relations)
    case m @ DistancesRequest(id, receiver) =>
      sender ! Distances(id, countDistances(id), receiver)
    case m @ NeighborRequest(movie, receiver) =>
      receiver ! MovieProfile(movie, getNeighbors(movie.id))
    case m @ _ => sender ! NotImplementedMessage("graph", m)
  }

  def countDistances(id: Int): List[(Int, Int)] = List.empty // TODO emplement it!
  def addRelations(id: Int, set: Set[Int]) = {
    set.foreach(uuid => graph += (id ~ uuid))
    s"$id set to be related to $set"
  }
  def getNeighbors(id: Int): Set[Int] = {
    try { // TODO nicer
      val node = graph get id
      (node neighbors).map(_.value.asInstanceOf[Int])
    } catch { 
      case e: NoSuchElementException => Set.empty
      case e: Throwable => throw e
    }
  }
}
