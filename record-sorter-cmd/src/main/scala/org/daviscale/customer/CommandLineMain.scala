package org.daviscale.customer

import java.io.FileInputStream

import scala.io.Source
import scala.util._

object CommandLineMain {

  lazy val allSortingMethodsPrinted = SortingMethod
    .all
    .map(_.toString)
    .mkString(", ")

  def printHelpMessage(): Unit = {
    val messages = Seq(
      "Invalid usage.",
      "",
      "Correct usage is as follows.",
      "./sortCustomers [filename] [sorting-method]",
      "",
      "Filename must refer to a comma, pipe, and space delimited file",
      s"Sorting method must be one of: $allSortingMethodsPrinted",
      "Example usage:",
      s"./sortCustomers myCustomers.csv ${SortingMethod.BirthDate}"
    )

    messages.foreach(println)
  }

  def readFile(fileName: String): Try[Seq[String]] = {
    Try {
      Source
        .fromFile(fileName)
        .getLines()
        .toSeq
    }
  }

  def sortAndPrintOutput(rows: Seq[String], sortingMethod: SortingMethod): Unit = {
    val customerRecords = rows.map(CustomerRecordExtractor.extract(_))
    val sortedRecords = CustomerRecordSorter.sort(customerRecords, sortingMethod)
    println(s"${sortingMethod.prettyPrinted}:")
    println("")
    sortedRecords.foreach(println)
  }


  def main(args: Array[String]): Unit = {
    args.toList match {
      case fileName :: sortingMethodStr :: Nil if SortingMethod.fromString(sortingMethodStr).isDefined =>
        val sortingMethod = SortingMethod.fromString(sortingMethodStr).get
        readFile(fileName) match {
          case Success(rows) =>
            sortAndPrintOutput(rows, sortingMethod)
          case Failure(_) =>
            println(s"Unable to read file $fileName")
        }
      case fileName :: invalidSortingMethod :: Nil =>
        println(s"$invalidSortingMethod is not a valid sorting method")
        println(s"Use one of the following as a sorting method $allSortingMethodsPrinted")
      case _ =>
        printHelpMessage()
    }
  }

}
