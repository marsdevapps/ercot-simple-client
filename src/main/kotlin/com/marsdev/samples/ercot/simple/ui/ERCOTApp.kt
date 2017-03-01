package com.marsdev.samples.ercot.simple.ui

import com.gluonhq.maps.MapLayer
import com.gluonhq.maps.MapPoint
import com.gluonhq.maps.MapView
import com.gluonhq.maps.demo.PoiLayer
import com.marsdev.samples.ercot.simple.common.ERCOTNode
import com.marsdev.samples.ercot.simple.common.SPPValue
import javafx.collections.FXCollections
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.text.FontWeight
import tornadofx.*
import java.time.LocalDate

class ERCOTApp : App(ERCOTNodeList::class)


class ERCOTNodeList : View("ERCOT Nodes") {
    val controller: ERCOTController by inject()
    val model: ERCOTSelectionModel by inject()
    lateinit var ercotNodes: Set<ERCOTNode>
    val map: MapView

    init {

        map = MapView()
        ercotNodes = controller.getERCOTNodes()
        val ercotNode = ercotNodes.elementAt(0)
        map.setCenter(ercotNode.lat, ercotNode.lon)
        map.zoom = 10.0
        map.addLayer(myDemoLayer())
    }

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
                    map.zoom = 13.0
                    map.setCenter(model.ercotNode.value.lat, model.ercotNode.value.lon)

                    controller.setSettlementPointPricesForSelection()
                }
            }
        }

        center {
            splitpane {
                orientation = Orientation.VERTICAL

                linechart("ERCOT DA Hourly Prices", CategoryAxis(), NumberAxis()) {
                    // todo need to bind the selected node to the series title
                    series("Settlement Point Prices ") {
                        data = controller.chartSeries
                    }
                    animated = false
                }
                add(map)
            }
        }
        right {
            // todo come up with a better way to display the hours
            listview<SPPValue> {
                items = controller.settlementPointPrices
                cellCache {
                    label("Hour Ending ${it.hourEnding + 1}:  $${it.settlementPointPrice}") {
                        alignment = Pos.CENTER_RIGHT
                        style {
                            fontSize = 16.px
                            fontWeight = FontWeight.MEDIUM
                        }
                    }
                }
            }
        }

        bottom {
            hbox {
                combobox<LocalDate>(model.date) {
                    items = controller.availableDates
                    selectionModel.selectFirst()

                    selectionModel.selectedItemProperty().onChange {
                        controller.setSettlementPointPricesForSelection()
                    }
                }
            }
        }
    }

    private fun myDemoLayer(): MapLayer {
        val ercotNodesLayer = PoiLayer()
        ercotNodes.forEach { n ->
            val mapPoint = MapPoint(n.lat, n.lon)
            val circle = Circle(4.0, Color.BLUEVIOLET)
            circle.tooltip(n.name)
            ercotNodesLayer.addPoint(mapPoint, circle)

        }
        return ercotNodesLayer
    }
}


