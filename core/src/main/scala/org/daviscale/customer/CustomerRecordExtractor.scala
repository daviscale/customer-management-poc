package org.daviscale.customer

import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed trait Delimiter {
  val stringValue: String
}

object Delimiter {
  case object Comma extends Delimiter {
    val stringValue = ","
  }
  case object Pipe extends Delimiter {
    val stringValue = "\\|"
  }
  case object Space extends Delimiter {
    val stringValue = " "
  }

  val all = List(Comma, Pipe, Space)

  def fromString(delimiterStr: String): Option[Delimiter] = {
    all
      .find(_.toString.equalsIgnoreCase(delimiterStr))
  }
}

object CustomerRecordExtractor {

  def findDelimiter(inputRow: String): Delimiter = {
    if (inputRow.contains(",")) {
      Delimiter.Comma
    } else if (inputRow.contains("|")) {
      Delimiter.Pipe
    } else {
      Delimiter.Space
    }
  }

  val dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

  def extract(inputRow: String): CustomerRecord = {
    val delimiter = findDelimiter(inputRow)
    val fields = inputRow
      .split(delimiter.stringValue)
      .map(_.trim)
    val dateOfBirth = LocalDate.parse(fields(4), dateTimeFormatter)
    CustomerRecord(
      firstName = fields(1),
      lastName = fields(0),
      email = fields(2),
      favoriteColor = fields(3),
      dateOfBirth = dateOfBirth
    )
  }

}
