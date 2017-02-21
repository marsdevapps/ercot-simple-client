package com.marsdev.samples.ercot.simple.ui

import com.marsdev.samples.ercot.simple.common.ERCOTNode
import javafx.collections.FXCollections
import tornadofx.*


class ERCOTApp : App(ERCOTNodeList::class)


class ERCOTNodeList : View("ERCOT Nodes") {
    val ercotController: ERCOTController by inject()

    override val root = borderpane {
        prefWidth = 400.0
        prefHeight = 200.0

        center {
            listview<ERCOTNode> {
                items = FXCollections.observableArrayList(ercotController.getERCOTNodes())

                cellCache {
                    label(it.name)
                }
            }
        }
    }
}


