package com.thomas.management.data.neo4j.generator

import com.fasterxml.jackson.annotation.JsonProperty

data class CompanyJson(
    @JsonProperty("razao_social")
    val razaoSocial: String,
    @JsonProperty("nome_fantasia")
    val nomeFantasia: String,
    @JsonProperty("cnpj")
    val cnpj: String,
    @JsonProperty("telefone")
    val telefone: String,
    @JsonProperty("celular")
    val celular: String,
    @JsonProperty("email")
    val email: String,
    @JsonProperty("cep")
    val cep: String,
    @JsonProperty("endereco")
    val endereco: String,
    @JsonProperty("bairro")
    val bairro: String,
    @JsonProperty("cidade")
    val cidade: String,
    @JsonProperty("estado")
    val estado: String,
)