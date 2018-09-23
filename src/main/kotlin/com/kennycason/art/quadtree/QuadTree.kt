package com.kennycason.art.quadtree

import java.awt.Color
import java.awt.Rectangle
import java.awt.image.BufferedImage
import kotlin.math.roundToInt

class QuadTree(private val target: BufferedImage) {

    val canvas: BufferedImage = BufferedImage(target.width, target.height, BufferedImage.TYPE_INT_ARGB)
    private val canvasGraphics = canvas.graphics
    init {
        canvasGraphics.color = Color.BLACK
        canvasGraphics.clearRect(0, 0, canvas.width, canvas.height)
    }

    private val root: Grid = Grid(Rectangle(0, 0, canvas.width, canvas.height))

    fun handle() {
        handle(root)
    }

    private fun handle(grid: Grid) {
        if (grid.size() <= 2) {
            return
        }

        if (grid.isChild()) {
            split(grid)
        }

        handle(getMaxErrorGrid(grid))
    }

    private fun split(grid: Grid) {
        val width = (grid.location.width / 2.0).roundToInt()
        val height = (grid.location.height / 2.0).roundToInt()
        grid.sw = Grid(location = Rectangle(grid.location.x, grid.location.y, width, height))
        grid.se = Grid(location = Rectangle(grid.location.x + grid.location.width / 2, grid.location.y, width, height))
        grid.nw = Grid(location = Rectangle(grid.location.x, grid.location.y + grid.location.height / 2, width, height))
        grid.ne = Grid(location = Rectangle(grid.location.x + grid.location.width / 2, grid.location.y + grid.location.height / 2, width, height))

        fillGrid(grid.sw!!)
        fillGrid(grid.se!!)
        fillGrid(grid.nw!!)
        fillGrid(grid.ne!!)
    }

    private fun getMaxErrorGrid(grid: Grid): Grid {
        val swError = grid.sw!!.averagePixelError(target, canvas)
        val seError = grid.se!!.averagePixelError(target, canvas)
        val nwError = grid.nw!!.averagePixelError(target, canvas)
        val neError = grid.ne!!.averagePixelError(target, canvas)

        println("sw: $swError, se: $seError, nw: $nwError, ne: $neError")

        var maxErrorGrid = grid.sw!!
        var maxError = swError

        if (seError > maxError) {
            maxErrorGrid = grid.se!!
            maxError = seError
        }
        if (nwError > maxError) {
            maxErrorGrid = grid.nw!!
            maxError = nwError
        }
        if (neError > maxError) {
            maxErrorGrid = grid.ne!!
            maxError = neError
        }

        println("e: $maxError, grid: $maxErrorGrid")
        return maxErrorGrid
    }

    private fun fillGrid(grid: Grid) {
        val averageColor = grid.averageColor(target)
        canvasGraphics.color = Color(averageColor.r, averageColor.g, averageColor.b)
        canvasGraphics.fillRect(grid.location.x, grid.location.y, grid.location.width, grid.location.height)
    }

}