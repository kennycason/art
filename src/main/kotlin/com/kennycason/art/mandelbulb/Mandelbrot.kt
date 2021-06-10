package com.kennycason.art.mandelbulb

import array2d
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import kotlin.random.Random

//class Mandelbrot: JFrame("Mandelbrot Set") {
//    companion object {
//        private const val MAX_ITER = 570
//        private var ZOOM = 150.0
//    }
//
//    private val img: BufferedImage
//    var inc = 1.0
//    var sx = 0.0
//    var sy = 0.0
//    init {
//        setBounds(100, 100, 800, 600)
//        isResizable = false
//        defaultCloseOperation = EXIT_ON_CLOSE
//        img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
//    }
//
//    override fun paint(g: Graphics) {
//        println("paint")
//        for (y in 0 until height) {
//            for (x in 0 until width) {
//                var zx = sx // 0.0
//                var zy = sy // 0.0
//                val cX = (x - 400) / ZOOM
//                val cY = (y - 300) / ZOOM
//                var iter = MAX_ITER
//                while (zx * zx + zy * zy < 4.0 && iter > 0) {
//                    val tmp = zx * zx - zy * zy + cX
//                    zy = 2.0 * zx * zy + cY
//                    zx = tmp
//                    iter--
//                }
//                img.setRGB(x, y, iter or (iter shl 7))
//            }
//        }
//        g.drawImage(img, 0, 0, this)
//        sx += inc
//        sy += inc
//        ZOOM += inc
//    }
//}


fun main(args: Array<String>) {
    Mandelbrot().run()
}

class Mandelbrot {
    private val width = 512
    private val height = 512
    private val canvas: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    private val pixels = array2d(512, 512, { 0 })
    private val canvasGraphics = canvas.graphics

    companion object {
        private const val MAX_ITER = 570
        private var ZOOM = 150.0
    }

    var i = 0
    var sx = 0.0
    var sy = 0.0

    var maxRGB = 0xffffff

    fun iterate() {
        println("paint: ${i++}")
        for (y in 0 until height) {
            for (x in 0 until width) {
                var zx = 0.0
                var zy = 0.0
                val cX = (x - width / 2 + sx) / ZOOM
                val cY = (y - height / 2 + sy) / ZOOM
                var iter = MAX_ITER
                while (zx * zx + zy * zy < 4.0 && iter > 0) {
                    val tmp = zx * zx - zy * zy + cX
                    zy = 2.0 * zx * zy + cY
                    zx = tmp
                    iter--
                }
                pixels[x][y] = (iter or (iter shl 7)) * 4
            }
        }
        sx -= Math.PI * 2 + 1.25
        sy -= 1.9
        ZOOM += 10
    }

    private val colors = mutableMapOf<Int, Color>()

    fun run() {
        val frame = JFrame()
        frame.setBounds(100, 100, 800, 600)
        frame.isResizable = false
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(width, height + 18)
        frame.isVisible = true

        val panel = object : JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)

                canvasGraphics.color = Color.BLACK
                canvasGraphics.clearRect(0, 0, width, height)
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        val rgb = pixels[x][y]
                        if (rgb in colors) {
                            canvasGraphics.color = colors[rgb]
                        } else {
                            if (colors.size < 2048) {
                                colors[rgb] = Color(rgb)
                            }
                            canvasGraphics.color = Color(pixels[x][y])
                        }
                        canvasGraphics.fillRect(x, y, 1, 1)
                    }
                }

                g.drawImage(canvas, 0, 0, this)

            }
        }
        frame.add(panel)
        panel.revalidate()

        while (true) {
            (0 until 3).forEach { iterate() }
//            iterate()
            panel.repaint() // must redraw as that's what actually draws to the canvas
          //  Thread.sleep(100)
        }
    }

}
