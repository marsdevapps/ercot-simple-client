package com.marsdev.samples.ercot.simple.ui

import com.marsdev.samples.ercot.simple.common.ERCOTNode
import com.marsdev.samples.ercot.simple.common.SPPValue
import com.marsdev.samples.ercot.simple.service.ERCOTServiceImpl
import tornadofx.*
import java.time.LocalDate
import java.util.*

class ERCOTController : Controller() {
    override val scope = super.scope as ERCOTScope
    val selectedSPP = HashMap<LocalDate, Map<Int, SPPValue>>()
    val ercotService = ERCOTServiceImpl()

    fun getERCOTNodes(): Set<ERCOTNode> {
        return ercotService.getERCOTNodes()
    }

    fun setSettlementPointPricesForSelection() {
        // date: LocalDate, node: ERCOTNode
        // looks dirty.. fix/figure out why...
        selectedSPP.clear()
        selectedSPP.putAll(ercotService.getSettlementPointPrices(scope.ercotModel.date.get()!!, scope.ercotModel.ercotNode.get()!!))
        println(selectedSPP.values.size)
    }
}
