package com.thomas.management.data.entity.info

import java.time.OffsetDateTime

interface CreationInfo {

    val createdAt: OffsetDateTime
    val updatedAt: OffsetDateTime

}
