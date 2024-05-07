package com.thomas.exposed.properties

import com.thomas.exposed.exception.ExposedException

data class ExposedAuditoryProperties(
    val tablesPackage: String = "",
    val tablesExcluded: List<String> = listOf(),
) {

    init {
        if (tablesPackage.trim().isEmpty()) {
            throw ExposedException("Auditory tables package cannot be null nor empty")
        }
    }

}
