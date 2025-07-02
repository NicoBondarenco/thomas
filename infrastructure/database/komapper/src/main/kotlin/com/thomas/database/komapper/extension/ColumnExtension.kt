package com.thomas.database.komapper.extension

import kotlin.reflect.KType
import org.komapper.core.dsl.expression.ColumnExpression
import org.komapper.core.dsl.expression.Operand
import org.komapper.core.dsl.expression.SqlBuilderScope
import org.komapper.core.dsl.expression.TableExpression
import org.komapper.core.dsl.expression.UserDefinedExpression

class CustomFunctionExpression<T : Any>(
    expression: ColumnExpression<T, String>,
    override val name: String,
    override val operands: List<Operand>,
    private val build: SqlBuilderScope.() -> Unit,
) : UserDefinedExpression<T, String> {
    override val exteriorType: KType = expression.exteriorType
    override val interiorType: KType = expression.interiorType
    override val wrap: (String) -> T = expression.wrap
    override val unwrap: (T) -> String = expression.unwrap
    override val owner: TableExpression<*> = expression.owner
    override val columnName: String = expression.columnName
    override val alwaysQuote: Boolean = expression.alwaysQuote
    override val masking: Boolean = false
    override fun build(scope: SqlBuilderScope) {
        scope.build()
    }

}

fun <T : Any> unaccentLower(
    expression: ColumnExpression<T, String>,
): ColumnExpression<T, String> {
    val name = "\"public\".\"unaccented_lower\""
    val o1 = Operand.Column(expression)
    return CustomFunctionExpression(expression, name, listOf(o1)) {
        append("$name(")
        visit(o1)
        append(")")
    }
}
