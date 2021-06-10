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
import kotlin.random.Random

// https://www.reddit.com/r/HighStrangeness/comments/nvuzak/were_living_in_a_simulation/
fun main(args: Array<String>) {
    ChaoticSierpinskiTriangleRandomVertices().run()
}

class ChaoticSierpinskiTriangleRandomVertices {
    private val width = 512
    private val height = 512
    private val canvas: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    private val scale = 512f
    private val colorizer = SmoothColorizer()
    private val canvasGraphics = canvas.graphics

    var i = 0
    val a = Point(Random.nextFloat() * scale, Random.nextFloat() * scale)
    val b = Point(Random.nextFloat() * scale, Random.nextFloat() * scale)
    val c = Point(Random.nextFloat() * scale, Random.nextFloat() * scale)

    val start = Point((a.x + b.x + c.x) / 3f, (a.y + b.y + c.y) / 3f)
    var current = Point(start.x, start.y)

    private fun pickRandomPoint() = when (Random.nextInt(3)) {
        0 -> a
        1 -> b
        2 -> c
        else -> throw IllegalStateException("n must be in range [0, 3]")
    }

    private fun midPoint(a: Point, b: Point) = Point((a.x + b.x) / 2f, (a.y + b.y) / 2f)

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
                val midPoint = midPoint(current, point)
                current = midPoint

                canvasGraphics.color = colorizer.apply(i)
                canvasGraphics.fillRect(current.x.toInt(), height - current.y.toInt(), 1, 1)

                g.drawImage(canvas, 0, 0, this)
                i++
            }
        }
        frame.add(panel)
        panel.revalidate()

        while (true) {
            panel.repaint()
        }
    }

}
