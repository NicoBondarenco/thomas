package com.thomas.database.neo4j.repository

import com.thomas.core.extension.withSessionContextIO
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.core.model.pagination.PageSort
import com.thomas.database.neo4j.filter.and
import com.thomas.database.neo4j.filter.between
import com.thomas.database.neo4j.filter.betweenEquals
import com.thomas.database.neo4j.filter.count
import com.thomas.database.neo4j.filter.endsWith
import com.thomas.database.neo4j.filter.endsWithUnaccentedLower
import com.thomas.database.neo4j.filter.equals
import com.thomas.database.neo4j.filter.equalsUnaccentedLower
import com.thomas.database.neo4j.filter.greaterThan
import com.thomas.database.neo4j.filter.greaterThanEquals
import com.thomas.database.neo4j.filter.inValues
import com.thomas.database.neo4j.filter.isFalse
import com.thomas.database.neo4j.filter.isNotNull
import com.thomas.database.neo4j.filter.isNull
import com.thomas.database.neo4j.filter.isTrue
import com.thomas.database.neo4j.filter.lessThan
import com.thomas.database.neo4j.filter.lessThanEquals
import com.thomas.database.neo4j.filter.like
import com.thomas.database.neo4j.filter.likeUnaccentedLower
import com.thomas.database.neo4j.filter.list
import com.thomas.database.neo4j.filter.notBetween
import com.thomas.database.neo4j.filter.notBetweenEquals
import com.thomas.database.neo4j.filter.notEndsWith
import com.thomas.database.neo4j.filter.notEndsWithUnaccentedLower
import com.thomas.database.neo4j.filter.notEquals
import com.thomas.database.neo4j.filter.notEqualsUnaccentedLower
import com.thomas.database.neo4j.filter.notInValues
import com.thomas.database.neo4j.filter.notLike
import com.thomas.database.neo4j.filter.notLikeUnaccentedLower
import com.thomas.database.neo4j.filter.notStartsWith
import com.thomas.database.neo4j.filter.notStartsWithUnaccentedLower
import com.thomas.database.neo4j.filter.one
import com.thomas.database.neo4j.filter.page
import com.thomas.database.neo4j.filter.startsWith
import com.thomas.database.neo4j.filter.startsWithUnaccentedLower
import com.thomas.database.neo4j.filter.toSortOrder
import com.thomas.database.neo4j.node.BooleanNestedPropsNode
import com.thomas.database.neo4j.node.BooleanPropsNode
import com.thomas.database.neo4j.node.ComplexPropsNode
import com.thomas.database.neo4j.node.DatetimeNestedPropsNode
import com.thomas.database.neo4j.node.DatetimePropsNode
import com.thomas.database.neo4j.node.GenericNestedPropsNode
import com.thomas.database.neo4j.node.GenericPropsNode
import com.thomas.database.neo4j.node.Neo4JNode
import com.thomas.database.neo4j.node.NumberNestedPropsNode
import com.thomas.database.neo4j.node.NumberPropsNode
import com.thomas.database.neo4j.node.PagePropsNode
import com.thomas.database.neo4j.node.SavePropsNode
import com.thomas.database.neo4j.node.StringNestedPropsNode
import com.thomas.database.neo4j.node.StringPropsNode
import java.time.temporal.Temporal
import java.util.UUID
import kotlin.reflect.KProperty
import org.neo4j.ogm.cypher.Filter
import org.neo4j.ogm.cypher.Filters
import org.neo4j.ogm.cypher.query.SortOrder
import org.neo4j.ogm.session.SessionFactory

