package com.marsdev.samples.ercot.simple.common

import java.time.LocalDateTime

data class ERCOTNode(val name: String, val lat: Double, val lon: Double, val prices: Map<LocalDateTime, SPPValue>)

data class SPPValue(val dateTime: LocalDateTime, val hourEnding: Int, val settlementPointPrice: Double)

