package org.daviscale.customer
package rest

import actor._

import akka.actor._
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._

object CustomerManagementServer {
  // needed for serializing and deserializing JSON
  import JsonFormats._

  implicit val system = ActorSystem("customer-management-system")
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  var bindingFuture: Future[ServerBinding] = null

  def main(args: Array[String]): Unit = {
    val customerRecordActor = system.actorOf(Props[CustomerRecordActor], "customer-record-actor")

    // timeout for the GET requests to return
    implicit val timeout: Timeout = 10.seconds

    val route = {
      pathPrefix("records") {
        post {
          entity(as[String]) { delimitedStr =>
            customerRecordActor ! AddRecord(delimitedStr)
            complete(Future.successful(StatusMessage("Record added")))
          }
        } ~
        path("color") {
          get {
            val future = (customerRecordActor ? GetRecords(SortingMethod.ColorAndLastName)).mapTo[SortedRecords]
            complete(future)
          }
        } ~
        path("birthdate") {
          get {
            val future = (customerRecordActor ? GetRecords(SortingMethod.BirthDate)).mapTo[SortedRecords]
            complete(future)
          }
        } ~
        path("name") {
          get {
            val future = (customerRecordActor ? GetRecords(SortingMethod.LastName)).mapTo[SortedRecords]
            complete(future)
          }
        }
      }
    }

    bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

    println(s"Server online at http://localhost:8080/")
  }

  def stopServer() {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
