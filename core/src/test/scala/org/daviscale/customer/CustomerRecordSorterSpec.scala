package org.daviscale.customer

import java.time.LocalDate

import org.scalatest.matchers.should._
import org.scalatest.flatspec.AnyFlatSpec

class CustomerRecordSorterSpec extends AnyFlatSpec with Matchers {

  import TestFixture._

  "CustomerRecordSorterSpec" should "sort by favorite color and then by last name ascending" in {
    CustomerRecordSorter.sortByColorAndLastNameAscending(customers.reverse) should contain theSameElementsInOrderAs customers
  }

  it should "sort by birth date ascending" in {
    CustomerRecordSorter.sortByBirthDateAscending(customers) should contain theSameElementsInOrderAs Seq(
      one,
      five,
      three,
      four,
      two,
      six
    )
  }

  it should "sort by last name descending" in {
    CustomerRecordSorter.sortByLastNameDescending(customers) should contain theSameElementsInOrderAs(customers.reverse)
  }

  object TestFixture {
    val baseRecord = CustomerRecord(
      "John",
      "Doe",
      "noreply@example.com",
      "Red",
      LocalDate.of(2000, 1, 1)
    )

    val one = baseRecord.copy(
      lastName = "Alpha",
      favoriteColor = "Blue",
      dateOfBirth = LocalDate.of(1940, 4, 3)
    )

    val two = baseRecord.copy(
      lastName = "Bravo",
      favoriteColor = "Blue",
      dateOfBirth = LocalDate.of(1950, 7, 15)
    )

    val three = baseRecord.copy(
      lastName = "Charlie",
      favoriteColor = "Green",
      dateOfBirth = LocalDate.of(1945, 9, 22)
    )

    val four = baseRecord.copy(
      lastName = "Delta",
      favoriteColor = "Green",
      dateOfBirth = LocalDate.of(1947, 2, 17)
    )

    val five = baseRecord.copy(
      lastName= "Echo",
      favoriteColor = "Purple",
      dateOfBirth = LocalDate.of(1940, 4, 4)
    )

    val six = baseRecord.copy(
      lastName = "Foxtrox",
      favoriteColor = "Purple",
      dateOfBirth = LocalDate.of(1950, 8, 15)
    )

    val customers = Seq(
      six,
      five,
      four,
      three,
      two,
      one
    )
  }
}
