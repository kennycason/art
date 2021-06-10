package com.kennycason.art.chaos

import com.kennycason.art.lib.geometry.Point
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.lang.IllegalStateException
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import kotlin.random.Random


fun main(args: Array<String>) {
    ChaoticPentagon().run()
}

class ChaoticPentagon {
    private val width = 512
    private val height = 512
    private val canvas: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    private val scale = 512f * 2

    private val canvasGraphics = canvas.graphics

    var i = 0
    // bottom
    val a = Point(0.3f * scale, 0.1f * scale)
    val b = Point(0.6f * scale, 0.1f * scale)
    // top
    val c = Point(0.45f * scale, 0.9f * scale)
    // middle
    val d = Point(0.1f * scale, 0.5f * scale)
    val e = Point(0.9f * scale, 0.5f * scale)

    val renderOffset = Point(0f, 0f)

    val start = Point((a.x + b.x + c.x + d.x + e.x) / 5f, (a.y + b.y + c.y + d.y + e.y) / 5f)
    var current = Point(start.x, start.y)

    private fun pickRandomPoint() = when (Random.nextInt(5)) {
        0 -> a
        1 -> b
        2 -> c
        3 -> d
        4 -> e
        else -> throw IllegalStateException("n must be in range [0, 4]")
    }

    private fun quarterPoint(a: Point, b: Point) = Point((a.x + b.x) / 4f, (a.y + b.y) / 4f)

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

                val point = pickRandomPoint()
                val midPoint = quarterPoint(current, point)
                current = midPoint


                canvasGraphics.color = Color.WHITE
                canvasGraphics.fillRect(
                    (renderOffset.x + current.x).toInt(),
                    (height - current.y - renderOffset.y).toInt(),
                    1, 1
                )

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
