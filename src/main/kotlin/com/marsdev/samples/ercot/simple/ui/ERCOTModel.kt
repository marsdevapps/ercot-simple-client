package com.marsdev.samples.ercot.simple.ui

import com.marsdev.samples.ercot.simple.common.ERCOTNode
import com.marsdev.samples.ercot.simple.common.SPPValue
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.time.LocalDate

class ERCOTSelection {
    val selectedSPPProperty = SimpleObjectProperty<Set<SPPValue>>()
    var selectedSPP by selectedSPPProperty

    val dateProperty = SimpleObjectProperty<LocalDate>()
    var date by dateProperty

    val ercotNodeProperty = SimpleObjectProperty<ERCOTNode>()
    var ercotNode by ercotNodeProperty
}

class ERCOTSelectionModel : ItemViewModel<ERCOTSelection>() {
    val selectedSPP = bind { item?.selectedSPPProperty }
    val date = bind { item?.dateProperty }
    val ercotNode = bind { item?.ercotNodeProperty }
}



