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
  // needed for the future flatMap/onComplete in the stopServer method
  implicit val executionContext = system.dispatcher

  val customerRecordActor = system.actorOf(Props[CustomerRecordActor], "customer-record-actor")
  var bindingFuture: Future[ServerBinding] = null

  // timeout for the GET requests to return
  implicit val timeout: Timeout = 10.seconds

  def main(args: Array[String]): Unit = {
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
            completeGetRequest(SortingMethod.ColorAndLastName)
          }
        } ~
        path("birthdate") {
          get {
            completeGetRequest(SortingMethod.BirthDate)
          }
        } ~
        path("name") {
          get {
            completeGetRequest(SortingMethod.LastName)
          }
        } ~
        path("email") {
          get {
            completeGetRequest(SortingMethod.Email)
          }
        }
      }
    }

    bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

    println(s"Server online at http://localhost:8080/")
  }

  def completeGetRequest(sortingMethod: SortingMethod) = {
    val future = (customerRecordActor ? GetRecords(sortingMethod)).mapTo[SortedRecords]
    complete(future)
  }

  def stopServer() {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
