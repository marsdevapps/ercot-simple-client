package com.marsdev.samples.ercot.simple.ui

import com.marsdev.samples.ercot.simple.common.ERCOTNode
import com.marsdev.samples.ercot.simple.common.SPPValue
import javafx.collections.FXCollections
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import tornadofx.*

class ERCOTApp : App(ERCOTNodeList::class)


class ERCOTNodeList : View("ERCOT Nodes") {
    val controller: ERCOTController by inject()
    val model: ERCOTSelectionModel by inject()

    override val root = borderpane {
        prefWidth = 400.0
        prefHeight = 200.0

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
            linechart("Hourly Prices", CategoryAxis(), NumberAxis(-50.0, 100.0, 10.0)) {
                series("SPP") {
                    data = controller.chartSeries
                }
                animated = false
            }
        }
        right {
            listview<SPPValue> {
                items = controller.settlementPointPrices

                cellCache {
                    label(it.hourEnding.toString() + ":  " + it.settlementPointPrice.toString())
                }


            }
        }

        bottom {
            hbox {
                datepicker(model.date)
                button("Load Prices") {
                    // fix so it is only enabled when both the date has been set and a node is selected
                    enableWhen { model.dirty }
                    setOnAction {
                        controller.setSettlementPointPricesForSelection()
                    }
                }
            }
        }
    }
}


