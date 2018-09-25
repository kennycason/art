package com.kennycason.art.quadtree

import java.awt.Rectangle
import java.awt.image.BufferedImage

class Grid(
        var location: Rectangle,
        var nw: Grid? = null,
        var ne: Grid? = null,
        var sw: Grid? = null,
        var se: Grid? = null) {

    fun isChild() = nw == null && ne == null && sw == null && se == null

    fun size() = location.width * location.height

    fun averageColor(target: BufferedImage): Rgb {
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
        val size = size()
        return Rgb(
                (totalR / size.toFloat()).toInt(),
                (totalG / size.toFloat()).toInt(),
                (totalB / size.toFloat()).toInt())
    }

    fun squaredPixelError(target: BufferedImage, canvas: BufferedImage): Double {
        var totalR = 0.0
        var totalG = 0.0
        var totalB = 0.0

        (0 until location.width).forEach { x ->
            (0 until location.height).forEach { y ->
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

    private fun rgbIntToRgb(rgb: Int) = Rgb(
            r = (rgb shr 16) and 0xFF,
            g =   (rgb shr 8) and 0xFF,
            b = rgb and 0xFF)

    fun rgbToRgbInt(rgb: Rgb) = rgb.b or (rgb.g shl 8) or (rgb.r shl 16)
}
