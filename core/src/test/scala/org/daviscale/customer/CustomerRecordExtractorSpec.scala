package org.daviscale.customer

import java.time.LocalDate

import org.scalatest.flatspec.AnyFlatSpec

class CustomerRecordExtractorSpec extends AnyFlatSpec {

  def makeInputRow(delimiter: String): String = {
    List(
      "Doe",
      "Jane",
      "jane.doe@example.org",
      "Orange",
      "01/01/1970"
    ).mkString(delimiter)
  }

  val expectedCustomerRecord = CustomerRecord(
    "Jane",
    "Doe",
    "jane.doe@example.org",
    "Orange",
    LocalDate.of(1970, 5, 1)
  )

  def runTest(delimiter: String): Boolean = {
    CustomerRecordExtractor.extract(makeInputRow(delimiter)) == expectedCustomerRecord
  }

  "CustomerRecordExtractor" should "extract a CustomerRecord when a comma delimiter is used" in {
    runTest(",")
    runTest(" , ")
  }

  it should "extract a CustomerRecord when a pipe delimiter is used" in {
    runTest("|")
    runTest(" | ")
  }

  it should "extract a CustomerRecord when a space delimiter is used" in {
    runTest("")
  }
}
