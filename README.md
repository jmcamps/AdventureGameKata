Adventure Game Kata
===================

Sample code kata in Scala inpired in examples from Principles of Reactive Programming course week 3 by Erik Meijer.

We try to implement a 'Buy treasure' game only to work and experiment with Futures, Promises, async/await, some dependency injection techniques and testing futures with ScalaTest.

# AdventureGame
The super simple game consist in one method *play* who takes the treasure to buy and the user and tries to buy the treasure in two steps:
collectCoins and buyTreasure. These two steps are implemented with two services 

# collectCoins
This service should take the user who is playing the game and return a list of coins. The list of coins is built simply by counting the number of repetitions of each character in the user name.
If username is empty should throw InvalidUserException.

# buyTreasure
This service should take one list of coins and one treasure and buy it only if the sum of coin values is gretaher or equal than the treasure value. 
If the treasure value is lower then the sum of coins should throw InsufficientFundsException.  

