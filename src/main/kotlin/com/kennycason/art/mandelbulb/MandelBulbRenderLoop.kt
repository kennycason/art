package com.kennycason.art.mandelbulb

import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

fun main(args: Array<String>) {
    MandelBulbRenderLoop().run()
}

class MandelBulbRenderLoop {
    private val width = 512
    private val height = 512
    private val canvas: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    private val canvasGraphics = canvas.graphics
    private val random = Random()
    val n = 8.0

    // render area
    val xa = -1.5
    val xb = 1.5
    val ya = -1.5
    val yb = 1.5

    val maxIterations = 256
    val pi2 = Math.PI * 2.0

    // random rotations to transform 2d plane to 3d plane
    val xy = random.nextDouble() * pi2
    val xz = random.nextDouble() * pi2
    val yz = random.nextDouble() * pi2

    val sinxy = Math.sin(xy)
    val sinxz = Math.sin(xz)
    val sinyz = Math.sin(yz)
    val cosxy = Math.cos(xy)
    val cosxz = Math.cos(xz)
    val cosyz = Math.cos(yz)

    val origX = (xa + xb) / 2.0
    val origY = (ya + yb) / 2.0

    fun run() {
        val frame = JFrame()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(width, height + 18)
        frame.isVisible = true

        val panel = object: JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                canvasGraphics.color = Color.BLACK
                canvasGraphics.clearRect(0, 0, width, height)

                for (ky in (0 until height)) {
                    println("y: $ky")
                    val b = ky * (yb - ya) / (height - 1).toDouble() + ya
                    for (kx in (0 until width)) {
                        val a = kx * (xb - xa) / (width - 1).toDouble() + xa
                        var x = a - origX
                        var y = b - origY
                        var z = 0.0

                        // 3d rotation around center of the plane
                        // xy-plane rotation
                        var x0 = x * cosxy - y * sinxy
                        y = x * sinxy + y * cosxy
                        x = x0
                        // xz-plane rotation
                        x0 = x * cosxz - z * sinxz
                        z = x * sinxz + z * cosxz
                        x = x0
                        // yz-plane rotation
                        val y0 = y * cosyz - z * sinyz
                        z = y * sinyz + z * cosyz
                        y = y0

                        x += origX
                        y += origY

                        val cx = x
                        val cy = y
                        val cz = z

                        for (i in (0 until maxIterations)) {
                            val r = Math.sqrt(x * x + y * y + z * z)
                            val t = Math.atan2(Math.hypot(x, y), z)
                            val p = Math.atan2(y, x)
                            val rn = Math.pow(r, n)
                            x = rn * Math.sin(t * n) * Math.cos(p * n) + cx
                            y = rn * Math.sin(t * n) * Math.sin(p * n) + cy
                            z = rn * Math.cos(t * n) + cz
                            if (x * x + y * y + z * z > 4.0) { break }
                            val rgb = Color((i % 4) * 64, (i % 8) * 32, (i % 16) * 16)
                            canvas.setRGB(kx, ky, rgb.rgb)
                        }
                    }
                }
                g.drawImage(canvas, 0, 0, width, height, this)
                ImageIO.write(canvas, "png", File("output/mandelbulb/mandelbulb_${System.currentTimeMillis()}.png"))
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
