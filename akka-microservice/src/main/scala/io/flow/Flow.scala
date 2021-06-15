package io.flow

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.`Set-Cookie`
import akka.http.scaladsl.model._

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.Future
import akka.stream.{ActorMaterializer}

class HttpClientCollectingHeaders {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "http://akka.io"))

    responseFuture.map {
      case response @ HttpResponse(StatusCodes.OK, _, _, _) =>
        val setCookies = response.headers[`Set-Cookie`]
        println(s"Cookies set by a server: $setCookies")
        response.discardEntityBytes()
      case _ => sys.error("something wrong")
    }
  }
}