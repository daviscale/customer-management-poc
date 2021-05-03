package org.daviscale.customer
package rest

import akka.actor.ActorSystem
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.Http
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import org.scalatest.flatspec.AsyncFlatSpec

import scala.concurrent.Future
import scala.io.Source

import spray.json._
import spray.json.DefaultJsonProtocol._

class CustomerRestManagementApiIntegrationSpec extends AsyncFlatSpec {

  implicit val actorSystem = ActorSystem("CustomerRestManagementApiIntegrationSpec")

  // needed for serializing and deserializing JSON
  import JsonFormats._

  def getRow(fileName: String): String = {
    Source.fromFile(
      getClass().getResource(s"/$fileName").getFile
    ).getLines()
      .toSeq
      .head
  }

  lazy val commaRow:String = getRow("commaDelimited.txt")
  lazy val pipeRow:String = getRow("pipeDelimited.txt")
  lazy val spaceRow:String = getRow("spaceDelimited.txt")

  Future {  CustomerManagementServer.main(Array.empty) }

  def runPostTest(row: String) = {
    val expectedResponseContent = "Record added"
    Http()
      .singleRequest(Post("http://localhost:8080/records", row))
      .flatMap { response => Unmarshal(response).to[StatusMessage] }
      .map { statusMessage =>
        assert(statusMessage.message == expectedResponseContent)
      }
  }

  "The Customer Management REST API app" should "add records via HTTP POST" in {
    runPostTest(commaRow)
    runPostTest(pipeRow)
    runPostTest(spaceRow)
  }

  it should "return records sorted by color and then last name when the /records/color endpoint is used" in {
    assert(1 == 2)
  }

  it should "return records sorted by birth date when the /records/birthdate endpoint is used" in {
    assert(1 == 2)
  }

  it should "return records sorted by last name when the /records/name endpoint is used" in {
    assert(1 == 2)
  }
}
