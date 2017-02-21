package com.marsdev.samples.ercot.simple.service;

import com.marsdev.samples.ercot.simple.common.ERCOTNode
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.time.LocalDate

class ERCOTServiceSpec : Spek({
    describe("ercot node service") {
        val ercotService = ERCOTServiceImpl()

        on("getNodes") {
            val nodes = ercotService.getERCOTNodes()

            it("should return the set of ERCOT nodes") {
                assert(nodes.size > 0)
            }
        }

        on("getPrices") {
            val node = ERCOTNode("BLSMT1_5_A_6", 34.293473, -99.367467)
            val spp = ercotService.getSettlementPointPrices(LocalDate.of(2017, 2, 13), node)

            it("should return a set of nodes with the SPP set.") {
                assert(spp.size > 100)
            }
        }
    }
})
