package org.daviscale.customer

import actor._

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import java.time.LocalDate

import spray.json._
import spray.json.DefaultJsonProtocol._

// class is used to return status messages in json format
case class StatusMessage(message: String)

object LocalDateJsonProtocol {
  implicit object LocalDateJsonFormat extends RootJsonFormat[LocalDate] {
    def write(localDate: LocalDate) = {
      JsString(localDate.format(CustomerRecordExtractor.dateTimeFormatter))
    }

    def read(value: JsValue) = {
      value match {
        case JsString(dateStr) =>
          LocalDate.parse(dateStr, CustomerRecordExtractor.dateTimeFormatter)
        case _ =>
          deserializationError("Date expected")
      }
    }
  }
}

object SortingMethodJsonProtocol {
  implicit object SortingMethodJsonFormat extends RootJsonFormat[SortingMethod] {
    def write(sortingMethod: SortingMethod) = {
      JsString(sortingMethod.toString)
    }

    def read(value: JsValue) = {
      value match {
        case JsString(sortingMethodStr) if SortingMethod.fromString(sortingMethodStr).isDefined =>
          SortingMethod.fromString(sortingMethodStr).get
        case _ =>
          deserializationError("Sorting method expected")
      }
    }
  }
}

object JsonFormats {
  implicit val printer = PrettyPrinter
  import LocalDateJsonProtocol._
  import SortingMethodJsonProtocol._
  implicit val statusMessageFormat = jsonFormat1(StatusMessage)
  implicit val customerRecordFormat = jsonFormat5(CustomerRecord)
  implicit val addRecordFormat = jsonFormat1(AddRecord)
  implicit val listRecordsFormat = jsonFormat1(GetRecords)
  implicit val sortedRecordsFormat = jsonFormat1(SortedRecords)
}
