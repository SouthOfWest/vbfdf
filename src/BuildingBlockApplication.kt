package com.buildingblock

import com.buildingblock.commands.CommandsApplicationRunner
import com.typesafe.config.*
import kotlinx.coroutines.*
import org.kodein.di.*
import org.slf4j.*
import kotlin.system.*

@ObsoleteCoroutinesApi
fun main(args: Array<String>) {
    val isCommand = args.isNotEmpty()
    val config = ConfigFactory.load()
    val di = buildContext(config)

    if (isCommand) {
        var commandResult = 1
        try {
            useMDCContext("command_name" to args.first(), "command" to args.joinToString(" ")) {
                val commandsApplicationRunner = di.instance<CommandsApplicationRunner>()

                commandResult = commandsApplicationRunner.run(args)
            }
        } finally {
            exitProcess(commandResult)
        }
    }
}

inline fun <R> useMDCContext(vararg pair: Pair<String, String>, block: () -> R): R = try {
    pair.forEach { (key, value) ->
        MDC.put(key, value)
    }

    block()
} finally {
    pair.forEach { (key, _) ->
        MDC.remove(key)
    }
}