package org.daviscale.customer

sealed trait SortingMethod {
  val prettyPrinted: String
}

object SortingMethod {
  case object ColorAndLastName extends SortingMethod {
    val prettyPrinted: String = "Sorted by color and last name in ascending order"
  }
  case object BirthDate extends SortingMethod {
    val prettyPrinted: String = "Sorted by birth date in ascending order"
  }
  case object LastName extends SortingMethod {
    val prettyPrinted: String = "Sorted by last name in descending order"
  }

  case object Email extends SortingMethod {
    val prettyPrinted: String = "Sorted by email in descending order"
  }

  val all = Seq(
    ColorAndLastName,
    BirthDate,
    LastName,
    Email
  )

  def fromString(sortingMethodStr: String): Option[SortingMethod] = {
    all
      .find(_.toString.equalsIgnoreCase(sortingMethodStr))
  }
}

object CustomerRecordSorter {

  def sort(customers: Seq[CustomerRecord], sortingMethod: SortingMethod): Seq[CustomerRecord] = {
    sortingMethod match {
      case SortingMethod.ColorAndLastName =>
        sortByColorAndLastNameAscending(customers)
      case SortingMethod.BirthDate =>
        sortByBirthDateAscending(customers)
      case SortingMethod.LastName =>
        sortByLastNameDescending(customers)
      case SortingMethod.Email =>
        sortByEmailDescending(customers)
    }
  }

  def sortByColorAndLastNameAscending(customers: Seq[CustomerRecord]): Seq[CustomerRecord] = {
    customers
      .groupBy(_.favoriteColor) // Map[String, Seq[CustomerRecord]]
      .toSeq // Seq[(String, Seq[CustomerRecord])]
      .map { case (color, subSeq) =>
        // within each subsequence grouped by color, sort by lastName
        val sortedSubSeq = subSeq.sortWith { (customer1, customer2) =>
          customer1.lastName < customer2.lastName
        }
        (color, sortedSubSeq)
      }
      .sortWith { (tuple1, tuple2) =>
        // sort the color groups
        tuple1._1 < tuple2._1
      }
      .flatMap(_._2) // just keep the customer records
  }

  def sortByBirthDateAscending(customers: Seq[CustomerRecord]): Seq[CustomerRecord] = {
    customers
      .sortWith { (customer1, customer2) =>
        customer1.dateOfBirth.isBefore(customer2.dateOfBirth)
      }
  }

  def sortByLastNameDescending(customers: Seq[CustomerRecord]): Seq[CustomerRecord] = {
    customers
      .sortWith { (customer1, customer2) =>
        customer1.lastName > customer2.lastName
      }
  }

  def sortByEmailDescending(customers: Seq[CustomerRecord]): Seq[CustomerRecord] = {
    customers
      .sortWith { (customer1, customer2) =>
        customer1.email > customer2.email
      }
  }

}
