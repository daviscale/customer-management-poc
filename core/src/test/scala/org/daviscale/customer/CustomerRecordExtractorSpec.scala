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
      "12/01/1970"
    ).mkString(delimiter)
  }

  val expectedCustomerRecord = CustomerRecord(
    "Jane",
    "Doe",
    "jane.doe@example.org",
    "Orange",
    LocalDate.of(1970, 1, 12)
  )

  def runTest(delimiter: String): Boolean = {
    CustomerRecordExtractor
      .extract(makeInputRow(delimiter)) == expectedCustomerRecord
  }

  "CustomerRecordExtractor" should "extract a CustomerRecord when a comma delimiter is used" in {
    assert(runTest(","))
    assert(runTest(" , "))
  }

  it should "extract a CustomerRecord when a pipe delimiter is used" in {
    assert(runTest("|"))
    assert(runTest(" | "))
  }

  it should "extract a CustomerRecord when a space delimiter is used" in {
    assert(runTest(" "))
  }
}
