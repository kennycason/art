package com.kennycason.art.lib.color

import java.awt.Color
import kotlin.math.sin


class SmoothColorizer(
    private val f1: Double = 0.3,
    private val f2: Double = 0.3,
    private val f3: Double = 0.3,
    private val p1: Double = 0.0,
    private val p2: Double = 2.0,
    private val p3: Double = 4.0,
    private val center: Int = 128,
    private val width: Int = 127
) : Colorizer {

    override fun apply(i: Int): Color {
        val r = sin(f1 * i + p1) * width + center
        val g = sin(f2 * i + p2) * width + center
        val b = sin(f3 * i + p3) * width + center

        return Color(r.toInt(), g.toInt(), b.toInt())
    }
}