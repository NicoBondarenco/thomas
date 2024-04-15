package com.thomas.mongo.data

import com.mongodb.client.MongoDatabase
import com.thomas.mongo.repository.MongoRepository

class TestMongoRepository(
    database: MongoDatabase,
    collection: String
) : MongoRepository<TestMongoEntity>(
    database,
    collection,
    TestMongoEntity::class
) {


    /* private fun Document.toEntity() = TestMongoEntity(
         id = this.getUUID("id"),
         stringValue = this.getString("stringValue"),
         booleanValue = this.getBoolean("booleanValue"),
         intValue = this.getInteger("intValue"),
         longValue = this.getLong("longValue"),
         doubleValue = this.getDouble("doubleValue"),
         bigDecimal = this.getBigDecimal("bigDecimal"),
         bigInteger = this.getBigInteger("bigInteger"),
         dateValue = this.getLocalDate("dateValue"),
         timeValue = this.getLocalTime("timeValue"),
         datetimeValue = this.getLocalDateTime("datetimeValue"),
         datetimeOffset = this.getOffsetDateTime("datetimeOffset"),
         enumValue = this.getEnum<TestMongoEnum>("enumValue"),
         uuidEmpty = this.getUUIDOrNull("uuidEmpty"),
         stringEmpty = this.getStringOrNull("stringEmpty"),
         booleanEmpty = this.getBooleanOrNull("booleanEmpty"),
         intEmpty = this.getIntegerOrNull("intEmpty"),
         longEmpty = this.getLongOrNull("longEmpty"),
         doubleEmpty = this.getDoubleOrNull("doubleEmpty"),
         bigdecimalEmpty = this.getBigDecimalOrNull("bigdecimalEmpty"),
         bigintegerEmpty = this.getBigIntegerOrNull("bigintegerEmpty"),
         dateEmpty = this.getLocalDateOrNull("dateEmpty"),
         timeEmpty = this.getLocalTimeOrNull("timeEmpty"),
         datetimeEmpty = this.getLocalDateTimeOrNull("datetimeEmpty"),
         offsetEmpty = this.getOffsetDateTimeOrNull("offsetEmpty"),
         enumNull = this.getEnumOrNull<TestMongoEnum>("enumNull"),
     )*/

}
