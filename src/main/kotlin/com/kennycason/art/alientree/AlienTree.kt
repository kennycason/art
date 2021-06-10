package com.kennycason.art.alientree

import com.kennycason.art.lib.geometry.Point
import java.awt.Color
import java.awt.Graphics
import java.util.*

class AlienTree(initialRing: Ring,
                private val minDiameter: Float = 5f) {
    private val random = Random()
    private val ringsToDraw = mutableListOf<Ring>()

    init {
        ringsToDraw.add(initialRing)
    }

    // handle a single iteration
    fun handle(g: Graphics) {
        val newRingsToDraw = mutableListOf<Ring>()
        ringsToDraw.forEach { ring ->
            println(ring.length)
            handle(g, ring, newRingsToDraw)
        }

        ringsToDraw.clear()
        ringsToDraw.addAll(newRingsToDraw)
    }

    private fun handle(g: Graphics, ring: Ring, newRingsToDraw: MutableList<Ring>) {
        if (ring.diameter < minDiameter) { return }

        g.color = ring.color
        g.fillOval(ring.center.x.toInt(), ring.center.y.toInt(), ring.diameter.toInt(), ring.diameter.toInt())

        newRingsToDraw.add(
                Ring(
                        i = ring.i + 1,
                        center = Point(ring.center.x + ring.v.x, ring.center.y + ring.v.y),
                        diameter = ring.diameter - ring.shrinkRate,
                        v = ring.v,
                        iToBranch = ring.iToBranch,
                        shrinkRate = ring.shrinkRate,
                        color = ring.color,
                        length = ring.length
                )
        )

        if (ring.i >= ring.iToBranch) {
            newRingsToDraw.add(
                    Ring(
                            i = ring.i + 1,
                            center = Point(ring.center.x + ring.v.x, ring.center.y + ring.v.y),
                            diameter = ring.diameter - ring.shrinkRate - 3,
                            v = Point((random.nextFloat() - 0.5f) * 2f, (random.nextFloat() - 0.5f) * 2f),
                            iToBranch = ring.i + (ring.length * .6f).toInt() + random.nextInt(150),
                            shrinkRate = ring.shrinkRate,
                            color = Color(random.nextFloat(), random.nextFloat(), random.nextFloat())
                    )
            )
        }
    }
}