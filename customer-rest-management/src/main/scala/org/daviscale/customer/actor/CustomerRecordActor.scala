package org.daviscale.customer
package actor

import akka.actor.Actor

import scala.collection.mutable.ListBuffer

case class AddRecord(postBody: String)
case object RecordAdded

case class ListRecords(sortingMethod: SortingMethod)
case class SortedRecords(customerRecords: Seq[CustomerRecord])

class CustomerRecordActor extends Actor {

  val customerRecords = ListBuffer.empty[CustomerRecord]

  def receive = {
    case AddRecord(postBody) =>
      val customerRecord = CustomerRecordExtractor.extract(postBody)
      customerRecords += customerRecord
      sender() ! RecordAdded
    case ListRecords(sortingMethod) =>
      sender() ! SortedRecords(CustomerRecordSorter.sort(customerRecords, sortingMethod))
    case other =>
      println(s"ERROR: Received an unknown message $other")
  }

}
