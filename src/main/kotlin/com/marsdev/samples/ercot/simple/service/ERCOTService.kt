package com.marsdev.samples.ercot.simple.service

import com.marsdev.samples.ercot.simple.common.ERCOTNode
import java.time.LocalDate

/**
 * Service for ERCOT nodes and prices
 */
interface ERCOTService {

    /**
     * Get the SPP values for all nodes available in the system for the requested
     * Date.
     */
    fun getSettlementPointPrices(date: LocalDate): List<ERCOTNode>
}

class ERCOTServiceImpl : ERCOTService {
    override fun getSettlementPointPrices(date: LocalDate): List<ERCOTNode> {
        throw UnsupportedOperationException("not implemented")
    }
}