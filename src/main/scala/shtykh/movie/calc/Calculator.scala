package shtykh.movie.calc

import akka.actor.{Actor, PoisonPill}
import shtykh.movie.CalculateRelevancy

/**
  * Created by shtykh on 10/05/16.
  */
class Calculator extends Actor {

   def twoInKMinusOne(k: Int) = 1 << (k-1)

   override def receive: Receive = {
     case CalculateRelevancy(rating, radius, ref) =>
       println("asking for %d and %s".format(radius, rating))
       val twos = twoInKMinusOne(radius)
       println("2^(%d-1) = %s".format(radius, twos))
       println("%s / 2^(%d-1) = %s".format(rating, radius, rating / twos))
     case PoisonPill => println("I died")
     case _ => println("I dunno :(")
   }
 }

//object Calculator {
//  val system = ActorSystem("CalcSystem")
//  val calc = system.actorOf(Props[Calculator], name = "calc")
//  for (radius <- 0 to 10) {
//    calculate(0.5, radius)
//  }
//  def calculate(rating: Double, radius: Int) = {
//    val req = new CalculateRelevancy(rating, radius)
//    calc ! req
//  }
//}
