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
    val map: MapView

    init {
        map = MapView()
        map.addLayer(myDemoLayer())
        map.setCenter(50.8458, 4.724)
        map.zoom = 3.0

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
                    controller.setSettlementPointPricesForSelection()
                }
            }
        }

        center {
            splitpane {
                orientation = Orientation.VERTICAL

                // todo need to bind number axis to upper/lower value in data and the chart title to the selected date
                linechart("ERCOT DA Hourly Prices", CategoryAxis(), NumberAxis(-50.0, 100.0, 10.0)) {
                    // todo need to bind the selected node to the series title
                    series("Settlement Point Prices") {
                        data = controller.chartSeries
                    }
                    animated = false
                }

                // todo add the map component
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
        val answer = PoiLayer()
        val icon1 = Circle(7.0, Color.BLUE)
        answer.addPoint(MapPoint(50.8458, 4.724), icon1)
        val icon2 = Circle(7.0, Color.GREEN)
        answer.addPoint(MapPoint(37.396256, -121.953847), icon2)
        return answer
    }
}


