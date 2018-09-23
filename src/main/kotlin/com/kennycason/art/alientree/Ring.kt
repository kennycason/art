package com.kennycason.art.alientree

import java.awt.Color

// represents a single ring in the tree
data class Ring(
        val i: Int, // the current iteration, or "age" tree.
        // the center of the ring and the ring's diameter
        val center: Point,
        val diameter: Float,
        // the velocity vector the tree ring is growing in
        val v: Point,
        // the rate at which the rings' diameters "shrink"
        val shrinkRate: Float,
        // the iteration in which the ring will "branch", or add a new ring to grow from.
        val iToBranch: Int,
        val color: Color,
        val length: Int = iToBranch - i
)