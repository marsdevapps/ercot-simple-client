package com.marsdev.samples.ercot.simple.ui

import com.marsdev.samples.ercot.simple.common.ERCOTNode
import com.marsdev.samples.ercot.simple.common.SPPValue
import com.marsdev.samples.ercot.simple.service.ERCOTServiceImpl
import tornadofx.*
import java.time.LocalDate
import java.util.*

class ERCOTController : Controller() {
    val selectedSPP = HashMap<LocalDate, Map<Int, SPPValue>>()
    val ercotService = ERCOTServiceImpl()

    fun getERCOTNodes(): Set<ERCOTNode> {
        return ercotService.getERCOTNodes()
    }

    fun setSettlementPointPricesForSelection(date: LocalDate, ercotNode: ERCOTNode) {
        // date: LocalDate, node: ERCOTNode
        // looks dirty.. fix/figure out why...
        selectedSPP.clear()
        selectedSPP.putAll(ercotService.getSettlementPointPrices(date, ercotNode))
        ercotNode.prices.put(date.atStartOfDay(), selectedSPP[date] as MutableMap<Int, SPPValue>)
    }
}
