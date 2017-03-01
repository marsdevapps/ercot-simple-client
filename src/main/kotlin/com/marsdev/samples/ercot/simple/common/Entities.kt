package com.marsdev.samples.ercot.simple.common

import java.time.LocalDateTime
import java.util.*
import kotlin.comparisons.compareValuesBy

data class ERCOTNode(val name: String, val lat: Double, val lon: Double) : Comparable<ERCOTNode> {
    var prices: MutableMap<LocalDateTime, MutableMap<Int, SPPValue>> = HashMap()

    override fun compareTo(other: ERCOTNode): Int {
        return compareValuesBy(this, other, ERCOTNode::name)
    }

    override fun toString(): String {
        return this.name
    }
}



data class SPPValue(val dateTime: LocalDateTime, val hourEnding: Int, val settlementPointPrice: Double)

