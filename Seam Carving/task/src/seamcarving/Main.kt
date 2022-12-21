package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    val width = getInteger("Enter rectangle width:")
    val height = getInteger("Enter rectangle height:")

    val fileName = getString("Enter output image name:")
    val fileFormat = "png";

    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val graphics = image.graphics

    graphics.color = Color.red
    graphics.drawLine(0, 0, width - 1, height - 1)
    graphics.drawLine(width - 1, 0, 0, height - 1)

    ImageIO.write(image, fileFormat, File(fileName))
}

fun getString(text: String): String {
    println(text)
    return readLine()!!
}

fun getInteger(text: String) = getString(text).toInt()