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
 * DISCLAIMED. IN NO EVENT SHALL GLUON BE LIABLE FOR ANY
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Service for ERCOT nodes and prices
 */
interface ERCOTService {

    /**
     * Get the SPP value for the given node in the system for the requested date.
     */
    fun getSettlementPointPrices(date: LocalDate, node: ERCOTNode): Map<LocalDate, Map<Int, SPPValue>>

    fun getERCOTNodes(): Set<ERCOTNode>
}

class ERCOTServiceImpl : ERCOTService {
    override fun getERCOTNodes(): Set<ERCOTNode> {
        // simple service... read from a csv file.  A real world application would read from a
        // database or web service
        val values = ExcelCSVParser.parse(FileReader("/temp/points.csv"))
        return values.asSequence().map { it -> ERCOTNode(it[0], it[1].toDouble(), it[2].toDouble()) }.toSortedSet()
    }

    override fun getSettlementPointPrices(date: LocalDate, node: ERCOTNode): Map<LocalDate, Map<Int, SPPValue>> {
        val values = ExcelCSVParser.parse(FileReader("/temp/prices/${date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))}.csv"))
        val format = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")
        val hourlyPrices = values.asSequence()
                .filter { it -> it[2].equals(node.name) }
                .map { it -> SPPValue(LocalDateTime.parse(it[0] + " " + it[1], format), LocalDateTime.parse(it[0] + " " + it[1], format).hour, it[3].toDouble()) }
                .toSet()

        val returnMap = HashMap<Int, SPPValue>()

        hourlyPrices.forEach {
            returnMap.put(it.hourEnding + 1, it)
        }

        val finalMap = HashMap<LocalDate, Map<Int, SPPValue>>()
        finalMap.put(date, returnMap)
        return finalMap
    }
}