package org.daviscale.customer

import actor._

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import java.time.LocalDate

import spray.json._
import spray.json.DefaultJsonProtocol._

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
  import LocalDateJsonProtocol._
  import SortingMethodJsonProtocol._
  implicit val customerRecordFormat = jsonFormat5(CustomerRecord)
  implicit val listRecordsFormat = jsonFormat1(GetRecords)
  implicit val sortedRecordsFormat = jsonFormat1(SortedRecords)
}
