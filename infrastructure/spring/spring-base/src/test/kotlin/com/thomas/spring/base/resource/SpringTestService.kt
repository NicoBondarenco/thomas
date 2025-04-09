package com.thomas.spring.base.resource

import com.thomas.core.aspect.MethodLog
import com.thomas.core.exception.DetailedException
import com.thomas.core.exception.ErrorType
import org.springframework.stereotype.Service

@Service
open class SpringTestService {

    @MethodLog
    open fun exceptionService(type: ErrorType) {
        throw object : DetailedException(
            message = "Exception occurred on ExceptionService - $type",
            type = type
        ) {}
    }

}
