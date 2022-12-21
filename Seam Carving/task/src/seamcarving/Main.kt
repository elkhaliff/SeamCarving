package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.Stack
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
    ImageIO.write(seam(inpFileName), fileFormat, File(outFileName))
}

fun seam(inpFileName: String): BufferedImage {
    val image = ImageIO.read(File(inpFileName))
    val energy = energy(image)

    var lowestBottom = Double.MAX_VALUE
    var oldSeamTotal = Double.MAX_VALUE
    var seamTotal: Double

    val stack = Stack<Int>()
    val oldStack = Stack<Int>()

    val col = energy[0].lastIndex
    val row = energy.lastIndex

    for (j in 1..col) {
        for (i in 0..row) {
            var boost = energy[i][j - 1]
            if (i != 0 && energy[i - 1][j - 1] < boost) boost = energy[i - 1][j - 1]
            if (i != row && energy[i + 1][j - 1] < boost) boost = energy[i + 1][j - 1]
            energy[i][j] += boost
            if (j == col && energy[i][j] < lowestBottom) lowestBottom = energy[i][j]
        }
    }

    for (i in 0..row) {
        if (energy[i][col] == lowestBottom) {
            stack.push(i)
            seamTotal = energy[i][col]
            var index = i
            for (j in col - 1 downTo 0) {
                var lowIndex = index
                if (index != 0 && energy[index - 1][j] < energy[index][j]) lowIndex -= 1
                if (index != row && energy[index + 1][j] < energy[lowIndex][j]) lowIndex = index + 1
                index = lowIndex
                stack.push(index)
                seamTotal += energy[index][j]
            }
            if (seamTotal < oldSeamTotal) {
                if (oldStack.isNotEmpty()) oldStack.clear()
                oldSeamTotal = seamTotal
                oldStack.addAll(stack)
            }
            stack.clear()
        }
    }

    for (j in 0..col) image.setRGB(oldStack.pop(), j, Color(255, 0, 0).rgb)

    return image
}

fun energy(image: BufferedImage): Array<Array<Double>> {
    val energyMap = Array(image.width) { Array(image.height) { 0.0 } }

    for (i in 0 until image.width)
        for (j in 0 until image.height) {
            val x = if (i == 0) 1 else if (i == image.width - 1) image.width - 2 else i
            val y = if (j == 0) 1 else if (j == image.height - 1) image.height - 2 else j
            val xD = gradient(Color(image.getRGB(x - 1, j)), Color(image.getRGB(x + 1, j)))
            val yD = gradient(Color(image.getRGB(i, y - 1)), Color(image.getRGB(i, y + 1)))
            val curr = sqrt((xD + yD).toDouble())
            energyMap[i][j] = curr
        }

    return energyMap
}

fun pow2(i: Int) = i * i

fun gradient(clr1: Color, clr2: Color) =
    pow2(clr1.red - clr2.red) + pow2(clr1.green - clr2.green) + pow2(clr1.blue - clr2.blue)