package com.marsdev.samples.ercot.simple.ui

import com.marsdev.samples.ercot.simple.common.ERCOTNode
import com.marsdev.samples.ercot.simple.common.SPPValue
import com.marsdev.samples.ercot.simple.service.ERCOTServiceImpl
import javafx.collections.FXCollections
import javafx.scene.chart.XYChart
import tornadofx.*
import java.time.LocalDate
import java.util.*

class ERCOTController : Controller() {
    val selectedSPP = HashMap<LocalDate, Map<Int, SPPValue>>()
    val ercotService = ERCOTServiceImpl()
    val model: ERCOTSelectionModel by inject()
    val chartSeries = FXCollections.observableArrayList<XYChart.Data<String, Number>>()

    val settlementPointPrices = FXCollections.observableArrayList<SPPValue>()

    fun getERCOTNodes(): Set<ERCOTNode> {
        return ercotService.getERCOTNodes()
    }

    fun setSettlementPointPricesForSelection() {
        // date: LocalDate, node: ERCOTNode
        // looks dirty.. fix/figure out why...
        selectedSPP.clear()
        settlementPointPrices.clear()
        selectedSPP.putAll(ercotService.getSettlementPointPrices(model.date.value, model.ercotNode.value))
        settlementPointPrices.addAll((selectedSPP[model.date.value] as MutableMap<Int, SPPValue>).values.toSet())
        setChartSeriesData()
        model.ercotNode.value.prices.put(model.date.value.atStartOfDay(), selectedSPP[model.date.value] as MutableMap<Int, SPPValue>)
    }

    private fun setChartSeriesData() {
        chartSeries.clear()
        settlementPointPrices.forEach {
            chartSeries += XYChart.Data<String, Number>(it.hourEnding.toString(), it.settlementPointPrice)
        }
    }
}
