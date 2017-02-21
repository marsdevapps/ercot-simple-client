package com.marsdev.samples.ercot.simple.ui

import com.marsdev.samples.ercot.simple.common.ERCOTNode
import com.marsdev.samples.ercot.simple.common.SPPValue
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.time.LocalDate

class ERCOTSelectionModel(var date: LocalDate, var ercotNode: ERCOTNode, var selectedSPP: Set<SPPValue>) : ViewModel()

class ERCOTModel : ItemViewModel<ERCOTModel>() {
    var date by property<SimpleObjectProperty<LocalDate?>>()
    fun dateProperty() = getProperty(ERCOTSelectionModel::date)

    var ercotNode by property<SimpleObjectProperty<ERCOTNode?>>()
    fun ercotNodeProperty() = getProperty(ERCOTSelectionModel::ercotNode)


}

