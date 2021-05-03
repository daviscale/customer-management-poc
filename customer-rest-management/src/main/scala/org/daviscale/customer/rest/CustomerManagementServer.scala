package org.daviscale.customer
package rest

import actor._

import akka.actor._

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn

object CustomerManagementServer {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("customer-management-system")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val customerRecordActor = system.actorOf(Props[CustomerRecordActor], "customer-record-actor")

    val route = {
      pathPrefix("records") {
        path("") {
          post {
            complete("yes")
          }
        } ~
        path("color") {
          get {
            complete("yes")
          }
        } ~
        path("birthdate") {
          get {
            complete("yes")
          }
        } ~
        path("name") {
          get {
            complete("yes")
          }
        }
      }
    }

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
