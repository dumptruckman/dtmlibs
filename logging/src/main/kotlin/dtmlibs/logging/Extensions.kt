package dtmlibs.logging

import mu.KLogger

val Class<*>.logger: KLogger
    get() = Logging.getLogger(this)

val Any.logger: KLogger
    get() = Logging.getLogger(this::class.java)