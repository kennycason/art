package com.kennycason.art.graphlife

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

fun main(args: Array<String>) {
    GraphGameOfLifeRenderLoop().run()
}

class GraphGameOfLifeRenderLoop {
    private val width = 800
    private val height = 600
    private val saveOutput = false
    private val saveOutputFrequency = 1
    private val canvas: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    private val canvasGraphics = canvas.graphics

    private val graph = GameOfLifeGraph(dimension = Dimension(width, height))

    fun run() {
        val frame = JFrame()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(width, height + 18)
        frame.isVisible = true

        var i = 0
        val panel = object: JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                canvasGraphics.color = Color.BLACK
                canvasGraphics.clearRect(0, 0, width, height)

                graph.iterate()
                graph.draw(canvasGraphics)

                g.drawImage(canvas, 0, 0, width, height, this)

                if (saveOutput && (i % saveOutputFrequency == 0)) {
                    ImageIO.write(canvas, "png", File("output/graph_gol/$i.png"))
                }
                println("i: $i")
                i++
            }
        }
        frame.add(panel)
        panel.revalidate()

        while (true) {
            panel.repaint() // must redraw as that's what actually draws to the canvas
            Thread.sleep(100)
        }
    }

}
