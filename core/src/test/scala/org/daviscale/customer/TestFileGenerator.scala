package org.daviscale.customer

import java.io.FileOutputStream
import java.time.LocalDate

import scala.util.Random

/**
 * A utility used to create test files. These methods are called from the Scala
 * console
 */
object TestFileGenerator {

  val firstNames = Seq(
    "Al",
    "Bob",
    "Frank",
    "George",
    "Sally",
    "Betty",
    "Jane",
    "Mary"
  )

  val lastNames = Seq(
    "Hill",
    "Miller",
    "Washington",
    "Smith",
    "Jones"
  )

  val emails = Seq(
    "noreply@example.com",
    "business@example.com",
    "volunteer@example.org"
  )

  val favoriteColors = Seq(
    "Red",
    "Orange",
    "Yellow",
    "Blue",
    "Indigo",
    "Violet"
  )

  def randomRangeValue(range: Range): Int = {
    randomSeqEntry(range.toVector)
  }

  def randomSeqEntry[A](seq: Seq[A]): A = {
    val index = Random.nextInt(seq.size)
    seq(index)
  }

  def generateRandomData(
    dataSize: Int,
    delimiter: String
  ): Seq[CustomerRecord] = {
    (1 to dataSize) map { _ =>
      CustomerRecord(
        firstName = randomSeqEntry(firstNames),
        lastName = randomSeqEntry(lastNames),
        email = randomSeqEntry(emails),
        favoriteColor = randomSeqEntry(favoriteColors),
        dateOfBirth = LocalDate.of(randomRangeValue(1940 to 2000), randomRangeValue(1 to 12), randomRangeValue(1 to 28))
      )
    }
  }

  def customerRecordToString(
    customerRecord: CustomerRecord,
    delimiter: String
  ): String = {
    Seq(
      customerRecord.lastName,
      customerRecord.firstName,
      customerRecord.email,
      customerRecord.favoriteColor,
      customerRecord.dateOfBirth
    ).mkString(delimiter)
  }

  def generateTestFile(
    resourcePath: String,
    delimiter: String,
    dataSize: Int
  ): Unit = {
    val customerRecords = generateRandomData(dataSize, delimiter)
    val fileContent = customerRecords
      .map(customerRecordToString(_, delimiter))
      .mkString("\n")
    val fileOutputStream = new FileOutputStream(resourcePath)
    fileContent.getBytes().foreach { byte =>
      fileOutputStream.write(byte)
    }
    fileOutputStream.close
  }
}
