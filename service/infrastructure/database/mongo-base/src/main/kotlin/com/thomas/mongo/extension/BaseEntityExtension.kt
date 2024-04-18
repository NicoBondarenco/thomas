package com.thomas.mongo.extension

import com.mongodb.client.model.Updates
import com.thomas.core.model.entity.BaseEntity
import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmErasure
import org.bson.Document
import org.bson.conversions.Bson

const val ENTITY_CLASS_PROPERTY = "entityClass"

fun <T : BaseEntity<T>> T.toUpsertDocument(): Bson = this::class.memberProperties.map { prop ->
    val value = (prop as KProperty1<BaseEntity<*>, Any?>).get(this)
    if (prop.isBaseEntity()) {
        val document = value?.let { (it as BaseEntity<*>).toDocument() }
        Updates.set(prop.name, document)
    } else if (prop.isBaseEntityList()) {
        val documents = value?.let { list ->
            (list as List<*>).filterNotNull().map { (it as BaseEntity<*>).toDocument() }
        }
        Updates.set(prop.name, documents)
    } else {
        Updates.set(prop.name, value)
    }
}.let {
    val props = it + Updates.set(ENTITY_CLASS_PROPERTY, this::class.qualifiedName!!)
    Updates.combine(props)
}

fun BaseEntity<*>.toDocument(): Document = Document().also { document ->
    document[ENTITY_CLASS_PROPERTY] = this::class.qualifiedName
    this::class.memberProperties.forEach { prop ->
        val value = (prop as KProperty1<BaseEntity<*>, Any?>).get(this)
        if (prop.isBaseEntity()) {
            val doc = value?.let { (it as BaseEntity<*>).toDocument() }
            document[prop.name] = doc
        } else if (prop.isBaseEntityList()) {
            val documents = value?.let { list ->
                (list as List<*>).filterNotNull().map { (it as BaseEntity<*>).toDocument() }
            }
            document[prop.name] = documents
        } else {
            document[prop.name] = value
        }
    }
}

private fun KProperty1<BaseEntity<*>, Any?>.isBaseEntity() =
    this.returnType.jvmErasure.isSubclassOf(BaseEntity::class)

private fun KProperty1<BaseEntity<*>, Any?>.isBaseEntityList() =
    this.returnType.jvmErasure.isSubclassOf(List::class) &&
            this.returnType.arguments.first().type?.jvmErasure?.isSubclassOf(BaseEntity::class) == true
