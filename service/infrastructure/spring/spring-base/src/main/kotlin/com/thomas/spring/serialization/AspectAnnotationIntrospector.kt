package com.thomas.spring.serialization

import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import com.thomas.core.aop.MaskField

class AspectAnnotationIntrospector : JacksonAnnotationIntrospector() {
    override fun findSerializer(field: Annotated): Any? = field.takeIf {
        it.hasAnnotation(MaskField::class.java)
    }?.let {
        MaskFieldSerializer()
    } ?: super.findSerializer(field)
}
