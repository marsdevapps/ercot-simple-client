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
package com.marsdev.samples.ercot.simple

import com.gluonhq.maps.MapLayer
import com.gluonhq.maps.MapPoint
import com.gluonhq.maps.MapView
import com.gluonhq.maps.demo.PoiLayer
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import tornadofx.*


class MapTestApp : App(MapTestView::class)

class MapTestView : View("Map Test") {
    val view: MapView

    init {
        view = MapView()
        view.addLayer(myDemoLayer())
        view.setCenter(50.8458, 4.724)
        view.zoom = 3.0

    }

    override val root = borderpane {

        top = label("top")
        bottom = label("bottom")
        left = label("left")
        right = label("right")
        center = view

        prefHeight = 400.0
        prefWidth = 600.0
    }

    private fun myDemoLayer(): MapLayer {
        val answer = PoiLayer()
        val icon1 = Circle(7.0, Color.BLUE)
        answer.addPoint(MapPoint(50.8458, 4.724), icon1)
        val icon2 = Circle(7.0, Color.GREEN)
        answer.addPoint(MapPoint(37.396256, -121.953847), icon2)
        return answer
    }
}