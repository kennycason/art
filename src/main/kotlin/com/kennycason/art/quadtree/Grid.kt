package com.kennycason.art.quadtree

import com.kennycason.art.lib.color.Colors.rgbIntToRgb
import com.kennycason.art.lib.color.Rgb
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle
import java.awt.image.BufferedImage

class Grid(
        var location: Rectangle,
        var nw: Grid? = null,
        var ne: Grid? = null,
        var sw: Grid? = null,
        var se: Grid? = null) {

    private val stepSize = 4

    val size by lazy { location.width * location.height }

    fun isChild() = nw == null && ne == null && sw == null && se == null

    fun squaredPixelError(target: BufferedImage, canvas: BufferedImage): Double {
        var totalR = 0.0
        var totalG = 0.0
        var totalB = 0.0

        (0 until location.width step stepSize).forEach { x ->
            (0 until location.height step stepSize).forEach { y ->
                val canvasRgb = rgbIntToRgb(canvas.getRGB(location.x + x, location.y + y))
                val targetRgb = rgbIntToRgb(target.getRGB(location.x + x, location.y + y))
                totalR += (canvasRgb.r - targetRgb.r) * (canvasRgb.r - targetRgb.r)
                totalG += (canvasRgb.g - targetRgb.g) * (canvasRgb.g - targetRgb.g)
                totalB += (canvasRgb.b - targetRgb.b) * (canvasRgb.b - targetRgb.b)
            }
        }

        // compute avg
        return (Math.sqrt(totalR) +
                Math.sqrt(totalG) +
                Math.sqrt(totalB)) / 3.0
    }

    fun fill(target: BufferedImage, canvasGraphics: Graphics, shape: Shape) {
        val averageColorRgb = averageColor(target)
        canvasGraphics.color = Color(averageColorRgb.r, averageColorRgb.g, averageColorRgb.b)
        when (shape) {
            Shape.RECTANGLE -> canvasGraphics.fillRect(location.x, location.y, location.width, location.height)
            Shape.OVAL -> canvasGraphics.fillOval(location.x, location.y, location.width, location.height)
        }
    }

    fun drawGridBorder(target: BufferedImage, canvasGraphics: Graphics, shape: Shape) {
        if (size >= 3) {
            canvasGraphics.color = Color.BLACK
            when (shape) {
                Shape.RECTANGLE -> canvasGraphics.drawRect(location.x, location.y, location.width, location.height)
                Shape.OVAL -> if (isChild()) { canvasGraphics.drawOval(location.x, location.y, location.width, location.height) }
            }
        }
    }

    private fun averageColor(target: BufferedImage): Rgb {
        var totalR = 0L
        var totalG = 0L
        var totalB = 0L

        (0 until location.width).forEach { x ->
            (0 until location.height).forEach { y ->
                val rgb = rgbIntToRgb(target.getRGB(location.x + x, location.y + y))
                totalR += rgb.r
                totalG += rgb.g
                totalB += rgb.b
            }
        }

        // compute avg
        return Rgb(
                (totalR / size.toFloat()).toInt(),
                (totalG / size.toFloat()).toInt(),
                (totalB / size.toFloat()).toInt())
    }

}
