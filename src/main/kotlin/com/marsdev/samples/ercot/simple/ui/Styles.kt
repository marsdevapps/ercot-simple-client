package com.marsdev.samples.ercot.simple.ui

import javafx.scene.paint.Color
import tornadofx.*

class Styles : Stylesheet() {
    companion object {

        val B_200 = c("#90CAF9")
        val swatch_100 = c("#BBDEFB")
        val swatch_200 = c("#90CAF9")
        val swatch_300 = c("#64BEF6")
        val swatch_400 = c("#42A5F5")
        val swatch_500 = c("#2196F3")

        val borderPane by cssclass()
    }

    init {
        root {
            focusColor = Color.TRANSPARENT
            faintFocusColor = Color.TRANSPARENT
            backgroundColor += Color.BLACK
            unsafe("-fx-accent", raw(B_200.css))
            unsafe("-fx-dark-text-color", raw("white"))
        }

        borderPane {
            //            focusColor = Color.TRANSPARENT
//            faintFocusColor = Color.TRANSPARENT
//            backgroundColor += Color.TRANSPARENT
        }
    }

}