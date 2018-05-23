package com.pusher.takeawaybackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TakeawayBackendApplication

fun main(args: Array<String>) {
    runApplication<TakeawayBackendApplication>(*args)
}
