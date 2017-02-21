package com.marsdev.samples.ercot.simple.common

import java.time.LocalDateTime
import kotlin.comparisons.compareValuesBy

data class ERCOTNode(val name: String, val lat: Double, val lon: Double) : Comparable<ERCOTNode> {
    lateinit var prices: Map<LocalDateTime, Map<Int, SPPValue>>

    override fun compareTo(other: ERCOTNode): Int {
        return compareValuesBy(this, other, ERCOTNode::name)
    }
}



data class SPPValue(val dateTime: LocalDateTime, val hourEnding: Int, val settlementPointPrice: Double)

