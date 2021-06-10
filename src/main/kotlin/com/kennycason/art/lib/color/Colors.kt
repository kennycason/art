package com.kennycason.art.lib.color

import java.awt.Color

object Colors {
    fun rgbIntToRgb(rgb: Int) = Rgb(
            r = (rgb shr 16) and 0xFF,
            g =   (rgb shr 8) and 0xFF,
            b = rgb and 0xFF)

//    fun rgbToRgbInt(rgb: Rgb) = rgb.b or (rgb.g shl 8) or (rgb.r shl 16)
    fun rgbToRgbInt(rgb: Rgb) = Color(rgb.r, rgb.g, rgb.b).rgb

}