class TestNeo4JRepository(
    sessionFactory: SessionFactory,
) : Neo4JRepository(sessionFactory) {

    //region BOOLEAN PROPS

    suspend fun <T : Any> booleanIsTrue(
        property: KProperty<T>
    ): List<BooleanPropsNode> = booleanSearch(isTrue(property))

    suspend fun <T : Any> booleanIsFalse(
        property: KProperty<T>
    ): List<BooleanPropsNode> = booleanSearch(isFalse(property))

    //endregion BOOLEAN PROPS

    //region BOOLEAN NESTED PROPS

    suspend fun <K : Any, T : Neo4JNode> booleanNestedIs(
        propName: KProperty<K>,
        propValue: Boolean,
        nestedName: String,
        nestedProp: KProperty<T>,
        nestedValue: Boolean
    ): List<BooleanNestedPropsNode> = booleanNestedSearch(
        (propValue.takeIf { it }?.let { isTrue(propName) } ?: isFalse(propName))
            .and(nestedValue.takeIf { it }?.let {
                isTrue(nestedName, nestedProp)
            } ?: isFalse(nestedName, nestedProp))
    )

    //endregion BOOLEAN NESTED PROPS

    //region DATETIME PROPS

    suspend fun <T : Any> datetimeGreaterThan(
        property: KProperty<T?>,
        value: Temporal
    ): List<DatetimePropsNode> = datetimeSearch(greaterThan(property, value))

    suspend fun <T : Any> datetimeGreaterThanEquals(
        property: KProperty<T?>,
        value: Temporal
    ): List<DatetimePropsNode> = datetimeSearch(greaterThanEquals(property, value))

    suspend fun <T : Any> datetimeLessThan(
        property: KProperty<T?>,
        value: Temporal
    ): List<DatetimePropsNode> = datetimeSearch(lessThan(property, value))

    suspend fun <T : Any> datetimeLessThanEquals(
        property: KProperty<T?>,
        value: Temporal
    ): List<DatetimePropsNode> = datetimeSearch(lessThanEquals(property, value))

    suspend fun <T : Any> datetimeBetween(
        property: KProperty<T?>,
        min: Temporal,
        max: Temporal
    ): List<DatetimePropsNode> = datetimeSearch(between(property, min, max))

    suspend fun <T : Any> datetimeBetweenEquals(
        property: KProperty<T?>,
        min: Temporal,
        max: Temporal
    ): List<DatetimePropsNode> = datetimeSearch(betweenEquals(property, min, max))

    suspend fun <T : Any> datetimeNotBetween(
        property: KProperty<T?>,
        min: Temporal,
        max: Temporal
    ): List<DatetimePropsNode> = datetimeSearch(notBetween(property, min, max))

    suspend fun <T : Any> datetimeNotBetweenEquals(
        property: KProperty<T?>,
        min: Temporal,
        max: Temporal
    ): List<DatetimePropsNode> = datetimeSearch(notBetweenEquals(property, min, max))

    //endregion DATETIME PROPS

    //region DATETIME NESTED PROPS

    suspend fun <K : Any> datetimeNestedGreaterThan(
        property: KProperty<K?>,
        value: Temporal
    ): List<DatetimeNestedPropsNode> = datetimeNestedSearch(greaterThan(property, DatetimeNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> datetimeNestedGreaterThanEquals(
        property: KProperty<K?>,
        value: Temporal
    ): List<DatetimeNestedPropsNode> = datetimeNestedSearch(greaterThanEquals(property, DatetimeNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> datetimeNestedLessThan(
        property: KProperty<K?>,
        value: Temporal
    ): List<DatetimeNestedPropsNode> = datetimeNestedSearch(lessThan(property, DatetimeNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> datetimeNestedLessThanEquals(
        property: KProperty<K?>,
        value: Temporal
    ): List<DatetimeNestedPropsNode> = datetimeNestedSearch(lessThanEquals(property, DatetimeNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> datetimeNestedBetween(
        property: KProperty<K?>,
        min: Temporal,
        max: Temporal
    ): List<DatetimeNestedPropsNode> = datetimeNestedSearch(between(property, DatetimeNestedPropsNode::nestedNode, min, max))

    suspend fun <K : Any> datetimeNestedBetweenEquals(
        property: KProperty<K?>,
        min: Temporal,
        max: Temporal
    ): List<DatetimeNestedPropsNode> = datetimeNestedSearch(betweenEquals(property, DatetimeNestedPropsNode::nestedNode, min, max))

    suspend fun <K : Any> datetimeNestedNotBetween(
        property: KProperty<K?>,
        min: Temporal,
        max: Temporal
    ): List<DatetimeNestedPropsNode> = datetimeNestedSearch(notBetween(property, DatetimeNestedPropsNode::nestedNode, min, max))

    suspend fun <K : Any> datetimeNestedNotBetweenEquals(
        property: KProperty<K?>,
        min: Temporal,
        max: Temporal
    ): List<DatetimeNestedPropsNode> = datetimeNestedSearch(notBetweenEquals(property, DatetimeNestedPropsNode::nestedNode, min, max))

    //endregion DATETIME NESTED PROPS

    //region GENERICS

    suspend fun <K : Any> propertyIsNull(
        property: KProperty<K?>
    ): List<GenericPropsNode> = propertySearch(isNull(property))

    suspend fun <K : Any> propertyIsNotNull(
        property: KProperty<K?>
    ): List<GenericPropsNode> = propertySearch(isNotNull(property))

    suspend fun <K : Any> propertyInValues(
        property: KProperty<K?>,
        values: Collection<Any>
    ): List<GenericPropsNode> = propertySearch(inValues(property, values))

    suspend fun <K : Any> propertyNotInValues(
        property: KProperty<K?>,
        values: Collection<Any>
    ): List<GenericPropsNode> = propertySearch(notInValues(property, values))

    //endregion GENERICS

    //region GENERICS NESTED

    suspend fun <K : Any> propertyIsNullNested(
        property: KProperty<K?>
    ): List<GenericNestedPropsNode> = propertySearchNested(isNull(property, GenericNestedPropsNode::nestedNode))

    suspend fun <K : Any> propertyIsNotNullNested(
        property: KProperty<K?>
    ): List<GenericNestedPropsNode> = propertySearchNested(isNotNull(property, GenericNestedPropsNode::nestedNode))

    suspend fun <K : Any> propertyInValuesNested(
        property: KProperty<K?>,
        values: Collection<Any>
    ): List<GenericNestedPropsNode> = propertySearchNested(inValues(property, GenericNestedPropsNode::nestedNode, values))

    suspend fun <K : Any> propertyNotInValuesNested(
        property: KProperty<K?>,
        values: Collection<Any>
    ): List<GenericNestedPropsNode> = propertySearchNested(notInValues(property, GenericNestedPropsNode::nestedNode, values))

    //endregion GENERICS NESTED

    //region NUMBER

    suspend fun <K : Any> numberGreaterThan(
        property: KProperty<K?>,
        value: Number
    ): List<NumberPropsNode> = numberSearch(greaterThan(property, value))

    suspend fun <K : Any> numberGreaterThanEquals(
        property: KProperty<K?>,
        value: Number
    ): List<NumberPropsNode> = numberSearch(greaterThanEquals(property, value))

    suspend fun <K : Any> numberLessThan(
        property: KProperty<K?>,
        value: Number
    ): List<NumberPropsNode> = numberSearch(lessThan(property, value))

    suspend fun <K : Any> numberLessThanEquals(
        property: KProperty<K?>,
        value: Number
    ): List<NumberPropsNode> = numberSearch(lessThanEquals(property, value))

    suspend fun <K : Any> numberBetween(
        property: KProperty<K?>,
        min: Number,
        max: Number
    ): List<NumberPropsNode> = numberSearch(between(property, min, max))

    suspend fun <K : Any> numberBetweenEquals(
        property: KProperty<K?>,
        min: Number,
        max: Number
    ): List<NumberPropsNode> = numberSearch(betweenEquals(property, min, max))

    suspend fun <K : Any> numberNotBetween(
        property: KProperty<K?>,
        min: Number,
        max: Number
    ): List<NumberPropsNode> = numberSearch(notBetween(property, min, max))

    suspend fun <K : Any> numberNotBetweenEquals(
        property: KProperty<K?>,
        min: Number,
        max: Number
    ): List<NumberPropsNode> = numberSearch(notBetweenEquals(property, min, max))

    //endregion NUMBER

    //region NUMBER NESTED

    suspend fun <K : Any> numberGreaterThanNested(
        property: KProperty<K?>,
        value: Number
    ): List<NumberNestedPropsNode> = numberSearchNested(greaterThan(property, NumberNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> numberGreaterThanEqualsNested(
        property: KProperty<K?>,
        value: Number
    ): List<NumberNestedPropsNode> = numberSearchNested(greaterThanEquals(property, NumberNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> numberLessThanNested(
        property: KProperty<K?>,
        value: Number
    ): List<NumberNestedPropsNode> = numberSearchNested(lessThan(property, NumberNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> numberLessThanEqualsNested(
        property: KProperty<K?>,
        value: Number
    ): List<NumberNestedPropsNode> = numberSearchNested(lessThanEquals(property, NumberNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> numberBetweenNested(
        property: KProperty<K?>,
        min: Number,
        max: Number
    ): List<NumberNestedPropsNode> = numberSearchNested(between(property, NumberNestedPropsNode::nestedNode, min, max))

    suspend fun <K : Any> numberBetweenEqualsNested(
        property: KProperty<K?>,
        min: Number,
        max: Number
    ): List<NumberNestedPropsNode> = numberSearchNested(betweenEquals(property, NumberNestedPropsNode::nestedNode, min, max))

    suspend fun <K : Any> numberNotBetweenNested(
        property: KProperty<K?>,
        min: Number,
        max: Number
    ): List<NumberNestedPropsNode> = numberSearchNested(notBetween(property, NumberNestedPropsNode::nestedNode, min, max))

    suspend fun <K : Any> numberNotBetweenEqualsNested(
        property: KProperty<K?>,
        min: Number,
        max: Number
    ): List<NumberNestedPropsNode> = numberSearchNested(notBetweenEquals(property, NumberNestedPropsNode::nestedNode, min, max))

    //endregion NUMBER NESTED

    //region STRING

    suspend fun <K : Any> stringEquals(
        property: KProperty<K?>,
        value: String
    ): List<StringPropsNode> = stringSearch(equals(property, value))

    suspend fun <K : Any> stringNotEquals(
        property: KProperty<K?>,
        value: String
    ): List<StringPropsNode> = stringSearch(notEquals(property, value))

    suspend fun <K : Any> stringEqualsUnaccentedLower(
        property: KProperty<K?>,
        value: String
    ): List<StringPropsNode> = stringSearch(equalsUnaccentedLower(property, value))

    suspend fun <K : Any> stringNotEqualsUnaccentedLower(
        property: KProperty<K?>,
        value: String
    ): List<StringPropsNode> = stringSearch(notEqualsUnaccentedLower(property, value))

    suspend fun <K : Any> stringLike(
        property: KProperty<K?>,
        value: String
    ): List<StringPropsNode> = stringSearch(like(property, value))

    suspend fun <K : Any> stringNotLike(
        property: KProperty<K?>,
        value: String
    ): List<StringPropsNode> = stringSearch(notLike(property, value))

    suspend fun <K : Any> stringLikeUnaccentedLower(
        property: KProperty<K?>,
        value: String
    ): List<StringPropsNode> = stringSearch(likeUnaccentedLower(property, value))

    suspend fun <K : Any> stringNotLikeUnaccentedLower(
        property: KProperty<K?>,
        value: String
    ): List<StringPropsNode> = stringSearch(notLikeUnaccentedLower(property, value))

    suspend fun <K : Any> stringStartsWith(
        property: KProperty<K?>,
        value: String
    ): List<StringPropsNode> = stringSearch(startsWith(property, value))

    suspend fun <K : Any> stringNotStartsWith(
        property: KProperty<K?>,
        value: String
    ): List<StringPropsNode> = stringSearch(notStartsWith(property, value))

    suspend fun <K : Any> stringStartsWithUnaccentedLower(
        property: KProperty<K?>,
        value: String
    ): List<StringPropsNode> = stringSearch(startsWithUnaccentedLower(property, value))

    suspend fun <K : Any> stringNotStartsWithUnaccentedLower(
        property: KProperty<K?>,
        value: String
    ): List<StringPropsNode> = stringSearch(notStartsWithUnaccentedLower(property, value))

    suspend fun <K : Any> stringEndsWith(
        property: KProperty<K?>,
        value: String
    ): List<StringPropsNode> = stringSearch(endsWith(property, value))

    suspend fun <K : Any> stringNotEndsWith(
        property: KProperty<K?>,
        value: String
    ): List<StringPropsNode> = stringSearch(notEndsWith(property, value))

    suspend fun <K : Any> stringEndsWithUnaccentedLower(
        property: KProperty<K?>,
        value: String
    ): List<StringPropsNode> = stringSearch(endsWithUnaccentedLower(property, value))

    suspend fun <K : Any> stringNotEndsWithUnaccentedLower(
        property: KProperty<K?>,
        value: String
    ): List<StringPropsNode> = stringSearch(notEndsWithUnaccentedLower(property, value))

    //endregion STRING

    //region STRING NESTED

    suspend fun <K : Any> stringEqualsNested(
        property: KProperty<K?>,
        value: String
    ): List<StringNestedPropsNode> = stringSearchNested(equals(property, StringNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> stringNotEqualsNested(
        property: KProperty<K?>,
        value: String
    ): List<StringNestedPropsNode> = stringSearchNested(notEquals(property, StringNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> stringEqualsUnaccentedLowerNested(
        property: KProperty<K?>,
        value: String
    ): List<StringNestedPropsNode> = stringSearchNested(equalsUnaccentedLower(property, StringNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> stringNotEqualsUnaccentedLowerNested(
        property: KProperty<K?>,
        value: String
    ): List<StringNestedPropsNode> = stringSearchNested(notEqualsUnaccentedLower(property, StringNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> stringLikeNested(
        property: KProperty<K?>,
        value: String
    ): List<StringNestedPropsNode> = stringSearchNested(like(property, StringNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> stringNotLikeNested(
        property: KProperty<K?>,
        value: String
    ): List<StringNestedPropsNode> = stringSearchNested(notLike(property, StringNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> stringLikeUnaccentedLowerNested(
        property: KProperty<K?>,
        value: String
    ): List<StringNestedPropsNode> = stringSearchNested(likeUnaccentedLower(property, StringNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> stringNotLikeUnaccentedLowerNested(
        property: KProperty<K?>,
        value: String
    ): List<StringNestedPropsNode> = stringSearchNested(notLikeUnaccentedLower(property, StringNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> stringStartsWithNested(
        property: KProperty<K?>,
        value: String
    ): List<StringNestedPropsNode> = stringSearchNested(startsWith(property, StringNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> stringNotStartsWithNested(
        property: KProperty<K?>,
        value: String
    ): List<StringNestedPropsNode> = stringSearchNested(notStartsWith(property, StringNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> stringStartsWithUnaccentedLowerNested(
        property: KProperty<K?>,
        value: String
    ): List<StringNestedPropsNode> = stringSearchNested(startsWithUnaccentedLower(property, StringNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> stringNotStartsWithUnaccentedLowerNested(
        property: KProperty<K?>,
        value: String
    ): List<StringNestedPropsNode> = stringSearchNested(notStartsWithUnaccentedLower(property, StringNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> stringEndsWithNested(
        property: KProperty<K?>,
        value: String
    ): List<StringNestedPropsNode> = stringSearchNested(endsWith(property, StringNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> stringNotEndsWithNested(
        property: KProperty<K?>,
        value: String
    ): List<StringNestedPropsNode> = stringSearchNested(notEndsWith(property, StringNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> stringEndsWithUnaccentedLowerNested(
        property: KProperty<K?>,
        value: String
    ): List<StringNestedPropsNode> = stringSearchNested(endsWithUnaccentedLower(property, StringNestedPropsNode::nestedNode, value))

    suspend fun <K : Any> stringNotEndsWithUnaccentedLowerNested(
        property: KProperty<K?>,
        value: String
    ): List<StringNestedPropsNode> = stringSearchNested(notEndsWithUnaccentedLower(property, StringNestedPropsNode::nestedNode, value))

    //endregion STRING NESTED

    suspend fun complexFilterSearch(filters: List<Filter>) = complexSearch(and(filters))

    suspend fun sortPageSearch(sort: PageSort): List<PagePropsNode> = pageSearch(sort.toSortOrder()).toList()

    suspend fun sortPageSearch(sorts: List<PageSort>): List<PagePropsNode> = pageSearch(sorts.toSortOrder()).toList()

    suspend fun findById(id: UUID): PagePropsNode? = sessionFactory.one(id)

    suspend fun findList(filters: List<Filter>, sorts: List<PageSort>): List<PagePropsNode> = sessionFactory.list(filters, sorts)

    suspend fun countTotal(filters: List<Filter>): Long = sessionFactory.count<PagePropsNode>(filters)

    suspend fun findPage(filters: List<Filter>, pageable: PageRequest): PageResponse<PagePropsNode> = sessionFactory.page(filters, pageable)

    suspend fun saveData(entity: SavePropsNode): SavePropsNode = transaction { session ->
        entity.apply { session.save(this) }
    }

    suspend fun deleteData(entity: SavePropsNode) = transaction { session ->
        session.delete(entity)
    }

    private suspend fun booleanSearch(
        filter: Filter
    ): List<BooleanPropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(BooleanPropsNode::class.java, filter)
            .toList()
    }

    private suspend fun booleanNestedSearch(
        filter: Filters
    ): List<BooleanNestedPropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(BooleanNestedPropsNode::class.java, filter)
            .toList()
    }

    private suspend fun datetimeSearch(
        filter: Filter
    ): List<DatetimePropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(DatetimePropsNode::class.java, filter)
            .toList()
    }

    private suspend fun datetimeNestedSearch(
        filter: Filter
    ): List<DatetimeNestedPropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(DatetimeNestedPropsNode::class.java, filter)
            .toList()
    }

    private suspend fun propertySearch(
        filter: Filter
    ): List<GenericPropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(GenericPropsNode::class.java, filter)
            .toList()
    }

    private suspend fun propertySearchNested(
        filter: Filter
    ): List<GenericNestedPropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(GenericNestedPropsNode::class.java, filter)
            .toList()
    }

    private suspend fun numberSearch(
        filter: Filter
    ): List<NumberPropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(NumberPropsNode::class.java, filter)
            .toList()
    }

    private suspend fun numberSearchNested(
        filter: Filter
    ): List<NumberNestedPropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(NumberNestedPropsNode::class.java, filter)
            .toList()
    }

    private suspend fun stringSearch(
        filter: Filter
    ): List<StringPropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(StringPropsNode::class.java, filter)
            .toList()
    }

    private suspend fun stringSearchNested(
        filter: Filter
    ): List<StringNestedPropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(StringNestedPropsNode::class.java, filter)
            .toList()
    }

    private suspend fun complexSearch(
        filters: Filters
    ): List<ComplexPropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(ComplexPropsNode::class.java, filters)
            .toList()
    }

    private suspend fun pageSearch(
        sort: SortOrder
    ) = withSessionContextIO {
        sessionFactory.openSession().loadAll(PagePropsNode::class.java, sort)
    }

}
