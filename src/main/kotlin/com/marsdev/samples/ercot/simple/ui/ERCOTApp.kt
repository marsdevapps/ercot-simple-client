/*
 * Copyright (c) 2017, mars dev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL MARS DEV BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.marsdev.samples.ercot.simple.ui

import com.esri.arcgisruntime.geometry.Point
import com.esri.arcgisruntime.geometry.SpatialReference
import com.esri.arcgisruntime.geometry.SpatialReferences
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.mapping.view.Graphic
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol
import com.marsdev.samples.ercot.simple.common.ERCOTNode
import com.marsdev.samples.ercot.simple.common.SPPValue
import com.marsdev.util.ToolTipDefaultsFixer
import javafx.collections.FXCollections
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.control.Tooltip
import javafx.scene.text.FontWeight
import tornadofx.*
import java.time.LocalDate

class ERCOTApp : App(ERCOTNodeList::class, Styles::class) {
    init {
        ToolTipDefaultsFixer.setTooltipTimers(25, 5000, 200)
        reloadStylesheetsOnFocus()
    }
}


class ERCOTNodeList : View("ERCOT Nodes") {
    val controller: ERCOTController by inject()
    val model: ERCOTSelectionModel by inject()
    lateinit var ercotNodes: Set<ERCOTNode>
    val mapView: MapView
    val map: ArcGISMap
    val priceAxis = NumberAxis()
    val spatialReference: SpatialReference

    init {
        spatialReference = SpatialReferences.getWebMercator()
        map = ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, 27.607441, -97.310196, 10)
        mapView = MapView()
        mapView.map = map
        ercotNodes = controller.getERCOTNodes()
        val ercotNode = ercotNodes.elementAt(0)
        priceAxis.isAutoRanging = false
        mapView.graphicsOverlays.add(myDemoLayer())
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
                selectionModel.selectFirst()
                selectionModel.selectedItemProperty().onChange {
                    mapView.setViewpointCenterAsync(Point(model.ercotNode.value.lon, model.ercotNode.value.lat, SpatialReferences.getWgs84()), 12000.0)
                    controller.setSettlementPointPricesForSelection()
                }
            }
        }

        center {
            splitpane {
                orientation = Orientation.VERTICAL

                linechart("ERCOT DA Hourly Prices", CategoryAxis(), priceAxis) {
                    series("Settlement Point Prices") {
                        data = controller.chartSeries
                        data.onChange {
                            // todo need to bind the selected node to the series title; this feels dirty?
                            name = "SPP - " + model.ercotNode.value.name
                            data.forEach {
                                Tooltip.install(it.node, Tooltip((it.yValue.toString())))
                            }
                            priceAxis.upperBoundProperty().set(model.maxPrice.value as Double)
                            priceAxis.lowerBoundProperty().set(model.minPrice.value as Double)

                        }
                    }
                    animated = false
                }
                add(mapView)
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
                        // todo better way to bind/handle this?
                        var max = controller.getMinMaxPrices(model.date.value).first
                        var min = controller.getMinMaxPrices(model.date.value).second
                        model.maxPrice.value = Math.ceil(max) + 5
                        model.minPrice.value = Math.floor(min) - 5
                    }
                }
            }
        }
    }

    private fun myDemoLayer(): GraphicsOverlay {
        val graphicsOverlay = GraphicsOverlay()
        ercotNodes.forEach { n ->
            val point = Point(n.lon, n.lat, SpatialReferences.getWgs84())
            val symbol = SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, 0xFFFF0000.toInt(), 10f)
            val graphic = Graphic(point, symbol)
            graphicsOverlay.graphics.add(graphic)
//            circle.tooltip(n.name)

        }
        return graphicsOverlay
    }
}


