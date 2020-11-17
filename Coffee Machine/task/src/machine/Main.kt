package machine

import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)

    val coffeeMachine = CoffeeMachine()
    coffeeMachine.displayInitialMessage()

    while (true) {
        val command = scanner.next()
        if (command == "exit") {
            break
        }
        coffeeMachine.makeAction(command)
    }
}

enum class CoffeeType(val waterPerCup: Int, val milkPerCup: Int, val beansPerCup: Int, val price: Int) {
    ESPRESSO(250, 0, 16, 4),
    LATTE(350, 75, 20, 7),
    CAPPUCCINO(200, 100, 12, 6)
}

enum class State {
    MAIN_MENU, BUY, FILL_WATER, FILL_MILK, FILL_COFFEE_BEANS, FILL_CUPS
}


class CoffeeMachine {
    private var state: State = State.MAIN_MENU
    private var waterCount: Int = 400
    private var milkCount: Int = 540
    private var beansCount: Int = 120
    private var disposableCups: Int = 9
    private var cash: Int = 550

    fun displayInitialMessage() {
        print("Write action (buy, fill, take, remaining, exit): ")
    }

    fun makeAction(command: String) {
        when {
            state == State.MAIN_MENU && command == "buy" -> {
                println()
                print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
                state = State.BUY
            }
            state == State.MAIN_MENU && command == "remaining" -> {
                println()
                printState()
                println()
                displayInitialMessage()
            }
            state == State.MAIN_MENU && command == "take" -> {
                println()
                println("I gave you \$${getCash()}")
                println()
                displayInitialMessage()
            }
            state == State.MAIN_MENU && command == "fill" -> {
                println()
                print("Write how many ml of water do you want to add: ")
                state = State.FILL_WATER
            }
            state == State.FILL_WATER -> {
                addResource(command.toInt())
                print("Write how many ml of milk do you want to add: ")
                state = State.FILL_MILK
            }
            state == State.FILL_MILK -> {
                addResource(command.toInt())
                print("Write how many grams of coffee beans do you want to add: ")
                state = State.FILL_COFFEE_BEANS
            }
            state == State.FILL_COFFEE_BEANS -> {
                addResource(command.toInt())
                print("Write how many disposable cups of coffee do you want to add: ")
                state = State.FILL_CUPS
            }
            state == State.FILL_CUPS -> {
                addResource(command.toInt())
                println()
                displayInitialMessage()
                state = State.MAIN_MENU
            }
            state == State.BUY && command == "back" -> {
                println()
                displayInitialMessage()
                state = State.MAIN_MENU
            }
            state == State.BUY && command == "1" -> {
                buy(CoffeeType.ESPRESSO)
                println()
                displayInitialMessage()
                state = State.MAIN_MENU
            }
            state == State.BUY && command == "2" -> {
                buy(CoffeeType.LATTE)
                println()
                displayInitialMessage()
                state = State.MAIN_MENU
            }
            state == State.BUY && command == "3" -> {
                buy(CoffeeType.CAPPUCCINO)
                println()
                displayInitialMessage()
                state = State.MAIN_MENU
            }
        }
    }

    private fun setResources(_waterCount: Int, _milkCount: Int, _beansCount: Int, _cash: Int, _disposableCups: Int) {
        waterCount = _waterCount
        milkCount = _milkCount
        beansCount = _beansCount
        cash = _cash
        disposableCups = _disposableCups
    }

    private fun addResource(resourceCount: Int) {
        when (state) {
            State.FILL_WATER -> waterCount += resourceCount
            State.FILL_MILK -> milkCount += resourceCount
            State.FILL_COFFEE_BEANS -> beansCount += resourceCount
            State.FILL_CUPS -> disposableCups += resourceCount
        }
    }

    private fun addResources(_waterCount: Int, _milkCount: Int, _beansCount: Int, _disposableCups: Int) {
        waterCount += _waterCount
        milkCount += _milkCount
        beansCount += _beansCount
        disposableCups += _disposableCups
    }

    private fun calcResourcesNeededForCups(countOfCups: Int, coffeeType: CoffeeType) {
        println("For $countOfCups of coffee you will need:")
        println("${countOfCups * coffeeType.waterPerCup} ml of water")
        println("${countOfCups * coffeeType.milkPerCup} ml of milk")
        println("${countOfCups * coffeeType.beansPerCup} g of coffee beans")
    }

    private fun printState() {
        println("The coffee machine has:")
        println("$waterCount of water")
        println("$milkCount of milk")
        println("$beansCount of coffee beans")
        println("$disposableCups of disposable cups")
        println("\$$cash of money")
    }

    private fun checkIfAvailableResourcesForNumberOfCups(numberOfCups: Int, coffeeType: CoffeeType) {
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

    private fun getMaximumNumberOfCups(coffeeType: CoffeeType): Int {
        val waterPortions = if (coffeeType.waterPerCup == 0) Int.MAX_VALUE else waterCount / coffeeType.waterPerCup
        val milkPortions = if (coffeeType.milkPerCup == 0) Int.MAX_VALUE else milkCount / coffeeType.milkPerCup
        val beansPortions = if (coffeeType.beansPerCup == 0) Int.MAX_VALUE else beansCount / coffeeType.beansPerCup

        return listOf(waterPortions, milkPortions, beansPortions, disposableCups).minOrNull() ?: 0
    }

    private fun buy(coffeeType: CoffeeType) {
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

    private fun getCash(): Int {
        val result = cash
        cash = 0
        return result
    }
}
