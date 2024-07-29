package com.thomas.authentication.tokenizer.auth0.data

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY
import com.fasterxml.jackson.annotation.PropertyAccessor.ALL
import com.fasterxml.jackson.core.JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_INVALID_SUBTYPE
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE
import com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature.NullIsSameAsDefault
import com.fasterxml.jackson.module.kotlin.KotlinFeature.NullToEmptyCollection
import com.fasterxml.jackson.module.kotlin.KotlinFeature.NullToEmptyMap
import com.fasterxml.jackson.module.kotlin.KotlinFeature.SingletonSupport
import com.fasterxml.jackson.module.kotlin.KotlinFeature.StrictNullChecks
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.thomas.authentication.tokenizer.auth0.properties.Auth0TokenizerProperties
import com.thomas.core.model.general.Gender.CIS_MALE
import com.thomas.core.model.security.SecurityUser
import com.thomas.core.random.randomSecurityGroups
import com.thomas.core.random.randomSecurityRoles
import java.time.LocalDate
import java.util.UUID.randomUUID

val defaultConfiguration = Auth0TokenizerProperties(
    issuerName = "test-application",
    jwtAudience = "test-audience",
    jwtRealm = "test-realm",
    jwtAlgorithm = "RSA",
    privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDJMiW6O3XOwALyk+" +
            "DsYRop+zp0HeNgbMpfKuJx4A5Hqcqdz0pmIAEyZMZCnwmG2GDE5D3oVQoST52V9BtENZxX+" +
            "yGnzd+FsKWND8/azDOPGtCC25GXHeppy1Swpd5Vsbe5w4itEsKABJY068zZmLtaGZA0jsSW" +
            "nOZFFWqGxDSVVAa8fukr8cpKNIFf5psGgWG3LyIPcjrU2R9nkx5mZ+DOJ/rlwX6Fg8+h2NJ" +
            "A7j7QDLjbzjzBAIe7nU3b5hn2rFdJfdPBbdgz+rLgeeSWqFH0IYKhAFdfVCrwcX1TcmeL+Y" +
            "6yIzlHp9dnwsYwXHj/+FunAeuIqP84TnXodrHMM00zAgMBAAECggEACS5DH3TgIs+LMuOBk" +
            "3e42GqQ18SXYAqGz4Hm9FhLfoSmbL4NQIy8UT7c9yRHrIIKJGHf206qF3zPjYymg93IZRFe" +
            "D0irJvARFdc/XlYMEj8gfaAkTBd+31X6ZtcixplF73GXNoKRIoNTEVYjMliwT+Ozu1o6aT7" +
            "+mO9pvX2NgrTOm9+1ZRpb4dZ2tGT2hqeni/HJE3yS4KlwMPVIssHnjJ1JdugrVGb19GYgsT" +
            "O/LMHSdmzlGYlsTuYg35X6dMI0uk4pj2zJ35bqWASQywuLCJMNwYEsrJkkXwyJRa5rFDIKO" +
            "Zo6CcxTkQ+19nJbF3qn8QNs1Y3K8II8ykufXdsBnQKBgQDxHXJ+I2QCke1T6nPXxrYHZdBk" +
            "PTccNi43gbottPcxixHyPF11rF48ImrpAqodTvv+IzdPb9mo47UQ0ZyUyorIw8eYvyIjSpf" +
            "i07FNjgyYqZQFF/oRq3ArHZWjLtdXWE5L6DHqa39YXvS504SMDdKd9vLfx/1kVoszvOkAyT" +
            "mmdwKBgQDVndKnqFAPhm05oHM9hmcMy2neSOiby8wBKPahrTycYnle+iWmyEICUx87m/cj5" +
            "peqelMVV8jC/pAg0LAoA7ZAtPmR49VgN263qIxtHuwZgCIzI9iWRqHfXP1Lw6R1HqSXLr71" +
            "6jV6FDWVs5OGPBajenBkJBbIROuYaex8Bj0yJQKBgG0aTQ+ExiFflMtvAII3+XA5guWIyaI" +
            "rZhhZzDwYce5qJnG9HPE9UNrXHBdZzE48ykwB9Woti4kfmHQG1VqCl/Amnu+gNTtQ6o+KCO" +
            "W2DD8cJM11PwO11qi711UiwfBVFgDFDaksgVAkj4pyiryxGIfAEcBvB1raylxb8/cWFs8bA" +
            "oGBALRGeNzdApPXkp4rdpXZs2Xs1O+u//9bQlNISruvA5PG1z78RII4RtzHYXhg63T0ydIp" +
            "YayFZPyK8P+AnulmGI0LLgOdDxogLeP47mm4yu/KXxJhkgtgmIN0ap9iAIyGy8h6vtWyTmc" +
            "0XvRzlHCYjVJxpiAdBM0GvPznh5gze7mxAoGAcCBUEmCaRRxEuMaU/jJ3IgX0q7R90uk4+s" +
            "QUjgKoT2j2cBdTSq8hRRiZ/TJ6nj4SkqgxkoAf8d3tfwf3ED/8e10oM02zEYQvT8b2R7fjI" +
            "yIEOutizbedtiwWs9PgTu9lzey0kBpqTSlJp0ADpTQVfZLVTjzCYlwq897R98GWlKs=",
    publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyTIlujt1zsAC8pPg7GEaKfs" +
            "6dB3jYGzKXyriceAOR6nKnc9KZiABMmTGQp8JhthgxOQ96FUKEk+dlfQbRDWcV/shp83fhb" +
            "CljQ/P2swzjxrQgtuRlx3qactUsKXeVbG3ucOIrRLCgASWNOvM2Zi7WhmQNI7ElpzmRRVqh" +
            "sQ0lVQGvH7pK/HKSjSBX+abBoFhty8iD3I61NkfZ5MeZmfgzif65cF+hYPPodjSQO4+0Ay4" +
            "2848wQCHu51N2+YZ9qxXSX3TwW3YM/qy4HnklqhR9CGCoQBXX1Qq8HF9U3Jni/mOsiM5R6f" +
            "XZ8LGMFx4//hbpwHriKj/OE516HaxzDNNMwIDAQAB",
)

val defaultUser: SecurityUser
    get() = SecurityUser(
        userId = randomUUID(),
        firstName = "Security",
        lastName = "Token",
        mainEmail = "security.token@example.com",
        phoneNumber = "16988776655",
        profilePhoto = "https://profile-photo.com/user-profile.png",
        birthDate = LocalDate.of(1990, 4, 28),
        userGender = CIS_MALE,
        isActive = true,
        userRoles = randomSecurityRoles(),
        userGroups = randomSecurityGroups(),
    )

val mapper = ObjectMapper().registerModule(
    KotlinModule.Builder()
        .withReflectionCacheSize(512)
        .configure(NullToEmptyCollection, false)
        .configure(NullToEmptyMap, false)
        .configure(NullIsSameAsDefault, false)
        .configure(SingletonSupport, false)
        .configure(StrictNullChecks, false)
        .build()
).registerModule(JavaTimeModule())
    .setPropertyNamingStrategy(SNAKE_CASE)
    .setVisibility(ALL, ANY)
    .enable(WRITE_BIGDECIMAL_AS_PLAIN)
    .disable(FAIL_ON_UNKNOWN_PROPERTIES)
    .disable(FAIL_ON_INVALID_SUBTYPE)
    .disable(WRITE_DATES_AS_TIMESTAMPS)
    .disable(FAIL_ON_EMPTY_BEANS)
