package cat.xampi.kata.adventure

import cat.xampi.kata.adventure._

case class Treasure(value: Int)
case class Coin(value: Int)
case class User(name: String)

trait AdventureGame {
  def play(treasure: Treasure, user: User): Treasure
}

class AdventureGameImpl(coinCollector: CoinCollector, treasureBuyer: TreasureBuyer) extends AdventureGame {    
  def play(treasure: Treasure, user: User) = {
    val coinsCollected = coinCollector.collect(user)
    treasureBuyer.buy(treasure, coinsCollected)
  } 
}



