package shtykh.movie

import akka.actor.ActorDSL._
import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.io.IO
import akka.io.Tcp._
import shtykh.movie.rest.ServiceActor
import spray.can.Http

object SampleApp extends App {
  implicit val system = ActorSystem("movie-system")

  /* Spray Service */
  val service= system.actorOf(Props[ServiceActor], "spray-swagger-service")

  val ioListener = actor("ioListener")(new Act with ActorLogging {
    become {
      case b: Bound => log.info(b.toString)
    }
  })

  IO(Http).tell(Http.Bind(service, SampleConfig.HttpConfig.interface, SampleConfig.HttpConfig.port), ioListener)

}
