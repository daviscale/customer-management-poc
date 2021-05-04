package org.daviscale.customer

import java.io.FileInputStream

import scala.io.Source

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
      "./record-sorter-cmd [output-delimiter] [sorting-method] [filename1] [filename2] ...",
      "",
      "output-delimiter must be one of the following words: comma, space, pipe",
      s"sorting-method must be one of: $allSortingMethodsPrinted",
      "",
      "Filenames must refer to a comma, pipe, and space delimited file",
      "At least one filename argument must be present",
      "Example usage:",
      s"./record-sorter-cmd comma ${SortingMethod.BirthDate} record1.txt record2.txt record3.txt"
    )

    messages.foreach(println)
  }

  def readFile(fileName: String): Seq[String] = {
    Source
      .fromFile(fileName)
      .getLines()
      .toSeq
  }

  def sortAndPrintOutput(
    outputDelimiter: Delimiter,
    rows: Seq[String],
    sortingMethod: SortingMethod
  ): Unit = {
    val customerRecords = rows.map(CustomerRecordExtractor.extract(_))
    val sortedRecords = CustomerRecordSorter.sort(customerRecords, sortingMethod)
    val outputRows = sortedRecords.map(_.toRow(outputDelimiter))
    println(s"${sortingMethod.prettyPrinted}:")
    println("")
    outputRows.foreach(println)
  }


  def main(args: Array[String]): Unit = {
    args.toList match {
      case preferredDelimiterStr :: sortingMethodStr :: rest if SortingMethod.fromString(sortingMethodStr).isDefined &&
        Delimiter.fromString(preferredDelimiterStr).isDefined && rest.nonEmpty => {
          val outputDelimiter = Delimiter.fromString(preferredDelimiterStr).get
          val sortingMethod = SortingMethod.fromString(sortingMethodStr).get
          val rows = rest.flatMap(fileName => readFile(fileName))
          sortAndPrintOutput(outputDelimiter, rows, sortingMethod)
        }
      case _ =>
        printHelpMessage()
    }
  }

}
