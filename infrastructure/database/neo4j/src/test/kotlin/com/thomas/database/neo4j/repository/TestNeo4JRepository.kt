package com.thomas.database.neo4j.repository

import com.thomas.core.extension.withSessionContextIO
import com.thomas.core.model.pagination.PageRequest
import com.thomas.core.model.pagination.PageResponse
import com.thomas.core.model.pagination.PageSort
import com.thomas.database.neo4j.filter.and
import com.thomas.database.neo4j.filter.between
import com.thomas.database.neo4j.filter.betweenEquals
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
import com.thomas.database.neo4j.node.BooleanPropsNode
import com.thomas.database.neo4j.node.ComplexPropsNode
import com.thomas.database.neo4j.node.DatetimePropsNode
import com.thomas.database.neo4j.node.GenericPropsNode
import com.thomas.database.neo4j.node.NumberPropsNode
import com.thomas.database.neo4j.node.PagePropsNode
import com.thomas.database.neo4j.node.SavePropsNode
import com.thomas.database.neo4j.node.StringPropsNode
import java.time.temporal.Temporal
import org.neo4j.ogm.cypher.Filter
import org.neo4j.ogm.cypher.Filters
import org.neo4j.ogm.cypher.query.SortOrder
import org.neo4j.ogm.session.SessionFactory

