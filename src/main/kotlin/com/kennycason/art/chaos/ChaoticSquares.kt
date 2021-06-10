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
    ChaoticSquares().run()
}

class ChaoticSquares {
    private val width = 512
    private val height = 512
    private val canvas: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    private val scale = 512f

    private val canvasGraphics = canvas.graphics

    var i = 0
    val a = Point(0f * scale, 0f * scale)
    val b = Point(1f * scale, 0f * scale)
    val c = Point(1f * scale, 1f * scale)
    val d = Point(0f * scale, 1f * scale)
    val renderOffset = Point(width / 4f, height / 4f)

    val start = Point((a.x + b.x + c.x + d.x) / 4f, (a.y + b.y + c.y + d.y) / 4f)
    var current = Point(start.x, start.y)

    private fun pickRandomPoint() = when (Random.nextInt(4)) {
        0 -> a
        1 -> b
        2 -> c
        3 -> d
        else -> throw IllegalStateException("n must be in range [0, 7]")
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
