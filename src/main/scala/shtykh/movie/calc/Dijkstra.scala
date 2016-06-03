package shtykh.movie.calc

/**
 * Created by shtykh on 01/06/16.
 */

import scala.collection.mutable._
import scalax.collection.GraphEdge._
import scalax.collection.mutable.Graph

// from https://raw.githubusercontent.com/yin/scala-dijkstra/master/src/Dijkstra.scala
class Dijkstra[T](graph: Graph[T, UnDiEdge]) {
  def compute(startValue: T): Map[T, Distance] = {
    var queue: Set[T] = new HashSet()
    var settled: Set[T] = new HashSet()
    val distances: Map[T, Distance] = new HashMap()
    graph.nodes.foreach(node=>distances(node.value.asInstanceOf[T])=Infinity(0))
    val start = graph get startValue
    queue += start
    distances(start) = Finite(0)

    while(!queue.isEmpty) {
      val u = extractMinimum(queue, distances)
      settled += graph get u
      relaxNeighbors(u, queue, settled, distances)
    }
    distances -= start
    return distances
  }

  /**
   * Finds element of <code>Q</code> with minimum value in D, removes it
   * from Q and returns it.
   */
  protected def extractMinimum[T](Q: Set[T], D: Map[T, Distance]): T = {
    var u = Q.head
    Q.foreach((node) =>  if(D(u) > D(node)) u = node)
    Q -= u
    u
  }

  /**
   * For all nodes <code>v</code> not in <code>S</code>, neighbors of
   * <code>u</code>}: Updates shortest distances and paths, if shorter than
   * the previous value.
   */
  protected def relaxNeighbors(u: T, 
                               Q: Set[T], 
                               S: Set[T],
                               D: Map[T, Distance]): Unit = {
    for(v <- graph get u neighbors) {
      if(!S.contains(v)) {
        if(!D.contains(v) || D(v).compareTo(D(u) + Finite(1)) > 0) {
          D(v) = Finite(D(u).value + 1)
          Q += v
        }
      }
    }
  }
}
  
abstract class Distance extends Comparable[Distance] {
  def value: Int
  def + (other: Distance): Distance
  def > (other: Distance): Boolean = compareTo(other) > 0

}

case class Finite(value: Int) extends Distance {
  override def compareTo(o: Distance): Int =
    o match {
      case Infinity(_) => -1
      case Finite(itsValue) => value - itsValue
    }

  override def +(other: Distance): Distance = other match {
    case Finite(otherValue) => Finite(value + otherValue)
    case Infinity(_) => Infinity(0)
    
  }
}

case class Infinity(value: Int) extends Distance {
  override def compareTo(o: Distance): Int = 
    o match {
      case Infinity(_) => 0
      case Finite(_) => 1
    }
  override def +(other: Distance): Distance = Infinity(0)
}