class TestNeo4JRepository(
    sessionFactory: SessionFactory,
) : Neo4JRepository(sessionFactory) {

    suspend fun stringEquals(name: String) = stringSearch(equals("prop_name", name))

    suspend fun stringNotEquals(name: String) = stringSearch(notEquals("prop_name", name))

    suspend fun stringEqualsUnaccentedLower(name: String) = stringSearch(equalsUnaccentedLower("prop_name", name))

    suspend fun stringNotEqualsUnaccentedLower(name: String) = stringSearch(notEqualsUnaccentedLower("prop_name", name))

    suspend fun stringLike(name: String) = stringSearch(like("prop_name", name))

    suspend fun stringNotLike(name: String) = stringSearch(notLike("prop_name", name))

    suspend fun stringLikeUnaccentedLower(name: String) = stringSearch(likeUnaccentedLower("prop_name", name))

    suspend fun stringNotLikeUnaccentedLower(name: String) = stringSearch(notLikeUnaccentedLower("prop_name", name))

    suspend fun stringStartsWith(name: String) = stringSearch(startsWith("prop_name", name))

    suspend fun stringNotStartsWith(name: String) = stringSearch(notStartsWith("prop_name", name))

    suspend fun stringStartsWithUnaccentedLower(name: String) = stringSearch(startsWithUnaccentedLower("prop_name", name))

    suspend fun stringNotStartsWithUnaccentedLower(name: String) = stringSearch(notStartsWithUnaccentedLower("prop_name", name))

    suspend fun stringEndsWith(name: String) = stringSearch(endsWith("prop_name", name))

    suspend fun stringNotEndsWith(name: String) = stringSearch(notEndsWith("prop_name", name))

    suspend fun stringEndsWithUnaccentedLower(name: String) = stringSearch(endsWithUnaccentedLower("prop_name", name))

    suspend fun stringNotEndsWithUnaccentedLower(name: String) = stringSearch(notEndsWithUnaccentedLower("prop_name", name))

    suspend fun numberGreaterThan(property: String, value: Number) = numberSearch(greaterThan(property, value))

    suspend fun numberGreaterThanEquals(property: String, value: Number) = numberSearch(greaterThanEquals(property, value))

    suspend fun numberLessThan(property: String, value: Number) = numberSearch(lessThan(property, value))

    suspend fun numberLessThanEquals(property: String, value: Number) = numberSearch(lessThanEquals(property, value))

    suspend fun numberBetween(property: String, min: Number, max: Number) = numberSearch(between(property, min, max))

    suspend fun numberBetweenEquals(property: String, min: Number, max: Number) = numberSearch(betweenEquals(property, min, max))

    suspend fun numberNotBetween(property: String, min: Number, max: Number) = numberSearch(notBetween(property, min, max))

    suspend fun numberNotBetweenEquals(property: String, min: Number, max: Number) = numberSearch(notBetweenEquals(property, min, max))

    suspend fun datetimeGreaterThan(property: String, value: Temporal) = datetimeSearch(greaterThan(property, value))

    suspend fun datetimeGreaterThanEquals(property: String, value: Temporal) = datetimeSearch(greaterThanEquals(property, value))

    suspend fun datetimeLessThan(property: String, value: Temporal) = datetimeSearch(lessThan(property, value))

    suspend fun datetimeLessThanEquals(property: String, value: Temporal) = datetimeSearch(lessThanEquals(property, value))

    suspend fun datetimeBetween(property: String, min: Temporal, max: Temporal) = datetimeSearch(between(property, min, max))

    suspend fun datetimeBetweenEquals(property: String, min: Temporal, max: Temporal) = datetimeSearch(betweenEquals(property, min, max))

    suspend fun datetimeNotBetween(property: String, min: Temporal, max: Temporal) = datetimeSearch(notBetween(property, min, max))

    suspend fun datetimeNotBetweenEquals(property: String, min: Temporal, max: Temporal) = datetimeSearch(notBetweenEquals(property, min, max))

    suspend fun booleanIsTrue(property: String) = booleanSearch(isTrue(property))

    suspend fun booleanIsFalse(property: String) = booleanSearch(isFalse(property))

    suspend fun propertyIsNull(property: String) = propertySearch(isNull(property))

    suspend fun propertyIsNotNull(property: String) = propertySearch(isNotNull(property))

    suspend fun propertyInValues(property: String, values: Collection<Any>) = propertySearch(inValues(property, values))

    suspend fun propertyNotInValues(property: String, values: Collection<Any>) = propertySearch(notInValues(property, values))

    suspend fun complexFilterSearch(filters: List<Filter>) = complexSearch(and(filters))

    suspend fun sortPageSearch(sort: PageSort): List<PagePropsNode> = pageSearch(sort.toSortOrder()).toList()

    suspend fun sortPageSearch(sorts: List<PageSort>): List<PagePropsNode> = pageSearch(sorts.toSortOrder()).toList()

    suspend fun findById(id: String): PagePropsNode? = sessionFactory.one(id)

    suspend fun findList(filters: List<Filter>, sorts: List<PageSort>): List<PagePropsNode> = sessionFactory.list(filters, sorts)

    suspend fun findPage(filters: List<Filter>, pageable: PageRequest): PageResponse<PagePropsNode> = sessionFactory.page(filters, pageable)

    suspend fun saveData(entity: SavePropsNode): SavePropsNode = transaction { session ->
        entity.apply { session.save(this) }
    }

    suspend fun deleteData(entity: SavePropsNode) = transaction { session ->
        session.delete(entity)
    }

    private suspend fun stringSearch(
        filter: Filter
    ): List<StringPropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(StringPropsNode::class.java, filter)
            .toList()
    }

    private suspend fun numberSearch(
        filter: Filter
    ): List<NumberPropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(NumberPropsNode::class.java, filter)
            .toList()
    }

    private suspend fun numberSearch(
        filters: Filters
    ): List<NumberPropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(NumberPropsNode::class.java, filters)
            .toList()
    }

    private suspend fun datetimeSearch(
        filter: Filter
    ): List<DatetimePropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(DatetimePropsNode::class.java, filter)
            .toList()
    }

    private suspend fun datetimeSearch(
        filters: Filters
    ): List<DatetimePropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(DatetimePropsNode::class.java, filters)
            .toList()
    }

    private suspend fun booleanSearch(
        filter: Filter
    ): List<BooleanPropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(BooleanPropsNode::class.java, filter)
            .toList()
    }

    private suspend fun propertySearch(
        filter: Filter
    ): List<GenericPropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(GenericPropsNode::class.java, filter)
            .toList()
    }

    private suspend fun propertySearch(
        filters: Filters
    ): List<GenericPropsNode> = withSessionContextIO {
        sessionFactory.openSession()
            .loadAll(GenericPropsNode::class.java, filters)
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
