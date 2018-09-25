package com.kennycason.art.quadtree

import java.awt.Color
import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.util.*

class QuadTree(private val target: BufferedImage,
               private val minSplitSize: Int = 2,
               private val randomProbability: Int = 0 /* 0 to 100 */) {
    val canvas: BufferedImage = BufferedImage(target.width, target.height, BufferedImage.TYPE_INT_ARGB)
    private val random = Random()
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
        if (grid.size() <= minSplitSize) {
            return
        }

        if (grid.isChild()) {
            split(grid)
        }

        if (random.nextInt(100) < randomProbability) {
            handle(getRandomGrid(grid))
        }
        else {
            handle(getMaxErrorGrid(grid))
        }
    }

    private fun split(grid: Grid) {
        val width = Math.ceil(grid.location.width / 2.0).toInt()
        val height = Math.ceil(grid.location.height / 2.0).toInt()

        grid.nw = Grid(resizeIfNeeded(Rectangle(grid.location.x,         grid.location.y,          width, height)))
        grid.ne = Grid(resizeIfNeeded(Rectangle(grid.location.x + width, grid.location.y,          width, height)))
        grid.sw = Grid(resizeIfNeeded(Rectangle(grid.location.x,         grid.location.y + height, width, height)))
        grid.se = Grid(resizeIfNeeded(Rectangle(grid.location.x + width, grid.location.y + height, width, height)))

        fillGrid(grid.sw!!)
        fillGrid(grid.se!!)
        fillGrid(grid.nw!!)
        fillGrid(grid.ne!!)
    }

    private fun resizeIfNeeded(r: Rectangle): Rectangle {
        return when {
            r.x + r.width >= target.width -> resizeIfNeeded(Rectangle(r.x, r.y, r.width - 1, r.height))
            r.y + r.height >= target.height -> resizeIfNeeded(Rectangle(r.x, r.y, r.width, r.height - 1))
            else -> r
        }
    }

    private fun getRandomGrid(grid: Grid) = when (random.nextInt(4)) {
        0 -> grid.sw!!
        1 -> grid.se!!
        2 -> grid.nw!!
        3 -> grid.ne!!
        else -> throw IllegalStateException()
    }

    private fun getMaxErrorGrid(grid: Grid): Grid {
        val swError = grid.sw!!.squaredPixelError(target, canvas)
        val seError = grid.se!!.squaredPixelError(target, canvas)
        val nwError = grid.nw!!.squaredPixelError(target, canvas)
        val neError = grid.ne!!.squaredPixelError(target, canvas)

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

        return maxErrorGrid
    }

    private fun fillGrid(grid: Grid) {
        val averageColorRgb = grid.averageColor(target)
        canvasGraphics.color = Color(averageColorRgb.r, averageColorRgb.g, averageColorRgb.b)
        canvasGraphics.fillRect(grid.location.x, grid.location.y, grid.location.width, grid.location.height)
    }

}