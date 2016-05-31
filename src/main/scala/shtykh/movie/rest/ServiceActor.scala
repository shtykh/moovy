package shtykh.movie.rest

import akka.actor.ActorLogging
import com.gettyimages.spray.swagger._
import com.wordnik.swagger.model.ApiInfo
import spray.routing._

import scala.reflect.runtime.universe._

class ServiceActor
  extends HttpServiceActor
  with ActorLogging {

    override def actorRefFactory = context

    val movies = new MovieApi {
      def actorRefFactory = context
    }

    def receive = runRoute(movies.routes ~ swaggerService.routes ~
      get {
        pathPrefix("") { pathEndOrSingleSlash {
            getFromResource("swagger-ui/index.html")
          }
        } ~
        getFromResourceDirectory("swagger-ui")
      })

  val swaggerService = new SwaggerHttpService {
    override def apiTypes = Seq(typeOf[MovieApi])
    override def apiVersion = "2.0"
    override def baseUrl = "/"
    override def docsPath = "api-docs"
    override def actorRefFactory = context
    override def apiInfo = Some(new ApiInfo(
      "Movie finder", 
      "Find yourself a relevant movie to watch!", 
      "TOC Url", 
      "shtykh.alexey@gmail.com", 
      "Apache V2", 
      "http://www.apache.org/licenses/LICENSE-2.0"))
  }
}
