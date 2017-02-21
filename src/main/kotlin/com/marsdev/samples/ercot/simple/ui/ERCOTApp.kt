package com.marsdev.samples.ercot.simple.ui

import com.marsdev.samples.ercot.simple.common.ERCOTNode
import javafx.collections.FXCollections
import tornadofx.*


class ERCOTApp : App(ERCOTNodeList::class)


class ERCOTNodeList : View("ERCOT Nodes") {
    val controller: ERCOTController by inject()
    override val scope = super.scope as ERCOTScope


    override val root = borderpane {
        prefWidth = 400.0
        prefHeight = 200.0

        left {
            listview<ERCOTNode> {
                items = FXCollections.observableArrayList(controller.getERCOTNodes())

                cellCache {
                    label(it.name)
                }

                selectionModel.selectedItemProperty().onChange {
                    scope.ercotModel.ercotNode.set(it)
                }
            }
        }

        bottom {
            hbox {
                datepicker(scope.ercotModel.dateProperty())
                button("Load Prices") {
                    setOnAction {
                        controller.setSettlementPointPricesForSelection()
                    }
                }
            }
        }
    }
}


