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
        val totalRgb = Rgb(0, 0, 0)
        // println("this: " + this.location)

        var skipped = 0
        (0 until location.width).forEach { x ->
            (0 until location.height).forEach { y ->
                val rgb = rgbIntToRgb(target.getRGB(location.x + x, location.y + y))
                // println("x: ${location.x + x}, y: ${location.y + y}, rgb: ${rgbIntToRgb(target.getRGB(location.x + x, location.y + y))}")
                totalRgb.r += rgb.r
                totalRgb.g += rgb.g
                totalRgb.b += rgb.b
            }
        }

        // compute avg
        val size = size() - skipped
        totalRgb.r /= size
        totalRgb.g /= size
        totalRgb.b /= size

        return totalRgb
    }

    fun averagePixelError(target: BufferedImage, canvas: BufferedImage): Double {
        var totalError = Rgb(0, 0,0)
//        val averageColor = averageColor(target)

        (0 until location.width).forEach { x ->
            (0 until location.height).forEach { y ->
                val canvasRgb = rgbIntToRgb(canvas.getRGB(location.x + x, location.y + y))
                val targetRgb = rgbIntToRgb(target.getRGB(location.x + x, location.y + y))
                totalError.r += (canvasRgb.r - targetRgb.r) * (canvasRgb.r - targetRgb.r)
                totalError.r += (canvasRgb.g - targetRgb.g) * (canvasRgb.g - targetRgb.g)
                totalError.b += (canvasRgb.b - targetRgb.b) * (canvasRgb.b - targetRgb.b)
            }
        }

        // compute avg
        val size = size()
        return ((totalError.r / size.toFloat()) +
                (totalError.g / size.toFloat()) +
                (totalError.b / size.toFloat())) / 3.0
    }

    private fun rgbIntToRgb(rgb: Int) = Rgb(
            r = (rgb shr 16) and 0xFF,
            g =   (rgb shr 8) and 0xFF,
            b = rgb and 0xFF)

    fun rgbToRgbInt(rgb: Rgb) = rgb.b or (rgb.g shl 8) or (rgb.r shl 16)
}
