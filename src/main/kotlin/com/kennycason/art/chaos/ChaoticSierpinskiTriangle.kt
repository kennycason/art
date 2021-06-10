package com.kennycason.art.chaos

import com.kennycason.art.lib.color.SmoothColorizer
import com.kennycason.art.lib.geometry.Point
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.lang.IllegalStateException
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import kotlin.math.sqrt
import kotlin.random.Random

// https://www.reddit.com/r/HighStrangeness/comments/nvuzak/were_living_in_a_simulation/
fun main(args: Array<String>) {
    ChaoticSierpinskiTriangle().run()
}

class ChaoticSierpinskiTriangle {
    private val width = 800
    private val height = 800
    private val canvas: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    private val scale = Point(600f, 600f)
    private val colorizer = SmoothColorizer()

    private val canvasGraphics = canvas.graphics

    val a = Point(0f * scale.x, 0f * scale.y)
    val b = Point(1f * scale.x, 0f * scale.y)
    val c = Point(0.5f * scale.x, sqrt(3.0 / 2.0).toFloat() * scale.y) // height of unit equilateral triangle

    val start = Point((a.x + b.x + c.x) / 3f, (a.y + b.y + c.y) / 3f)
    var current = Point(start.x, start.y)
    val renderOffset = Point(100f, 30f)
    var i = 0

    private fun pickRandomPoint() = when (Random.nextInt(3)) {
        0 -> a
        1 -> b
        2 -> c
        else -> throw IllegalStateException("n must be in range [0, 3]")
    }

    private fun midPoint(a: Point, b: Point) = Point((a.x + b.x) / 2f, (a.y + b.y) / 2f)

    private fun iterate() {
        val point = pickRandomPoint()
        val midPoint = midPoint(current, point)
        current = midPoint

//      canvasGraphics.color = Color.WHITE
        canvasGraphics.color = colorizer.apply(i)
        canvasGraphics.fillRect(
            (renderOffset.x + current.x).toInt(),
            (height - current.y - renderOffset.y).toInt(),
            1, 1
        )

        val dim = (Random.nextFloat() * 4).toInt()
        canvasGraphics.drawOval(
            (renderOffset.x + current.x).toInt(),
            (height - current.y - renderOffset.y).toInt(),
            dim, dim
        )

        i++
    }

    fun run() {
        val frame = JFrame()
        frame.setBounds(100, 100, 800, 600)
        frame.isResizable = false
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(width, height + 18)
        frame.isVisible = true

        // initialize black screen
        canvasGraphics.color = Color.BLACK
        canvasGraphics.clearRect(0, 0, width, height)

        val panel = object : JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                iterate()
                g.drawImage(canvas, 0, 0, this)
            }
        }
        frame.add(panel)
        panel.revalidate()

        while (true) {
            panel.repaint()
        }
    }

}
