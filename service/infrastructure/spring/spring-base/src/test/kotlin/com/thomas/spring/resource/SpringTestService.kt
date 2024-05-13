package com.thomas.spring.resource

import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType
import org.springframework.stereotype.Service

@Service
class SpringTestService {

    fun exceptionService(type: ErrorType) {
        throw object : DetailedException(
            message = "Exception occurred on ExceptionService - $type",
            type = type
        ) {}
    }

}