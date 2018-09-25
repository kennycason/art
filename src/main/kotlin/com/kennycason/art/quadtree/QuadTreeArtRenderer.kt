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

    private val fileName =
            "profile.jpg"
//            "bulbasaur.bmp"
//            "sprout_social.png"
//            "flower.jpg"
//            "space_needle.jpg"
//            "jing.jpg"
//            "moon_apollo.jpg"
//            "moon_first_small.jpg"
    private val target: BufferedImage = ImageIO.read(Thread.currentThread().contextClassLoader.getResource(
        fileName
))

    private val maxIterations = 2500
    private val minSplitSize = 2
    private val width = target.width
    private val height = target.height
    private val saveOutput = true
    private val saveOutputFrequency = 25
    private val quadTree = QuadTree(target, minSplitSize = minSplitSize, randomProbability = 5)

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

        (0 until maxIterations).forEach {
            println("i $i")

            quadTree.handle()

            panel.repaint() // must redraw as that's what actually draws to the canvas
            i++
        }
        ImageIO.write(quadTree.canvas, "png", File("output/quad_tree/${fileName.split(".")[0]}_${System.currentTimeMillis()}.png"))
    }
}

