package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.sqrt

fun main(args: Array<String>) {
    var inpFileName = ""
    var outFileName = ""
    val fileFormat = "png"

    for (i in args.indices) {
        when (args[i]) {
            "-in" -> inpFileName = args[i + 1]
            "-out" -> outFileName = args[i + 1]
        }
    }
    ImageIO.write(energy(inpFileName), fileFormat, File(outFileName))
}

fun energy(inpFileName: String): BufferedImage {
    val image = ImageIO.read(File(inpFileName))
    val energyMap = Array(image.width) { Array(image.height) { 0.0 } }
    var max = 0.0

    for (i in 0 until image.width)
        for (j in 0 until image.height) {
            val x = if (i == 0) 1 else if (i == image.width - 1) image.width - 2 else i
            val y = if (j == 0) 1 else if (j == image.height - 1) image.height - 2 else j
            val xD = gradient(Color(image.getRGB(x - 1, j)), Color(image.getRGB(x + 1, j)))
            val yD = gradient(Color(image.getRGB(i, y - 1)), Color(image.getRGB(i, y + 1)))
            val curr = sqrt((xD + yD).toDouble())
            energyMap[i][j] = curr
            if (curr > max) max = curr
        }

    for (i in 0 until image.width)
        for (j in 0 until image.height) {
            val intensity = (255.0 * energyMap[i][j] / max).toInt()
            image.setRGB(i, j, Color(intensity, intensity, intensity).rgb)
        }
    return image
}

fun pow2(i: Int) = i * i

fun gradient(clr1: Color, clr2: Color) =
    pow2(clr1.red - clr2.red) + pow2(clr1.green - clr2.green) + pow2(clr1.blue - clr2.blue)