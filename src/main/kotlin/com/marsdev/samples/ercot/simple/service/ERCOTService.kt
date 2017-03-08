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
package com.marsdev.samples.ercot.simple.service

import com.Ostermiller.util.ExcelCSVParser
import com.marsdev.samples.ercot.simple.common.ERCOTNode
import com.marsdev.samples.ercot.simple.common.SPPValue
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Service for ERCOT nodes and prices
 */
interface ERCOTService {

    /**
     * Get the SPP value for the given node in the system for the requested date.
     */
    fun getSettlementPointPrices(date: LocalDate, node: ERCOTNode): Map<LocalDate, Map<Int, SPPValue>>

    fun getAvailableSPPDate(): Set<LocalDate>

    fun getERCOTNodes(): Set<ERCOTNode>

    fun loadSettlementPointPrices(date: LocalDate): Any
}

class ERCOTServiceImpl : ERCOTService {

    val prices = HashMap<ERCOTNode, Map<LocalDate, Map<Int, SPPValue>>>()
    val maxValues: Map<LocalDate, Double> = HashMap()
    val minValues: Map<LocalDate, Double> = HashMap()

    val ercotNodes: Set<ERCOTNode>

    init {
        val values = ExcelCSVParser.parse(FileReader("src/main/data/geo/points.csv"))
        ercotNodes = values.asSequence().map { it -> ERCOTNode(it[0], it[1].toDouble(), it[2].toDouble()) }.toSortedSet()

        val dates = getAvailableSPPDate()

        ercotNodes.forEach {
            val dateMap = HashMap<LocalDate, Map<Int, SPPValue>>()
            dates.forEach {
                dateMap.put(it, HashMap<Int, SPPValue>())
            }
            prices.put(it, dateMap)
        }
        dates.forEach {
            loadSettlementPointPrices(it)
        }
    }

    override fun loadSettlementPointPrices(date: LocalDate) {
        // load the requested date... set the min/max prices
        val values = ExcelCSVParser.parse(FileReader("src/main/data/prices/${date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))}.csv"))
        val format = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")

        values.asSequence().forEach {

            val datePriceMap = prices[ercotNodes.find { (name) -> name == it[2] }]
            if (datePriceMap != null && datePriceMap?.contains(date)!!) {

                val dateHourPriceMap: Map<Int, SPPValue> = datePriceMap?.get(date)!!
                if (dateHourPriceMap is HashMap<Int, SPPValue>) {
                    val sppValue = SPPValue(LocalDateTime.parse(it[0] + " " + it[1], format), LocalDateTime.parse(it[0] + " " + it[1], format).hour, it[3].toDouble())
                    dateHourPriceMap.put(sppValue.hourEnding + 1, sppValue)
                }
            }
        }
    }

    override fun getERCOTNodes(): Set<ERCOTNode> {
        return ercotNodes
    }

    override fun getAvailableSPPDate(): Set<LocalDate> {
        val dates = HashSet<LocalDate>()
        val format = DateTimeFormatter.ofPattern("yyyyMMdd")

        Files.list(Paths.get("src/main/data/prices/")).forEach({ it ->
            dates.add(LocalDate.parse(it.fileName.toString().substringBeforeLast("."), format))
        })

        return dates
    }

    override fun getSettlementPointPrices(date: LocalDate, node: ERCOTNode): Map<LocalDate, Map<Int, SPPValue>> {
        val finalMap = HashMap<LocalDate, Map<Int, SPPValue>>()
        finalMap.put(date, prices.get(node)!!.get(date)!!)
        return finalMap
    }
}