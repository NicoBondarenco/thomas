package com.thomas.management.data.entity.info

import java.time.OffsetDateTime

interface BasicInfo {

    val isActive: Boolean
    val createAt: OffsetDateTime
    val updatedAt: OffsetDateTime

}
