package org.daviscale.customer

import java.time.LocalDate

case class CustomerRecord(
  firstName: String,
  lastName: String,
  email: String,
  favoriteColor: String,
  dateOfBirth: LocalDate
)
