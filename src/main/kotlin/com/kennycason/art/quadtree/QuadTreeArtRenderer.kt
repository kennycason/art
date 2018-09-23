package com.kennycason.art.quadtree

import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

fun main(args: Array<String>) {
    QuadTreeArtRenderer().run()
}

class QuadTreeArtRenderer {

    private val fileName = "space_needle.jpg"
    private val target: BufferedImage = ImageIO.read(Thread.currentThread().contextClassLoader.getResource(fileName))

    private val maxIterations = 15000
    private val width = target.width
    private val height = target.height
    private val saveOutput = true
    private val saveOutputFrequency = 25
    private val quadTree = QuadTree(target)

    fun run() {
        val frame = JFrame()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(width, height + 18)
        frame.isVisible = true

        var i = 0
        val panel = object: JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                g.drawImage(quadTree.canvas, 0, 0, width, height, this)

                if (saveOutput && (i % saveOutputFrequency == 0)) {
                    ImageIO.write(quadTree.canvas, "png", File("/tmp/iteration_$i.png"))
                }
            }
        }
        frame.add(panel)
        panel.revalidate()

//        quadTree.handle()
//        ImageIO.write(quadTree.canvas, "png", File("output/bulbasaur_${System.currentTimeMillis()}.png"))
//        panel.repaint()

        (0 until maxIterations).forEach {
            println(i)
            // draw shit
            quadTree.handle()

            panel.repaint() // must redraw as that's what actually draws to the canvas
            i++
        }
        ImageIO.write(quadTree.canvas, "png", File("/tmp/tree_final.png"))
    }
}

