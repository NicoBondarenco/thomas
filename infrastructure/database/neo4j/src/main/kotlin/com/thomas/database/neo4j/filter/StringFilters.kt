package com.thomas.database.neo4j.filter

import com.thomas.database.neo4j.function.GenericFilterFunction
import com.thomas.database.neo4j.operator.StringOperator.ENDS_WITH
import com.thomas.database.neo4j.operator.StringOperator.ENDS_WITH_UNACCENTED_LOWER
import com.thomas.database.neo4j.operator.StringOperator.EQUALS_UNACCENTED_LOWER
import com.thomas.database.neo4j.operator.StringOperator.LIKE
import com.thomas.database.neo4j.operator.StringOperator.LIKE_UNACCENTED_LOWER
import com.thomas.database.neo4j.operator.StringOperator.NOT_ENDS_WITH
import com.thomas.database.neo4j.operator.StringOperator.NOT_ENDS_WITH_UNACCENTED_LOWER
import com.thomas.database.neo4j.operator.StringOperator.NOT_EQUALS_UNACCENTED_LOWER
import com.thomas.database.neo4j.operator.StringOperator.NOT_LIKE
import com.thomas.database.neo4j.operator.StringOperator.NOT_LIKE_UNACCENTED_LOWER
import com.thomas.database.neo4j.operator.StringOperator.NOT_STARTS_WITH
import com.thomas.database.neo4j.operator.StringOperator.NOT_STARTS_WITH_UNACCENTED_LOWER
import com.thomas.database.neo4j.operator.StringOperator.STARTS_WITH
import com.thomas.database.neo4j.operator.StringOperator.STARTS_WITH_UNACCENTED_LOWER
import org.neo4j.ogm.cypher.Filter

fun equalsUnaccentedLower(
    property: String,
    value: String,
): Filter = Filter(property, GenericFilterFunction(value, EQUALS_UNACCENTED_LOWER))

fun notEqualsUnaccentedLower(
    property: String,
    value: String,
): Filter = Filter(property, GenericFilterFunction(value, NOT_EQUALS_UNACCENTED_LOWER))

fun like(
    property: String,
    value: String,
): Filter = Filter(property, GenericFilterFunction(value, LIKE))

fun notLike(
    property: String,
    value: String,
): Filter = Filter(property, GenericFilterFunction(value, NOT_LIKE))

fun likeUnaccentedLower(
    property: String,
    value: String,
): Filter = Filter(property, GenericFilterFunction(value, LIKE_UNACCENTED_LOWER))

fun notLikeUnaccentedLower(
    property: String,
    value: String,
): Filter = Filter(property, GenericFilterFunction(value, NOT_LIKE_UNACCENTED_LOWER))

fun startsWith(
    property: String,
    value: String,
): Filter = Filter(property, GenericFilterFunction(value, STARTS_WITH))

fun notStartsWith(
    property: String,
    value: String,
): Filter = Filter(property, GenericFilterFunction(value, NOT_STARTS_WITH))

fun startsWithUnaccentedLower(
    property: String,
    value: String,
): Filter = Filter(property, GenericFilterFunction(value, STARTS_WITH_UNACCENTED_LOWER))

fun notStartsWithUnaccentedLower(
    property: String,
    value: String,
): Filter = Filter(property, GenericFilterFunction(value, NOT_STARTS_WITH_UNACCENTED_LOWER))

fun endsWith(
    property: String,
    value: String,
): Filter = Filter(property, GenericFilterFunction(value, ENDS_WITH))

fun notEndsWith(
    property: String,
    value: String,
): Filter = Filter(property, GenericFilterFunction(value, NOT_ENDS_WITH))

fun endsWithUnaccentedLower(
    property: String,
    value: String,
): Filter = Filter(property, GenericFilterFunction(value, ENDS_WITH_UNACCENTED_LOWER))

fun notEndsWithUnaccentedLower(
    property: String,
    value: String,
): Filter = Filter(property, GenericFilterFunction(value, NOT_ENDS_WITH_UNACCENTED_LOWER))


