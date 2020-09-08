package com.kennycason.art.fractalverse

import array2d
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

fun main(args: Array<String>) {
    FractalVerse().run()
}

class FractalVerse {
    private val worldWidth = 256
    private val worldHeight = 256
    private val saveOutput = true
    private val saveOutputFrequency = 100
    private val canvas: BufferedImage = BufferedImage(worldWidth, worldHeight, BufferedImage.TYPE_INT_ARGB)
    private val canvasGraphics = canvas.graphics
    private val random = Random()

    private val buffer = array2d(worldWidth, worldHeight) { 0 }
    private val colors = array2d(worldWidth, worldHeight) { 0 }

    private val functions = arrayOf(
            Linear(),
            Sinusoidal(),
            Spherical(),
            Swirl(),
            Horseshoe(),
            Popcorn(random.nextDouble(), random.nextDouble()),
            Pdj(random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble())
    )

    private val functionColors = IntArray(functions.size) { i -> (random.nextDouble() * 0xFFFFFF).toInt() }

    fun run() {
        val frame = JFrame()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(worldWidth * 3, worldHeight * 3 + 18)
        frame.isVisible = true


		var x = 1 - 2 * Math.random()
		var y = 1 - 2 * Math.random()
        var maxCount = 0

        fun step(steps: Int) {
            var skipped = 0
            (0 until steps).forEach {
                val i = random.nextInt(functions.size)
                val fn = functions[i]

                val point = fn.apply(R2Point(x, y, 0))
                x = point.x
                y = point.y

                //println("x: $x, y: $y")
                val xInt = ((x + 1) * worldWidth / 2.0).toInt()
                val yInt = ((y + 1) * worldHeight / 2.0).toInt()

                // println("$xInt, $yInt")

                if (xInt >= 0 && xInt < worldWidth && yInt >= 0 && yInt < worldHeight) {
                    buffer[xInt][yInt]++
                }
                else {
                    skipped++
                }
            }
            //println("total skipped: $skipped")
            (0 until worldWidth).forEach { x ->
                (0 until worldHeight).forEach { y ->
                    if (buffer[x][y] > maxCount) {
                        maxCount = buffer[x][y]
                    }
                }
            }
            //println("max: $maxCount")
        }

        step(1000000)

        var i = 0
        val panel = object: JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)

                step(10000000)

                canvasGraphics.color = Color.BLACK
                canvasGraphics.clearRect(0, 0, worldWidth, worldHeight)

                (0 until worldWidth).forEach { x ->
                    (0 until worldHeight).forEach { y ->
                        val rgb = ((buffer[x][y] / maxCount.toFloat()) * 0xFFFFFF).toInt()
                        canvasGraphics.color = Color(rgb)
                        canvasGraphics.fillRect(x, y, 1, 1)
                    }
                }

                g.drawImage(canvas, 0, 0, width, height, this)

                if (saveOutput && (i % saveOutputFrequency == 0)) {
                    println("save...")
                    ImageIO.write(canvas, "png", File("/tmp/iteration_$i.png"))
                }
                i++
            }
        }
        frame.add(panel)
        panel.revalidate()



        while (true) {
            panel.repaint()
            Thread.sleep(100)
        }

    //    ImageIO.write(canvas, "png", File("/tmp/fractalverse.png"))
    }

}


data class R2Point(val x: Double, val y: Double, val c: Int)

fun r(p: R2Point) = sqrt(p.x * p.x + p.y * p.y)

interface R2Function {
    fun apply(p: R2Point): R2Point
}

class Linear : R2Function {
    override fun apply(p: R2Point) = R2Point(p.x, p.y, p.c)
}

class Sinusoidal : R2Function {
    override fun apply(p: R2Point) = R2Point(sin(p.x), sin(p.y), p.c)
}

class Spherical : R2Function {
    override fun apply(p: R2Point): R2Point {
        val r = r(p)
        val rSquared = r * r
        return R2Point(p.x / rSquared, p.y / rSquared, p.c)
    }
}

class Swirl : R2Function {
    override fun apply(p: R2Point): R2Point {
        val r = r(p)
        val rSquared = r * r
        val sinRSquared = sin(rSquared)
        val cosRSquared = cos(rSquared)
        return R2Point(p.x * sinRSquared - p.y * cosRSquared, p.x * cosRSquared + p.y * sinRSquared, p.c)
    }
}

class Horseshoe : R2Function {
    override fun apply(p: R2Point): R2Point {
        val r = r(p)
        return R2Point(((p.x - p.y) * (p.x + p.y)) / r , (2 * p.x * p.y) / r, p.c)
    }
}

class Popcorn(val a: Double, val b: Double) : R2Function {
    override fun apply(p: R2Point): R2Point {
        return R2Point(p.x + a * sin(tan(3 * p.y)), p.y + b * sin(tan(3 * p.x)), p.c)
    }
}

class Pdj(val a: Double, val b: Double, val c: Double, val d: Double) : R2Function {
    override fun apply(p: R2Point): R2Point {
        return R2Point(sin(a * p.y) - cos(b * p.x), sin(c * p.x) - cos(d * p.y), p.c)
    }
}