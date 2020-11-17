package machine

import java.util.*

fun main() {
    CoffeeShop().beginWork()
}

enum class CoffeeType(val waterPerCup: Int, val milkPerCup: Int, val beansPerCup: Int, val price: Int) {
    ESPRESSO(250, 0, 16, 4),
    LATTE(350, 75, 20, 7),
    CAPPUCCINO(200, 100, 12, 6)
}

class CoffeeShop {
    private val scanner = Scanner(System.`in`)
    private val coffeeMachine = CoffeeMachine()

    fun beginWork() {
        configureCoffeeMachine()
        while (true) {
            print("Write action (buy, fill, take, remaining, exit): ")
            val action = scanner.next()!!
            if (action == "exit") {
                break
            }
            makeAction(action)
        }
    }

    private fun configureCoffeeMachine() {
        coffeeMachine.setResources(400, 540, 120, 550, 9)
    }

    private fun printCoffeeMachineState() {
        coffeeMachine.printState()
    }

    private fun makeAction(action: String) {
        println()
        when (action) {
            "buy" -> buyACupOfCoffee()
            "fill" -> fillCoffeeMachineWithResources()
            "take" -> takeCashFromCoffeeMachine()
            "remaining" -> printCoffeeMachineState()
        }
        println()
    }

    private fun buyACupOfCoffee() {
        print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
        when (scanner.next()) {
            "1" -> coffeeMachine.buy(CoffeeType.ESPRESSO)
            "2" -> coffeeMachine.buy(CoffeeType.LATTE)
            "3" -> coffeeMachine.buy(CoffeeType.CAPPUCCINO)
            "back" -> return
        }
    }

    private fun fillCoffeeMachineWithResources() {
        print("Write how many ml of water do you want to add: ")
        val water = scanner.nextInt()
        print("Write how many ml of milk do you want to add: ")
        val milk = scanner.nextInt()
        print("Write how many grams of coffee beans do you want to add: ")
        val beans = scanner.nextInt()
        print("Write how many disposable cups of coffee do you want to add: ")
        val cups = scanner.nextInt()

        coffeeMachine.addResources(water, milk, beans, cups)
    }

    private fun takeCashFromCoffeeMachine() {
        println("I gave you \$${coffeeMachine.getCash()}")
    }
}

class CoffeeMachine() {
    private var waterCount: Int = 0
    private var milkCount: Int = 0
    private var beansCount: Int = 0
    private var cash: Int = 0
    private var disposableCups: Int = 0

    fun setResources(_waterCount: Int, _milkCount: Int, _beansCount: Int, _cash: Int, _disposableCups: Int) {
        waterCount = _waterCount
        milkCount = _milkCount
        beansCount = _beansCount
        cash = _cash
        disposableCups = _disposableCups
    }

    fun addResources(_waterCount: Int, _milkCount: Int, _beansCount: Int, _disposableCups: Int) {
        waterCount += _waterCount
        milkCount += _milkCount
        beansCount += _beansCount
        disposableCups += _disposableCups
    }

    fun calcResourcesNeededForCups(countOfCups: Int, coffeeType: CoffeeType) {
        println("For $countOfCups of coffee you will need:")
        println("${countOfCups * coffeeType.waterPerCup} ml of water")
        println("${countOfCups * coffeeType.milkPerCup} ml of milk")
        println("${countOfCups * coffeeType.beansPerCup} g of coffee beans")
    }

    fun printState() {
        println("The coffee machine has:")
        println("$waterCount of water")
        println("$milkCount of milk")
        println("$beansCount of coffee beans")
        println("$disposableCups of disposable cups")
        println("$cash of money")
    }

    fun checkIfAvailableResourcesForNumberOfCups(numberOfCups: Int, coffeeType: CoffeeType) {
        val maxNumberOfCups = getMaximumNumberOfCups(coffeeType)
        when {
            maxNumberOfCups == numberOfCups -> {
                println("Yes, I can make that amount of coffee")
            }
            maxNumberOfCups > numberOfCups -> {
                println("Yes, I can make that amount of coffee (and even $maxNumberOfCups more than that)")
            }
            else -> {
                println("No, I can make only $maxNumberOfCups cups of coffee")
            }
        }
    }

    fun getMaximumNumberOfCups(coffeeType: CoffeeType): Int {
        val waterPortions = if (coffeeType.waterPerCup == 0) Int.MAX_VALUE else waterCount / coffeeType.waterPerCup
        val milkPortions = if (coffeeType.milkPerCup == 0) Int.MAX_VALUE else milkCount / coffeeType.milkPerCup
        val beansPortions = if (coffeeType.beansPerCup == 0) Int.MAX_VALUE else beansCount / coffeeType.beansPerCup

        return listOf(waterPortions, milkPortions, beansPortions, disposableCups).minOrNull() ?: 0
    }

    fun buy(coffeeType: CoffeeType) {
        when {
            waterCount < coffeeType.waterPerCup -> println("Sorry, not enough water!")
            milkCount < coffeeType.milkPerCup -> println("Sorry, not enough milk!")
            beansCount < coffeeType.beansPerCup -> println("Sorry, not enough coffee beans!")
            disposableCups == 0 -> println("Sorry, not enough disposable cups!")
            else -> {
                println("I have enough resources, making you a coffee!")
                waterCount -= coffeeType.waterPerCup
                milkCount -= coffeeType.milkPerCup
                beansCount -= coffeeType.beansPerCup
                cash += coffeeType.price
                --disposableCups
            }
        }
    }

    fun getCash(): Int {
        val result = cash
        cash = 0
        return result
    }
}
