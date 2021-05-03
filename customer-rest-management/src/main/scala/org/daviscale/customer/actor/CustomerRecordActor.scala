package org.daviscale.customer
package actor

import akka.actor.Actor

import scala.collection.mutable.ListBuffer

case class AddRecord(delimitedRecord: String)
case object RecordAdded

case class GetRecords(sortingMethod: SortingMethod)
case class SortedRecords(customerRecords: Seq[CustomerRecord])

class CustomerRecordActor extends Actor {

  val customerRecords = ListBuffer.empty[CustomerRecord]

  def receive = {
    case AddRecord(delimitedRecord) =>
      val customerRecord = CustomerRecordExtractor.extract(delimitedRecord)
      customerRecords += customerRecord
    case GetRecords(sortingMethod) =>
      sender() ! SortedRecords(CustomerRecordSorter.sort(customerRecords, sortingMethod))
    case other =>
      println(s"ERROR: Received an unknown message $other")
  }

}
