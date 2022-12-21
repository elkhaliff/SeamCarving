package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

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
    ImageIO.write(negativeImage(inpFileName), fileFormat, File(outFileName))
}

fun negativeImage(inpFileName: String): BufferedImage {
    val image = ImageIO.read(File(inpFileName))

    for (i in 0 until image.width)
        for (j in 0 until image.height) {
            val color = Color(image.getRGB(i, j))
            image.setRGB(i, j, Color(255 - color.red, 255 - color.green, 255 - color.blue).rgb)
        }
    return image
}