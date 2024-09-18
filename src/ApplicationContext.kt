package com.buildingblock

import com.buildingblock.modules.*
import com.typesafe.config.*
import org.kodein.di.*

fun buildContext(config: Config) = DI.direct {
    bindSingleton { config }

    importOnce(baseModule)
//    importOnce(amqpModule)
//    importOnce(redisModule)
//    importOnce(mongoModule)
//    importOnce(processingCollectionsModule)
//    importOnce(partsDataCollectionsModule)
//    importOnce(analyticsCollectionsModule)
//    importOnce(elasticModule)
//    importOnce(jdbiModule)
//    importOnce(snowflakeModule)
//    importOnce(servicesModule)
//    importOnce(customAttributeModule)
//    importOnce(phasesModule)
//    importOnce(handlerModule)
//    importOnce(commandsModule)
//    importOnce(rpcModule)
}
