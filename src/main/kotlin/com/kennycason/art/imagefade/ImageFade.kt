package com.kennycason.art.imagefade

import java.awt.Color
import java.awt.Graphics
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    ImageFader().run()
}

class ImageFader {

    private val images: List<BufferedImage> = listOf(
            "IMG_0160.PNG",
            "IMG_0182.PNG",
            "IMG_0187.jpg",
            "IMG_0189.jpg",
            "IMG_0192.jpg",
            "IMG_0223.PNG"
    ).map {
        ImageIO.read(Thread.currentThread().contextClassLoader.getResource(it))
    }.map {
        val newWidth = it.width / 4
        val newHeight = it.height / 4
        println("resizing to: $newWidth x $newHeight")
        val resized = BufferedImage(newWidth, newHeight, it.getType())
        val g = resized.createGraphics()
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        g.drawImage(it, 0, 0, newWidth, newHeight, 0, 0, it.getWidth(), it.getHeight(), null)
        g.dispose()
        resized
    }

    private val width = images.first().width
    private val height = images.first().height
    private val faded = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

    private val saveOutput = true
    private val saveOutputFrequency = 5

    fun run() {
        var i = 0
        var j = 1
        var iterations = 0
        var fromPercent = 1.0
        var toPercent = 0.0

        val frame = JFrame()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(faded.width, faded.height + 18)
        frame.isVisible = true

        val panel = object : JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)

//                val from = images[i]
//                val to = images[j]

                g.color = Color.BLACK
                g.clearRect(0, 0, width, height)

//                (0 until faded.width).forEach { x ->
//                    (0 until faded.height).forEach { y ->
//
//                        val fromArgb = from.getRGB(x, y)
//                        val fromAlpha: Int = (fromArgb shr 24) and 0xff
//                        val fromRed: Int = (fromArgb shr 16) and 0xff
//                        val fromGreen: Int = (fromArgb shr 8) and 0xff
//                        val fromBlue: Int = fromArgb and 0xff
//
//                        val toArgb = to.getRGB(x, y)
//                        val toAlpha: Int = (toArgb shr 24) and 0xff
//                        val toRed: Int = (toArgb shr 16) and 0xff
//                        val toGreen: Int = (toArgb shr 8) and 0xff
//                        val toBlue: Int = toArgb and 0xff
//
//                        val alpha: Int = toAlpha // (((fromAlpha * fromPercent) + (toAlpha * toPercent)) / 2.0).toInt()
//                        val red: Int = (((fromRed * fromPercent) + (toRed * toPercent)) / 2.0).toInt()
//                        val green: Int = (((fromGreen * fromPercent) + (toGreen * toPercent)) / 2.0).toInt()
//                        val blue: Int = (((fromBlue * fromPercent) + (toBlue * toPercent)) / 2.0).toInt()
//
//                        val fadedArgb = (alpha shl 24) or (red shl 16) or (green shl 8) or (blue)
//
//                        faded.setRGB(x, y, fadedArgb)
//                    }
//                }

                g.drawImage(faded, 0, 0, width, height, this)

                if (saveOutput && (iterations % 1 == 5)) {
                //   ImageIO.write(faded, "png", File("/tmp/iteration_${iterations}.png"))
                }
            }
        }
        frame.add(panel)
        panel.revalidate()

        var ii = 0
        i = 0
        j = 1
        while (true) {
            (0 until 101 step 5).forEach { percent ->
                println(percent)
                fromPercent = (100.0 - percent) / 100.0
                toPercent = percent / 100.0
                println("$fromPercent% -> $toPercent%")

                val from = images[i]
                val to = images[j]

                val g = faded.graphics

                g.color = Color.BLACK
                g.clearRect(0, 0, width, height)

                fun fc(x: Int, y: Int, c: Int) : Double = sin(x.toDouble() * (c / 255.0)) - cos(y.toDouble() * (c / 255.0))
                fun clamp(fc: Double, min: Double, max: Double): Double = (fc - min) / (max - min)
//                fun clamp(fc: Double, min: Double, max: Double): Double = min(255.0, max(0.0, fc))
                fun toRgb(x: Double): Int = (x * 255.0).toInt()

                (0 until faded.width).forEach { x ->
                    (0 until faded.height).forEach { y ->

                        val fromArgb = from.getRGB(x, y)
                        val fromAlpha: Int = (fromArgb shr 24) and 0xff
                        val fromRed: Int = (fromArgb shr 16) and 0xff
                        val fromGreen: Int = (fromArgb shr 8) and 0xff
                        val fromBlue: Int = fromArgb and 0xff

                        val toArgb = to.getRGB(x, y)
                        val toAlpha: Int = (toArgb shr 24) and 0xff
                        val toRed: Int = (toArgb shr 16) and 0xff
                        val toGreen: Int = (toArgb shr 8) and 0xff
                        val toBlue: Int = toArgb and 0xff

                        val alpha: Int = toAlpha // (((fromAlpha * fromPercent) + (toAlpha * toPercent)) / 2.0).toInt()
                        val red: Int = (((fromRed * fromPercent) + (toRed * toPercent)) / 2.0).toInt()
                        val green: Int = (((fromGreen * fromPercent) + (toGreen * toPercent)) / 2.0).toInt()
                        val blue: Int = (((fromBlue * fromPercent) + (toBlue * toPercent)) / 2.0).toInt()

                        val fRed = toRgb(clamp(fc(x, y, red), -1.0, 1.0))
                        val fGreen = toRgb(clamp(fc(x, y, green), -1.0, 1.0))
                        val fBlue = toRgb(clamp(fc(x, y, blue), -1.0, 1.0))

                        val fadedRgb = (fRed shl 16) or (fGreen shl 8) or (fBlue)
                        val fadedArgb = (alpha shl 24) or fadedRgb

                        faded.setRGB(x, y, fadedArgb)
                    }
                }

                if (saveOutput && (iterations % 1 == 0)) {
                   // ImageIO.write(faded, "png", File("/tmp/iteration_${iterations}.png"))
                }
                iterations++

                panel.repaint() // must redraw as that's what actually draws to the canvas
                Thread.sleep(100)
            }

            if (ii < images.size - 2)  {
                ii++
            }
            else {
                exitProcess(0)
                ii = 0
            }
            i = ii
            j = ii + 1

        }
    }

}

