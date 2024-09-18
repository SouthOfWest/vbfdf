package com.buildingblock.modules

import com.buildingblock.services.ShuttingDownService
import com.buildingblock.util.ObjectMapperFactory
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.dataformat.smile.databind.*
import com.fasterxml.jackson.module.kotlin.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import org.kodein.di.*
import java.security.cert.*
import java.util.concurrent.*
import javax.net.ssl.*

val baseModule by DI.Module {
    bindSingleton { ShuttingDownService() }
    bindSingleton<ObjectMapper> {
        ObjectMapperFactory.getObjectMapper()
    }
    bindSingleton<SmileMapper> {
        SmileMapper().apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            registerModule(KotlinModule.Builder().build())
        }
    }
    bindSingleton<ScheduledExecutorService> {
        Executors.newScheduledThreadPool(1)
    }
    bindSingleton<HttpClient> {
        HttpClient(CIO) {
            install(HttpTimeout) {
                requestTimeoutMillis = 120_000L
            }
            engine {

                https {
                    trustManager = object : X509TrustManager {
                        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

                        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

                        override fun getAcceptedIssuers(): Array<X509Certificate>? = null
                    }
                }
            }
        }
    }
}