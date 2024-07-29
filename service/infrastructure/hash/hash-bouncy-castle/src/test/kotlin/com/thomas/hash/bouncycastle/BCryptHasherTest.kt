package com.thomas.hash.bouncycastle

import com.thomas.hash.bouncycastle.exception.BCryptHasherException
import com.thomas.hash.bouncycastle.properties.BCryptProperties
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class BCryptHasherTest {

    companion object {

        interface HashValue {
            fun expected(cost: Int): String
        }

        @JvmStatic
        fun hashValues(): List<Arguments> = listOf(
            Arguments.of(
                "Vk;1CV;1ps@.Mz,",
                "qe~J+]ZS^Q]%E2Ls",
                object : HashValue {
                    override fun expected(cost: Int) = if (cost == 4) {
                        "C9UgKmrJxvJCyUfm+QHWBS5J+fJvp2ga"
                    } else {
                        "ExnrZDL/WRzcDbxEJoDoqtBJbOIgHajd"
                    }
                },
            ),
            Arguments.of(
                "a1840699-a296-4e33-9d8d-5b77cece6f3f",
                "cf75aefa36984367",
                object : HashValue {
                    override fun expected(cost: Int) = if (cost == 4) {
                        "zvFVLFn63u6VJtq1hSnyM9xvTnR/cFoV"
                    } else {
                        "uvyiUbrTDUOqTPaq6BOJs+feUG582T3M"
                    }
                },
            ),
            Arguments.of(
                "01J3NHW14VFC9VYBJ0XD88DXX1",
                "z1lkgz0000o86whf",
                object : HashValue {
                    override fun expected(cost: Int) = if (cost == 4) {
                        "KNsW0tnv9D3WbO9rCr1sHZRWfyHsxWyv"
                    } else {
                        "6RHW3+9d/yI0Zk0WzVwIRV9uHFYz0oKy"
                    }
                },
            ),
            Arguments.of(
                "SfC6[j5Z'wBsRi'^duCYS3YiJ1YZ%tl@H6LxD765jSYS.b%7d\$",
                "t]^=79gam\$Hx{sH_",
                object : HashValue {
                    override fun expected(cost: Int) = if (cost == 4) {
                        "L5gB4w16xAubAu2e1V+6ZJOCUzplDsvH"
                    } else {
                        "aMnptCPx5H9RlKVPeX3wewMgExELYxrh"
                    }
                },
            ),
            Arguments.of(
                "aVq=l",
                "7uV71a-%QJ0<.n£",
                object : HashValue {
                    override fun expected(cost: Int) = if (cost == 4) {
                        "kmaErX3zPuOWQTwu8RDDGDOoh8jIiqCF"
                    } else {
                        "E2gOY6uJyPc2d9YJ3hnqcldwvVK4nJDP"
                    }
                },
            ),
            Arguments.of(
                "9+I@S1YPjZ-TA\$nGo~JL^df9_J6MoXo-53)}cdS%)#xsQ75KIb",
                "3Kt99.mD+ZexMJ4t",
                object : HashValue {
                    override fun expected(cost: Int) = if (cost == 4) {
                        "BnIsPyOZYbrcTVuCuCICBmJInRz3D9IR"
                    } else {
                        "jUfmP5djNelIMUW1rTpY3YSG8dedrpxY"
                    }
                },
            ),
            Arguments.of(
                "@6iG\$yz4cA30?e=]£6£-DNP,tO/Z\"£",
                "0943316847516328",
                object : HashValue {
                    override fun expected(cost: Int) = if (cost == 4) {
                        "WmtQSJJtXQ3h9E6kTtUS2HiotYC7BeF1"
                    } else {
                        "PSI3Y8rxJiSINtd61OcwAvsK1OSTUkeL"
                    }
                },
            ),
            Arguments.of(
                "621065407890849089847098490849",
                "p7bQ2e<x#=QH6E5>",
                object : HashValue {
                    override fun expected(cost: Int) = if (cost == 4) {
                        "QMWnpaD0gp4Frg8pGt2JyRqb9Be64vq4"
                    } else {
                        "GK5IF91X1JapJHN+tM2RX43tfuAANYd1"
                    }
                },
            ),
            Arguments.of(
                "A",
                "cd31e8cd48ad41ff",
                object : HashValue {
                    override fun expected(cost: Int) = if (cost == 4) {
                        "irh5nsUI64Te2E9NQrn1QHKpsex8eGj2"
                    } else {
                        "AHCSZgLAS2ln9zmFB/i227sCnfnri7gv"
                    }
                },
            ),
        )

        @JvmStatic
        fun saltInvalidValues(): List<Arguments> = listOf(
            Arguments.of(""),
            Arguments.of("     "),
            Arguments.of("123456789012345"),
            Arguments.of("12345678901234567"),
            Arguments.of("2^wT4QO0*Jq-,rR"),
            Arguments.of("£<318w-79a3)Ag0O"),
        )

        @JvmStatic
        fun costInvalidValues(): List<Arguments> = listOf(
            Arguments.of(0),
            Arguments.of(1),
            Arguments.of(2),
            Arguments.of(3),
            Arguments.of(32),
            Arguments.of(33),
            Arguments.of(34),
            Arguments.of(-1),
            Arguments.of(-2),
        )
    }

    private val propertiesCommon = BCryptProperties(
        pepperHash = "!8\$Y-Rb%z_6nP,6@",
    )

    private val propertiesAlt = BCryptProperties(
        pepperHash = "S#}:5=Oj`Jy[-d48",
        costValue = 8,
    )

    private fun hasher(properties: BCryptProperties = propertiesCommon) = BCryptHasher(properties)

    @ParameterizedTest
    @MethodSource("saltInvalidValues")
    fun `GIVEN pepper different than 16 bytes SHOULD throws BCryptHasherException`(pepper: String) {
        val exception = assertThrows<BCryptHasherException> { BCryptProperties(pepperHash = pepper) }
        assertEquals("Pepper hash length must be 16 bytes, got ${pepper.toByteArray().size}", exception.message)
    }

    @ParameterizedTest
    @MethodSource("costInvalidValues")
    fun `GIVEN cost lower than 4 or higher than 31 SHOULD throws BCryptHasherException`(cost: Int) {
        val exception = assertThrows<BCryptHasherException> { BCryptProperties(pepperHash = "!8\$Y-Rb%z_6nP,6@", costValue = cost) }
        assertEquals("Cost value must be between 4 and 31, got $cost", exception.message)
    }

    @ParameterizedTest
    @MethodSource("saltInvalidValues")
    fun `GIVEN salt different than 16 bytes SHOULD throws BCryptHasherException`(salt: String) {
        val hasher = hasher()
        val exception = assertThrows<BCryptHasherException> { hasher.hash(UUID.randomUUID().toString(), salt) }
        assertEquals("Salt hash length must be 16 bytes, got ${salt.toByteArray().size}", exception.message)
    }

    @ParameterizedTest
    @MethodSource("hashValues")
    fun `Hash BCrypt value should returns the same value`(value: String, salt: String, expected: HashValue) {
        val hasher = hasher()
        val hashed = hasher.hash(value, salt)
        assertEquals(expected.expected(propertiesCommon.costValue), hashed)

        val hasherVerifier = hasher()
        val hashedVerifier = hasherVerifier.hash(value, salt)
        assertEquals(expected.expected(propertiesCommon.costValue), hashedVerifier)
        assertEquals(hashed, hashedVerifier)
    }

    @ParameterizedTest
    @MethodSource("hashValues")
    fun `Hash BCrypt value should returns the same value alt properties`(value: String, salt: String, expected: HashValue) {
        val hasher = hasher(propertiesAlt)
        val hashed = hasher.hash(value, salt)
        assertEquals(expected.expected(propertiesAlt.costValue), hashed)

        val hasherVerifier = hasher(propertiesAlt)
        val hashedVerifier = hasherVerifier.hash(value, salt)
        assertEquals(expected.expected(propertiesAlt.costValue), hashedVerifier)
        assertEquals(hashed, hashedVerifier)
    }

    @Test
    fun `Should generate valid salt`(){
        val hasher = hasher()
        repeat((1..1000).count()) {
            val salt = hasher.generateSalt()
            assertEquals(16, salt.toByteArray().size)
            assertDoesNotThrow { BCryptProperties(salt) }
        }
    }

}
