package com.marsdev.samples.ercot.simple.ui

import com.marsdev.samples.ercot.simple.common.ERCOTNode
import com.marsdev.samples.ercot.simple.common.SPPValue
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.text.FontWeight
import tornadofx.*
import java.time.LocalDate

class ERCOTApp : App(ERCOTNodeList::class)


class ERCOTNodeList : View("ERCOT Nodes") {
    val controller: ERCOTController by inject()
    val model: ERCOTSelectionModel by inject()

    override val root = borderpane {
        prefWidth = 1200.0
        prefHeight = 800.0

        left {
            listview<ERCOTNode> {
                items = FXCollections.observableArrayList(controller.getERCOTNodes())
                cellCache {
                    label(it.name)
                }
                bindSelected(model.ercotNode)
                selectionModel.selectedItemProperty().onChange {
                    controller.setSettlementPointPricesForSelection()
                }
            }
        }

        center {
            // todo need to bind number axis to upper/lower value in data and the chart title to the selected date
            linechart("ERCOT DA Hourly Prices", CategoryAxis(), NumberAxis(-50.0, 100.0, 10.0)) {
                // todo need to bind the selected node to the series title
                series("Settlement Point Prices") {
                    data = controller.chartSeries
                }
                animated = false
            }

            // todo add the map component
        }
        right {
            // todo come up with a better way to display the hours
            listview<SPPValue> {
                items = controller.settlementPointPrices
                cellCache {
                    //                    label(it.hourEnding.toString() + ":  " + it.settlementPointPrice.toString())
                    label("Hour Ending ${it.hourEnding + 1}:  $${it.settlementPointPrice}") {
                        alignment = Pos.CENTER_RIGHT
                        style {
                            fontSize = 16.px
                            fontWeight = FontWeight.MEDIUM
                        }
                    }
                }
                selectionModel.selectedItemProperty().onChange {
                    // todo not working
                    controller.setSettlementPointPricesForSelection()
                }

            }
        }

        bottom {
            hbox {
                // todo set an initial selected date
                combobox<LocalDate>(model.date) {
                    items = controller.availableDates
                }
            }
        }
    }
}


