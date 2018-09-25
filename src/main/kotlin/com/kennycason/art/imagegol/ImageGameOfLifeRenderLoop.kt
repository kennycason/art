package com.kennycason.art.imagegol

import com.kennycason.art.util.Colors
import com.kennycason.art.util.Rgb
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

fun main(args: Array<String>) {
    ImageGameOfLifeRenderLoop().run()
}

class ImageGameOfLifeRenderLoop {
    private val random = Random()
    private enum class Algo {
        GOL,
        BLUR,
        RANDOM_SWAP,
        SORT_SWAP
    }
    private val saveOutput = false
    private val saveOutputFrequency = 100
    private val algo = Algo.GOL
    private val fileName =
            "profile_512px.jpg"
    //            "bulbasaur.bmp"
//            "sprout_social.png"
//            "flower.jpg"
//            "space_needle.jpg"
//            "jing.jpg"
//            "moon_apollo.jpg"
//            "moon_first.jpg"
//            "jupiter.jpg"
    private var fg: BufferedImage = ImageIO.read(Thread.currentThread().contextClassLoader.getResource(fileName))
    private var bg: BufferedImage = BufferedImage(fg.width, fg.height, BufferedImage.TYPE_INT_ARGB)
    private val width = fg.width
    private val height = fg.height

    fun run() {
        val frame = JFrame()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(width, height + 18)
        frame.isVisible = true

        var i = 0
        val panel = object: JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)

                iterate()
                g.drawImage(fg, 0, 0, width, height, this)

                if (saveOutput && (i % saveOutputFrequency == 0) && i > 0) {
                    ImageIO.write(fg, "png",
                            File("output/image_gol/${fileName}_${algo.toString().toLowerCase()}_$i.png"))
                }

                val tmp = fg
                fg = bg
                bg = tmp
                i++
                println(i)
            }
        }
        frame.add(panel)
        panel.revalidate()

        while (true) {
            panel.repaint() // must redraw as that's what actually draws to the canvas
        }
    }

    private fun iterate() = when (algo) {
        Algo.GOL -> gol()
        Algo.BLUR -> blur()
        Algo.RANDOM_SWAP -> randomSwap()
        Algo.SORT_SWAP -> sortSwap()
    }

    private fun gol() {
        // draw shit
        (0 until fg.width).forEach { x ->
            (0 until fg.height).forEach { y ->
                val neighbors = getNeighbors(x, y)
                val isAlive = neighbors > 1 // means the current pixel is lowest "dead"
                if (isAlive) {
                    if (neighbors > 3 || neighbors < 2) {
                        bg.setRGB(x, y, Colors.rgbToRgbInt(getMinNeighbor(x, y)))
                    }
                    else {
                        bg.setRGB(x, y, fg.getRGB(x, y))
                    }
                } else {
                    if (neighbors == 3) { // come back to life
                        bg.setRGB(x, y, Colors.rgbToRgbInt(getAvgNeighbor(x, y)))
                    }
                    else {
                         bg.setRGB(x, y, fg.getRGB(x, y))
                    }
                }
            }
        }
    }

    private fun blur() {
        // draw shit
        (0 until fg.width).forEach { x ->
            (0 until fg.height).forEach { y ->
                bg.setRGB(x, y, Colors.rgbToRgbInt(getAvgNeighbor(x, y)))
            }
        }
    }

    private fun randomSwap() {
        (0 until fg.width).forEach { x ->
            (0 until fg.height).forEach { y ->
                bg.setRGB(x, y, geti(x, y))
            }
        }

        (0 until 2500).forEach {
            val x = random.nextInt(width)
            val y = random.nextInt(height)
            val dx = random.nextInt(3) - 1
            val dy = random.nextInt(3) - 1
            setBg(x, y, geti(x + dx, y + dy))
            setBg(x + dx, y + dy, geti(x, y))
        }
    }

    private fun sortSwap() {
        (0 until fg.width).forEach { x ->
            (0 until fg.height).forEach { y ->
                bg.setRGB(x, y, geti(x, y))
            }
        }

        (0 until 2500).forEach {
            val x = random.nextInt(width)
            val y = random.nextInt(height)
            val dx = random.nextInt(3) - 1
            val dy = random.nextInt(3) - 1
            if (geti(x + dx, y + dy) > geti(x, y)) {
                setBg(x, y, geti(x + dx, y + dy))
                setBg(x + dx, y + dy, geti(x, y))
            }
        }
    }

    // count number of alive cells. "alive" is defined be being greater than current pixel
    private fun getNeighbors(x: Int, y: Int): Int {
        var alive = 0
        val point = get(x, y)
        (-1.. 1).forEach { dx ->
            (-1.. 1).forEach { dy ->
                if (dx == 0 && dy == 0) { return@forEach }
                if (isAlive(point, get(x + dx, y + dy))) {
                    alive++
                }
            }
        }
        return alive
    }

    private fun getMaxNeighbor(x: Int, y: Int): Rgb {
        var maxNeighbor = Rgb(0, 0, 0)
        var maxNeighborScore = 0.0
        (-1.. 1).forEach { dx ->
            (-1.. 1).forEach { dy ->
                if (dx == 0 && dy == 0) { return@forEach }
                val point = get(x + dx, y + dy)
                val score = score(point)
                if (score > maxNeighborScore) {
                    maxNeighborScore = score
                    maxNeighbor = point
                }
            }
        }
        return maxNeighbor
    }

    private fun getMinNeighbor(x: Int, y: Int): Rgb {
        var minNeighbor = Rgb(255, 255, 255)
        var minNeighborScore = Double.MAX_VALUE
        (-1.. 1).forEach { dx ->
            (-1.. 1).forEach { dy ->
                if (dx == 0 && dy == 0) { return@forEach }
                val point = get(x + dx, y + dy)
                val score = score(point)
                if (score < minNeighborScore) {
                    minNeighborScore = score
                    minNeighbor = point
                }
            }
        }
        return minNeighbor
    }

    private fun getAvgNeighbor(x: Int, y: Int): Rgb {
        var totalNeighbor = Rgb(0, 0, 0)
        (-1.. 1).forEach { dx ->
            (-1.. 1).forEach { dy ->
                if (dx == 0 && dy == 0) { return@forEach }
                val point = get(x + dx, y + dy)
                totalNeighbor.r += point.r
                totalNeighbor.g += point.g
                totalNeighbor.b += point.b
            }
        }
        totalNeighbor.r /= 8
        totalNeighbor.g /= 8
        totalNeighbor.b /= 8
        return totalNeighbor
    }

    private fun score(point: Rgb) = (point.r + point.g + point.b) / 3.0

    private fun isAlive(point: Rgb, neighbor: Rgb) = score(neighbor) > score(point)

    private fun get(x: Int, y: Int) = Colors.rgbIntToRgb(fg.getRGB((x + width) % width, (y + height) % height))
    private fun geti(x: Int, y: Int) = fg.getRGB((x + width) % width, (y + height) % height)
    private fun setBg(x: Int, y: Int, rgb: Int) = bg.setRGB((x + width) % width, (y + height) % height, rgb)
}
