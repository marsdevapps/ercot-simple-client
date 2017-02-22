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

    val availableDates = FXCollections.observableArrayList<LocalDate>()
    val settlementPointPrices = FXCollections.observableArrayList<SPPValue>()

    init {
        availableDates += LocalDate.of(2017, 2, 13)
        availableDates += LocalDate.of(2017, 2, 14)
        availableDates += LocalDate.of(2017, 2, 15)
        availableDates += LocalDate.of(2017, 2, 16)
        availableDates += LocalDate.of(2017, 2, 17)
        availableDates += LocalDate.of(2017, 2, 18)
        availableDates += LocalDate.of(2017, 2, 19)
        availableDates += LocalDate.of(2017, 2, 20)
    }
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
        // is this the right way to do this?
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
