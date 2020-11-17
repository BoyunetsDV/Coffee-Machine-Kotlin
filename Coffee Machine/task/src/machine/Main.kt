package machine

import java.util.*

fun main() {
    println("Write how many cups of coffee you will need:")
    val scanner = Scanner(System.`in`)
    val cupsCount = scanner.nextInt()
    CoffeMachine().calculateResources(cupsCount)
}

class CoffeMachine {
    companion object {
        private const val WATER_PER_CUP = 200
        private const val MILK_PER_CUP = 50
        private const val BEANS_PER_CUP = 15
    }

    fun calculateResources(countOfCups: Int) {
        println("For $countOfCups of coffee you will need:")
        println("${countOfCups * WATER_PER_CUP} ml of water")
        println("${countOfCups * MILK_PER_CUP} ml of milk")
        println("${countOfCups * BEANS_PER_CUP} g of coffee beans")
    }
}
