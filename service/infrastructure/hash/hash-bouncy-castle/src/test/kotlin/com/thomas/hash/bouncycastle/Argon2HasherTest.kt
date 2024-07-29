package com.thomas.hash.bouncycastle

import com.thomas.hash.bouncycastle.properties.Argon2Properties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class Argon2HasherTest {

    companion object {

        interface HashValue {
            fun expected(length: Int): String
        }

        @JvmStatic
        fun hashValues(): List<Arguments> = listOf(
            Arguments.of(
                "Vk;1CV;1ps@.Mz,",
                "qve~J+]TYZS^Q]%ECNX2LOs",
                object : HashValue {
                    override fun expected(length: Int) = if (length == 32) {
                        "hvYQmwvM1aio22j1z8MACQgwxSzoBtzgpNqO6QcOi3c="
                    } else {
                        "QP8VSbZgl6FGFgJb+NtSJIIuyjWonQZJXMoKtQSDN79Df7T4i8/3pptK+1uDcaP9yqljJ7t7shREPliyKx4q5A=="
                    }
                },
            ),
            Arguments.of(
                "a1840699-a296-4e33-9d8d-5b77cece6f3f",
                "cf75aefa-3698-4367-b541-1e588dc06655",
                object : HashValue {
                    override fun expected(length: Int) = if (length == 32) {
                        "yEGv/tGNZxxlT30uafCTRAKljHBk1ycHL5LdkBXZ+sA="
                    } else {
                        "n4CBomCqZ9D8BgwHGnVzF26rHCl7NEUaYLZd+ORq2DOG0C2rAnc/OjnEsJm5+WUJY65paOfFkUfR+tza9ZRt1A=="
                    }
                },
            ),
            Arguments.of(
                "01J3NHW14VFC9VYBJ0XD88DXX1",
                "clz1lktgz0000ow586whxpfq2",
                object : HashValue {
                    override fun expected(length: Int) = if (length == 32) {
                        "h+9Xt2RBg8rry22j5EaOUhU8R6uRpRrhfabj1FHmRvM="
                    } else {
                        "KVt2ZVbRaAkBzwsfANqG1hIRPpLDBEjJRp7/dqZZ8jbZCKz7BzCpUmfneiJxi1At+BMw9DbjNTmapEwI5LwxuQ=="
                    }
                },
            ),
            Arguments.of(
                "SfC6[j5Z'wBsRi'^duCYS3YiJ1YZ%tl@H6LxD765jSYS.b%7d\$",
                "t]^=79gam\$Hx{sdmkdH_",
                object : HashValue {
                    override fun expected(length: Int) = if (length == 32) {
                        "lYhDmYBynsFRtnVa6pLFwjVrgr93sps5OLa8+6r0tak="
                    } else {
                        "GkHam8n72PKYK1Nx0iq7yGFdNrY9aLptAZHjQ3kJoGFHFiZ7x0WZH7LJPdpf0o8BTzU4I8NjzZMUQ1c+1Q1Iyw=="
                    }
                },
            ),
            Arguments.of(
                "aVq=l",
                "i.JQ!",
                object : HashValue {
                    override fun expected(length: Int) = if (length == 32) {
                        "XZB/jjqmfkaaLCdof/kFoER5JnSuhWUB6/dhk1Lmpws="
                    } else {
                        "qiZEfGdl4/UfqT7epbj+ljpmSy8SxV8X8sJKvWUi6zhjbd0hh93ehm92dt/RTaumgP0U+dXKFv5plbPITpf7FQ=="
                    }
                },
            ),
            Arguments.of(
                "9+I@S1YPjZ-TA\$nGo~JL^df9_J6MoXo-53)}cdS%)#xsQ75KIb",
                "}r\$S]1y!)aHGwhb_ksmhr\$BE3pXnmKcAN^4VMKKk08^1!f8ntj",
                object : HashValue {
                    override fun expected(length: Int) = if (length == 32) {
                        "EILUPYjyOQFWKWPPGQezTS+1GD1zoA0tmg1upcPmHSs="
                    } else {
                        "uZ7Qc8UyAxjyp6N6xk0kKwB1KGKM2BomvJyLqz666fVZVLn+vrxUv6UzWjEAAUGbkExnl1DNZxN7UJtBbfww9w=="
                    }
                },
            ),
            Arguments.of(
                "@6iG\$yz4cA30?e=]£6£-DNP,tO/Z\"£",
                "00635411698402197806510967809",
                object : HashValue {
                    override fun expected(length: Int) = if (length == 32) {
                        "Zjw/Nqf1JezLjaN4Zv+i70hS/viFSaxmrnRUfH5b62A="
                    } else {
                        "IKQPrnyj2Suhkj0fOcr4OfNa8IDSUi+0DTmNeK2Nqu8kjV2ZlyXp0V9MWbvlfjRHG5hC2cfyMu7c+XnmYifDdA=="
                    }
                },
            ),
            Arguments.of(
                "621065407890849089847098490849",
                "p7bQ2e<x#=QH6E5",
                object : HashValue {
                    override fun expected(length: Int) = if (length == 32) {
                        "+iZT1XyZVkfz6lV8TNQEfMvEciDs1SVsYgIPixng1SQ="
                    } else {
                        "X8vGSWKGJaj51TAGsofRrDbwIaPrFINjFNO2iTG/68ZehGMgW0nnhzJggH2El3FJjL3IpUAL5hs9IEy96s9bqg=="
                    }
                },
            ),
            Arguments.of(
                "IASOIHPUIIAUENFEAPQWQPOJNVYFA",
                "E",
                object : HashValue {
                    override fun expected(length: Int) = if (length == 32) {
                        "KwJdpz4klC/KIwySLYlMk5J732NoeWsrfmNvP+QA5Eg="
                    } else {
                        "2QsgmnmkVeIXUIht6obwpBh1owDKUq37uoAyRuDLcum85dZyaBvelFVbzEbuBnFYD9mCSMbxEaoZL/cATj+Imw=="
                    }
                },
            ),
            Arguments.of(
                "A",
                "cd31e8cd48ad41f1875857e85a5fe536",
                object : HashValue {
                    override fun expected(length: Int) = if (length == 32) {
                        "vJ+BmBZTyjPMd4sFH+/l2W+/pDSMAG+PNBS+7T8QqR4="
                    } else {
                        "N9BPDX6COljQcYTLRThShhqJlahYYzertGSns8ax7gAlUHcJzBJ8mqvtiL9g2WJRWXNhLyOd6Kxeooa8lGlffA=="
                    }
                },
            ),
        )
    }

    private val propertiesCommon = Argon2Properties(
        pepperHash = "M!8\$Y-RUGbKA%zT_6EnPP,e6@",
    )

    private val propertiesAlt = Argon2Properties(
        pepperHash = "Te8E!4QtdJFn(A=WB8m&a~vZ!",
        iterationsCount = 3,
        memoryLimit = 66536,
        hashLength = 32,
        parallelismCount = 2,
    )

    private fun hasher(properties: Argon2Properties = propertiesCommon) = Argon2Hasher(properties)

    @ParameterizedTest
    @MethodSource("hashValues")
    fun `Hash Argon2 value should returns the same value`(value: String, salt: String, expected: HashValue) {
        val hasher = hasher()
        val hashed = hasher.hash(value, salt)
        assertEquals(expected.expected(propertiesCommon.hashLength), hashed)

        val hasherVerifier = hasher()
        val hashedVerifier = hasherVerifier.hash(value, salt)
        assertEquals(expected.expected(propertiesCommon.hashLength), hashedVerifier)
        assertEquals(hashed, hashedVerifier)
    }

    @ParameterizedTest
    @MethodSource("hashValues")
    fun `Hash Argon2 value should returns the same value alt properties`(value: String, salt: String, expected: HashValue) {
        val hasher = hasher(propertiesAlt)
        val hashed = hasher.hash(value, salt)
        assertEquals(expected.expected(propertiesAlt.hashLength), hashed)

        val hasherVerifier = hasher(propertiesAlt)
        val hashedVerifier = hasherVerifier.hash(value, salt)
        assertEquals(expected.expected(propertiesAlt.hashLength), hashedVerifier)
        assertEquals(hashed, hashedVerifier)
    }

    @Test
    fun `Should generate valid salt`() {
        val hasher = hasher()
        repeat((1..1000).count()) {
            val salt = hasher.generateSalt()
            assertEquals(propertiesCommon.hashLength, salt.toByteArray().size)
        }
    }

    @Test
    fun `Should generate valid salt alt properties`() {
        val hasher = hasher(propertiesAlt)
        repeat((1..1000).count()) {
            val salt = hasher.generateSalt()
            assertEquals(propertiesAlt.hashLength, salt.toByteArray().size)
        }
    }

}
