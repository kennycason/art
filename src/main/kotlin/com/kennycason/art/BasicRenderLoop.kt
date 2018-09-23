package com.kennycason.art

import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

fun main(args: Array<String>) {
    BasicRenderLoop().run()
}

class BasicRenderLoop {
    private val maxIterations = 150
    private val width = 800
    private val height = 600
    private val saveOutput = true
    private val saveOutputFrequency = 25
    private val canvas: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    private val canvasGraphics = canvas.graphics

    fun run() {
        val frame = JFrame()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(width, height + 18)
        frame.isVisible = true

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

            canvasGraphics.color = Color.BLACK
            canvasGraphics.clearRect(0, 0, width, height)

            // draw shit

            panel.repaint() // must redraw as that's what actually draws to the canvas
            i++
        }
        ImageIO.write(canvas, "png", File("/tmp/tree_final.png"))
    }

}
