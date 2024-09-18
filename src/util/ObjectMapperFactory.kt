package com.buildingblock.util

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.*
import com.fasterxml.jackson.module.kotlin.*

class ObjectMapperFactory {
    companion object {
        fun getObjectMapper(): ObjectMapper = jacksonObjectMapper()
            .registerModule(CollectionJacksonModule())
            .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
    }

    private class CollectionJacksonModule : SimpleModule() {
        init {
            addAbstractTypeMapping(Set::class.java, LinkedHashSet::class.java)
        }
    }
}