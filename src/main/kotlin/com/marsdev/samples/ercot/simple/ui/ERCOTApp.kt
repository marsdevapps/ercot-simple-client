package com.marsdev.samples.ercot.simple.ui

import com.marsdev.samples.ercot.simple.common.ERCOTNode
import javafx.collections.FXCollections
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

                selectionModel.selectedItemProperty().onChange {
                    model.ercotNode.value = it
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
                        controller.setSettlementPointPricesForSelection(model.date.value, model.ercotNode.value)
                    }
                }
            }
        }
    }
}


