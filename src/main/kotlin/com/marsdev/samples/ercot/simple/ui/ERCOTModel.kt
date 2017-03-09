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
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.time.LocalDate

class ERCOTSelection(date: LocalDate, ercotNode: ERCOTNode) {
    val dateProperty = SimpleObjectProperty<LocalDate>(date)
    var date by dateProperty

    val ercotNodeProperty = SimpleObjectProperty<ERCOTNode>(ercotNode)
    var ercotNode by ercotNodeProperty

    val maxPriceProperty = SimpleDoubleProperty(100.0)
    var maxPrice by maxPriceProperty

    val minPriceProperty = SimpleDoubleProperty(-100.0)
    var minPrice by minPriceProperty
}

class ERCOTSelectionModel : ItemViewModel<ERCOTSelection>() {
    val date = bind { item?.dateProperty }
    val ercotNode = bind { item?.ercotNodeProperty }
    val maxPrice = bind { item?.maxPriceProperty }
    val minPrice = bind { item?.minPriceProperty }
}



