package org.daviscale.customer

import java.time.LocalDate

case class CustomerRecord(
  firstName: String,
  lastName: String,
  email: String,
  favoriteColor: String,
  dateOfBirth: LocalDate
) {
  def toRow(delimiter: Delimiter): String = {
    // IMO comma delimiters look best when there's a single space after the comma
    // Pipe delimiters look best when there's a space before and after the pipe
    val readableDelimiter = delimiter match {
      case Delimiter.Comma =>
        ", "
      case Delimiter.Pipe =>
        " | "
      case Delimiter.Space =>
        " "
    }
    List(
      lastName,
      firstName,
      email,
      favoriteColor,
      dateOfBirth.format(CustomerRecordExtractor.dateTimeFormatter)
    ).mkString(readableDelimiter)
  }
}
