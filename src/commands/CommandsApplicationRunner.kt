package com.buildingblock.commands

import com.beust.jcommander.*
import net.logstash.logback.argument.StructuredArgument
import net.logstash.logback.argument.StructuredArguments.entries
import org.kodein.di.*
import org.slf4j.*
import kotlin.system.*

class CommandsApplicationRunner(
    private val di: DirectDI,
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun run(args: Array<String>): Int {
        logger.info("Start processing-kt command", context("args" to args))

        val jc = JCommander()
        val parsedCommand = args.first()

        var exception: Throwable? = null
        val duration = measureTimeMillis {
            try {
                val command = di.instanceOrNull<CommandInterface>(tag = parsedCommand)
                    ?: throw IllegalArgumentException("Command not found: $parsedCommand")
                jc.addCommand(parsedCommand, command)

                jc.parse(*args)

                logger.info(
                    "Command $parsedCommand is started",
                    context("command_name" to parsedCommand, "time" to timestamp())
                )

                command.execute()
            } catch (ex: Throwable) {
                exception = ex
            }
        }

        exception?.let {
            logger.error("Exception in command: $parsedCommand exception: ${it.message}", it)
        }

        logger.info(
            "Command $parsedCommand is completed",
            context(
                "command_name" to parsedCommand,
                "time" to timestamp(),
                "duration" to duration
            )
        )

        return if (null != exception) 1 else 0
    }
}

fun <K, V> context(vararg pairs: Pair<K, V>): StructuredArgument {
    return entries(pairs.toMap().mapKeys { (key) -> "ctxt_$key" })
}

fun timestamp() = (System.currentTimeMillis() / 1000).toInt()