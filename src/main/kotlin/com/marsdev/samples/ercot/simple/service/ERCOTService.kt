package com.marsdev.samples.ercot.simple.service

import com.Ostermiller.util.ExcelCSVParser
import com.marsdev.samples.ercot.simple.common.ERCOTNode
import com.marsdev.samples.ercot.simple.common.SPPValue
import java.io.FileReader
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Service for ERCOT nodes and prices
 */
interface ERCOTService {

    /**
     * Get the SPP value for the given node in the system for the requested date.
     */
    fun getSettlementPointPrices(date: LocalDate, node: ERCOTNode): Map<LocalDate, Map<Int, SPPValue>>

    fun getERCOTNodes(): Set<ERCOTNode>
}

class ERCOTServiceImpl : ERCOTService {
    override fun getERCOTNodes(): Set<ERCOTNode> {
        // simple service... read from a csv file.  A real world application would read from a
        // database or web service
        val values = ExcelCSVParser.parse(FileReader("/temp/points.csv"))
        return values.asSequence().map { it -> ERCOTNode(it[0], it[1].toDouble(), it[2].toDouble()) }.toSortedSet()
    }

    override fun getSettlementPointPrices(date: LocalDate, node: ERCOTNode): Map<LocalDate, Map<Int, SPPValue>> {
        val values = ExcelCSVParser.parse(FileReader("/temp/prices/${date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))}.csv"))
        val format = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")
        val hourlyPrices = values.asSequence()
                .filter { it -> it[2].equals(node.name) }
                .map { it -> SPPValue(LocalDateTime.parse(it[0] + " " + it[1], format), LocalDateTime.parse(it[0] + " " + it[1], format).hour, it[3].toDouble()) }
                .toSet()

        val returnMap = HashMap<Int, SPPValue>()

        hourlyPrices.forEach {
            returnMap.put(it.hourEnding + 1, it)
        }

        val finalMap = HashMap<LocalDate, Map<Int, SPPValue>>()
        finalMap.put(date, returnMap)
        return finalMap
    }
}