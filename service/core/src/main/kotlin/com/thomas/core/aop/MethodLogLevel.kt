package com.thomas.core.aop

import org.apache.logging.log4j.Level

enum class MethodLogLevel(val level: Level) {

    FATAL(Level.FATAL),
    ERROR(Level.ERROR),
    WARN(Level.WARN),
    INFO(Level.INFO),
    DEBUG(Level.DEBUG),
    TRACE(Level.TRACE),

}