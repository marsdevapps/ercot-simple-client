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

import com.marsdev.samples.ercot.simple.common.ERCOTNode
import com.marsdev.samples.ercot.simple.common.SPPValue
import com.marsdev.samples.ercot.simple.service.ERCOTServiceImpl
import javafx.collections.FXCollections
import javafx.scene.chart.XYChart
import tornadofx.*
import java.time.LocalDate
import java.util.*

class ERCOTController : Controller() {
    val selectedSPP = HashMap<LocalDate, Map<Int, SPPValue>>()
    val ercotService = ERCOTServiceImpl()
    val model: ERCOTSelectionModel by inject()
    val chartSeries = FXCollections.observableArrayList<XYChart.Data<String, Number>>()

    val availableDates = FXCollections.observableArrayList<LocalDate>()
    val settlementPointPrices = FXCollections.observableArrayList<SPPValue>()

    init {
        availableDates += LocalDate.of(2017, 2, 13)
        availableDates += LocalDate.of(2017, 2, 14)
        availableDates += LocalDate.of(2017, 2, 15)
        availableDates += LocalDate.of(2017, 2, 16)
        availableDates += LocalDate.of(2017, 2, 17)
        availableDates += LocalDate.of(2017, 2, 18)
        availableDates += LocalDate.of(2017, 2, 19)
        availableDates += LocalDate.of(2017, 2, 20)
    }

    fun getERCOTNodes(): Set<ERCOTNode> {
        return ercotService.getERCOTNodes()
    }

    fun setSettlementPointPricesForSelection() {
        // date: LocalDate, node: ERCOTNode
        // looks dirty.. fix/figure out why...
        selectedSPP.clear()
        settlementPointPrices.clear()
        selectedSPP.putAll(ercotService.getSettlementPointPrices(model.date.value, model.ercotNode.value))
        settlementPointPrices.addAll((selectedSPP[model.date.value] as MutableMap<Int, SPPValue>).values.toSet())
        // is this the right way to do this?
        setChartSeriesData()
        model.ercotNode.value.prices.put(model.date.value.atStartOfDay(), selectedSPP[model.date.value] as MutableMap<Int, SPPValue>)
    }

    private fun setChartSeriesData() {
        chartSeries.clear()
        settlementPointPrices.forEach {
            chartSeries += XYChart.Data<String, Number>(it.hourEnding.toString(), it.settlementPointPrice)
        }
    }
}
