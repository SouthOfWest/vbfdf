package com.buildingblock.services

import java.io.*

class ShuttingDownService {
    private val closableList = mutableListOf<Closeable>()

    fun register(closable: Closeable) {
        closableList.add(closable)
    }

    fun shutdown() {
        var exceptionOnClose: Throwable? = null
        closableList.forEach { closable ->
            try {
                closable.close()
            } catch (closeException: Throwable) {
                if (null == exceptionOnClose) {
                    exceptionOnClose = closeException
                } else {
                    exceptionOnClose!!.addSuppressed(closeException)
                }
            }
        }
        if (null != exceptionOnClose) {
            throw exceptionOnClose!!
        }
    }
}