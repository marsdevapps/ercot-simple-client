package com.marsdev.samples.ercot.simple

import com.gluonhq.maps.MapLayer
import com.gluonhq.maps.MapPoint
import com.gluonhq.maps.MapView
import com.gluonhq.maps.demo.PoiLayer
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import tornadofx.*


class MapTestApp : App(MapTestView::class)

class MapTestView : View("Map Test") {
    val view: MapView

    init {
        view = MapView()
        view.addLayer(myDemoLayer())
        view.setCenter(50.8458, 4.724)
        view.zoom = 3.0

    }

    override val root = borderpane {

        top = label("top")
        bottom = label("bottom")
        left = label("left")
        right = label("right")
        center = view

        prefHeight = 400.0
        prefWidth = 600.0
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