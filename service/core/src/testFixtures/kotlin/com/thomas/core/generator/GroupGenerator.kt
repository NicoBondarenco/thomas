package com.thomas.core.generator

import com.thomas.core.data.GroupTestData
import kotlin.random.Random

object GroupGenerator {

    private val groupNamesDescriptions = mapOf(
        "Administração" to listOf("Grupo do departamento de administração", null),
        "Recursos Humanos" to listOf("Setor dos recursos humanos", null),
        "Financeiro" to listOf("Departamento financeiro da empresa", null),
        "Contábil" to listOf("Setor contábil", null),
        "Marketing" to listOf("Grupo de marketing", null),
        "Vendas" to listOf("Setor de venda da produção da empresa", null),
        "Produção" to listOf("Produção de items da empresa", null),
        "Logística" to listOf("Departamento de logística", null),
        "Tecnologia da Informação" to listOf("Departamento de tecnologia da informação e afins", null),
        "Jurídico" to listOf("Setor legal e jurídico", null),
        "Pesquisa" to listOf("Setor de pesquisas empresariais", null),
        "Compras" to listOf("Departamento de compras externas", null),
        "Suprimentos" to listOf("Departamento de compras internas", null),
        "Atendimento ao Cliente" to listOf("SAC do cliente", null),
        "Manutenção" to listOf("Departamento de manutenções", null),
    )

    fun generate() = groupNamesDescriptions.entries.random().let {
        GroupTestData(
            groupName = "${it.key} - ${Random.nextInt(1000, 9999)}",
            groupDescription = it.value.random(),
            isActive = listOf(true, false).random(),
        )
    }

}
