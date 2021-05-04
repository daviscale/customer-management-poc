package org.daviscale.customer
package rest

import actor.SortedRecords

import akka.actor.ActorSystem
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.Http
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AsyncFlatSpec

import scala.concurrent.Future
import scala.io.Source

import spray.json._
import spray.json.DefaultJsonProtocol._

class CustomerRestManagementApiIntegrationSpec extends AsyncFlatSpec with BeforeAndAfterAll {

  implicit val actorSystem = ActorSystem("CustomerRestManagementApiIntegrationSpec")

  override def beforeAll() {
    // future is needed so that the server doesn't block the execution of the tests
    Future { CustomerManagementServer.main(Array.empty) }
  }

  override def afterAll() {
    CustomerManagementServer.stopServer()
  }

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

  lazy val commaCustomer = CustomerRecordExtractor.extract(commaRow)
  lazy val pipeCustomer = CustomerRecordExtractor.extract(pipeRow)
  lazy val spaceCustomer = CustomerRecordExtractor.extract(spaceRow)

  def runPostTest(row: String) = {
    val expectedResponseContent = "Record added"
    Http()
      .singleRequest(Post("http://localhost:8080/records", row))
      .flatMap { response => Unmarshal(response).to[StatusMessage] }
      .map { statusMessage =>
        assert(statusMessage.message == expectedResponseContent)
      }
  }

  def runGetTest(expectedRecords: Seq[CustomerRecord], endpoint: String) = {
    Http()
      .singleRequest(Get(s"http://localhost:8080/records/$endpoint"))
      .flatMap { response => Unmarshal(response).to[SortedRecords].map(_.customerRecords) }
      .map { customerRecords =>
        assert(customerRecords(0) == expectedRecords(0))
        assert(customerRecords(1) == expectedRecords(1))
        assert(customerRecords(2) == expectedRecords(2))
      }
  }

  "The Customer Management REST API app" should "add records via HTTP POST" in {
    runPostTest(commaRow)
    runPostTest(pipeRow)
    runPostTest(spaceRow)
  }

  it should "return records sorted by color and then last name when the /records/color endpoint is used" in {
    runGetTest(Seq(commaCustomer, spaceCustomer, pipeCustomer), "color")
  }

  it should "return records sorted by birth date when the /records/birthdate endpoint is used" in {
    runGetTest(Seq(spaceCustomer, pipeCustomer, commaCustomer), "birthdate")
  }

  it should "return records sorted by last name when the /records/name endpoint is used" in {
    runGetTest(Seq(pipeCustomer, commaCustomer, spaceCustomer), "name")
  }
}
