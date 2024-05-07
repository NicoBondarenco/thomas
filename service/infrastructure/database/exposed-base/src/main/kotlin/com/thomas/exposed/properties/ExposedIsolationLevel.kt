package com.thomas.exposed.properties

enum class ExposedIsolationLevel(
    internal val isolationCode: Int
) {

    TRANSACTION_NONE(0),
    TRANSACTION_READ_UNCOMMITTED(1),
    TRANSACTION_READ_COMMITTED(2),
    TRANSACTION_REPEATABLE_READ(4),
    TRANSACTION_SERIALIZABLE(8),

}
