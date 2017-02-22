package com.marsdev.samples.ercot.simple.ui

import com.marsdev.samples.ercot.simple.common.ERCOTNode
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.time.LocalDate

class ERCOTSelection(date: LocalDate, ercotNode: ERCOTNode) {
    val dateProperty = SimpleObjectProperty<LocalDate>(date)
    var date by dateProperty

    val ercotNodeProperty = SimpleObjectProperty<ERCOTNode>(ercotNode)
    var ercotNode by ercotNodeProperty
}

class ERCOTSelectionModel : ItemViewModel<ERCOTSelection>() {
    val date = bind { item?.dateProperty }
    val ercotNode = bind { item?.ercotNodeProperty }
}



