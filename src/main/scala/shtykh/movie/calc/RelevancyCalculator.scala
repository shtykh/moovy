package shtykh.movie.calc

import akka.actor.{Actor, ActorRef}
import shtykh.movie._

/**
  * Created by shtykh on 10/05/16.
  */
class RelevancyCalculator(store: ActorRef) extends Actor {

   def coef(distance: Distance): Double = distance match {
     case Infinity(_) => 0
     case Finite(value) => 1.0 / (1 << (value - 1) )
   }

   override def receive: Receive = {
     case CalculateRelevancy(id, radius, ref) => 
       store ! (Get(id, ref), radius)
     case (MovieMessage(movie, receiver), radius: Distance) =>
       receiver ! MovieRelevancy(movie, movie.rating * coef(radius))
   }
 }
