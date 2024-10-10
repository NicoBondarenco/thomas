package com.thomas.core.data

import com.thomas.core.extension.unaccentedLower
import com.thomas.core.model.general.Gender
import java.time.LocalDate
import java.util.UUID

data class PersonTestData(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val documentNumber: String,
    val phoneNumber: String,
    val birthDate: LocalDate,
    val userGender: Gender,
) {

    val mainEmail: String
        get() = "$firstName.$lastName@testmail.com".unaccentedLower()

}
