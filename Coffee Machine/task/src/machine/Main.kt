package machine

import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)
    println("Write how many ml of water the coffee machine has: ")
    val waterCount = scanner.nextInt()
    println("Write how many ml of milk the coffee machine has: ")
    val milkCount = scanner.nextInt()
    println("Write how many grams of coffee beans the coffee machine has: ")
    val beansCount = scanner.nextInt()

    println("Write how many cups of coffee you will need: ")
    val cupsCount = scanner.nextInt()

    val coffeeMachine = CoffeeMachine()
    coffeeMachine.setResources(waterCount, milkCount, beansCount)
    coffeeMachine.checkIfAvailableNumberOfCups(cupsCount)
}

class CoffeeMachine() {
    private var waterCount: Int = 0
    private var milkCount: Int = 0
    private var beansCount: Int = 0

    companion object {
        private const val WATER_PER_CUP = 200
        private const val MILK_PER_CUP = 50
        private const val BEANS_PER_CUP = 15
    }

    fun setResources(water: Int, milk: Int, beans: Int) {
        waterCount = water
        milkCount = milk
        beansCount = beans
    }

    fun calcResourcesNeededForNumberForCups(countOfCups: Int) {
        println("For $countOfCups of coffee you will need:")
        println("${countOfCups * WATER_PER_CUP} ml of water")
        println("${countOfCups * MILK_PER_CUP} ml of milk")
        println("${countOfCups * BEANS_PER_CUP} g of coffee beans")
    }

    fun checkIfAvailableNumberOfCups(numberOfCups: Int) {
        val maxNumberOfCups = getMaximumNumberOfCups()
        when {
            maxNumberOfCups == numberOfCups -> {
                println("Yes, I can make that amount of coffee")
            }
            maxNumberOfCups > numberOfCups -> {
                println("Yes, I can make that amount of coffee (and even ${maxNumberOfCups - numberOfCups} more than that)")
            }
            else -> {
                println("No, I can make only $maxNumberOfCups cups of coffee")
            }
        }
    }

    fun getMaximumNumberOfCups(): Int {
        val waterPortions = waterCount / WATER_PER_CUP
        val milkPortions = milkCount / MILK_PER_CUP
        val beansPortions = beansCount / BEANS_PER_CUP

        return if (waterPortions < beansPortions && waterPortions < milkPortions) {
            waterPortions
        } else if (beansPortions < milkPortions) {
            beansPortions
        } else {
            milkPortions
        }
    }
}
