package org.daviscale.customer

object CustomerRecordSorter {

  def sortByColorAndLastNameAscending(customers: Seq[CustomerRecord]): Seq[CustomerRecord] = {
    customers
      .groupBy(_.favoriteColor) // Map[String, Seq[CustomerRecord]]
      .toSeq // Seq[(String, Seq[CustomerRecord])]
      .map { case (color, subSeq) =>
        // within each subsequence grouped by color, sort by lastName
        val sortedSubSeq = subSeq.sortWith { (customer1, customer2) =>
          customer1.lastName > customer2.lastName
        }
        (color, sortedSubSeq)
      }
      .sortWith { (tuple1, tuple2) =>
        // sort the color groups
        tuple1._1 > tuple2._1
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
        customer1.lastName < customer2.lastName
      }
  }

}
