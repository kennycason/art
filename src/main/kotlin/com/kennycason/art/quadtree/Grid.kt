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
        val totalRgb = Rgb(0, 0,0)

        (0 until location.width).forEach { x ->
            (0 until location.height).forEach { y ->
                val rgb = rgbIntToRgb(target.getRGB(location.x + x, location.y + y))
                //println("rgb: ${rgbIntToRgb(target.getRGB(location.x + x, location.y + y))}")
                totalRgb.r += rgb.r
                totalRgb.g += rgb.g
                totalRgb.b += rgb.b
            }
        }

        // compute avg
        val size = size()
        totalRgb.r /= size
        totalRgb.g /= size
        totalRgb.b /= size

        return totalRgb
    }

    fun averagePixelError(target: BufferedImage, canvas: BufferedImage): Double {
        var totalError = Rgb(0, 0,0)
        val averageColor = averageColor(target)

        (0 until location.width).forEach { x ->
            (0 until location.height).forEach { y ->
                val rgb = rgbIntToRgb(canvas.getRGB(location.x + x, location.y + y))
                totalError.r += (rgb.r - averageColor.r) * (rgb.r - averageColor.r)
                totalError.r += (rgb.g - averageColor.g) * (rgb.g - averageColor.g)
                totalError.b += (rgb.b - averageColor.b) * (rgb.b - averageColor.b)
            }
        }

        // compute avg
        val size = size()
        return ((totalError.r / size.toFloat()) +
                (totalError.g / size.toFloat()) +
                (totalError.b / size.toFloat())) / 3.0
    }

    private fun rgbIntToRgb(rgb: Int) = Rgb(
            rgb and 0xFF,
            (rgb shr 8) and 0xFF,
            (rgb shr 16) and 0xFF)

    fun rgbToRgbInt(rgb: Rgb) = rgb.b or (rgb.g shl 8) or (rgb.r shl 16)
}
