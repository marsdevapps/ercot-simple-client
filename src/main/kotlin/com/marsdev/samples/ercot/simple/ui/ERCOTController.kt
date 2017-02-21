package com.marsdev.samples.ercot.simple.ui

import com.marsdev.samples.ercot.simple.common.ERCOTNode
import com.marsdev.samples.ercot.simple.service.ERCOTServiceImpl
import tornadofx.*

class ERCOTController : Controller() {

    val ercotService = ERCOTServiceImpl()

    fun getERCOTNodes(): Set<ERCOTNode> {
        return ercotService.getERCOTNodes()
    }
}
