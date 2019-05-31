package ca.marshallwalker.updns.ext

import org.slf4j.LoggerFactory

val Any.logger get() = LoggerFactory.getLogger(javaClass)