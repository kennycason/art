package com.kennycason.art.alientree

import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

fun main(args: Array<String>) {
    AlienTreeRenderer().run()
}

class AlienTreeRenderer {
    private val maxIterations = 10000
    private val width = 1200
    private val height = 1200
    private val saveOutput = true
    private val saveOutputFrequency = 25
    private val canvas: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    private val canvasGraphics = canvas.graphics

    private val baseDiameter = 100f
    private val alienTree = AlienTree(
            Ring(
                    i = 1,
                    center = Point(width / 2f - baseDiameter / 2f, height.toFloat()),
                    diameter = baseDiameter,
                    v = Point(0.02f, -1f),
                    iToBranch = 500,
                    shrinkRate = 0.095f,
                    color = Color.WHITE
            )
    )

    fun run() {
        val frame = JFrame()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(width, height + 18)
        frame.isVisible = true

        canvasGraphics.color = Color.BLACK
        canvasGraphics.clearRect(0, 0, width, height)

        var i = 0
        val panel = object: JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                g.drawImage(canvas, 0, 0, width, height, this)

                if (saveOutput && (i % saveOutputFrequency == 0)) {
                    ImageIO.write(canvas, "png", File("/tmp/iteration_$i.png"))
                }
            }
        }
        frame.add(panel)
        panel.revalidate()

        (0 until maxIterations).forEach {
            println("i: $i")
            // draw a single iteration
            alienTree.handle(canvasGraphics)

            panel.repaint() // must redraw as that's what actually draws to the canvas
            i++
        }
        ImageIO.write(canvas, "png", File("output/alien_tree_${System.currentTimeMillis()}.png"))
    }

}
