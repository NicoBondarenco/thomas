package com.thomas.storage.exception

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType.INVALID_ENTITY

class FileResourceException(
    message: String,
    detail: List<String>,
) : DetailedException(
    message = message,
    detail = detail,
    type = INVALID_ENTITY
)
