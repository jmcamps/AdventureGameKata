package cat.xampi.kata.adventure

import cat.xampi.kata.adventure._
import scala.util.{ Failure, Success, Try }
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.async.Async._

case class Treasure(value: Int)
case class Coin(value: Int)
case class User(name: String)

trait AdventureGame {
  def play(treasure: Treasure, user: User): Treasure
  def playWithTry(treasure: Treasure, user: User): Try[Treasure]
  def playWithFutures(treasure: Treasure, user: User): Future[Treasure]
}

class AdventureGameImpl(coinCollector: CoinCollector, treasureBuyer: TreasureBuyer) extends AdventureGame {
  def play(treasure: Treasure, user: User) = {
    val coinsCollected = coinCollector.collect(user)
    treasureBuyer.buy(treasure, coinsCollected)
  }

  def playWithTry(treasure: Treasure, user: User) = {
    val coinsCollected = coinCollector.collectAsTry(user)
    // Implementation with pattern matching
    // coinsCollected match {
    //   case Success(coins) => treasureBuyer.buyAsTry(treasure, coins) 
    //   case Failure(e) => Failure(e)
    // }

    // Implementation with flatMap
    //coinsCollected flatMap( coins => treasureBuyer.buyAsTry(treasure, coins))

    // Implementation with for comprehensions (just sintactic shugar)
    for {
      coinsCollected <- coinCollector.collectAsTry(user)
      treasure <- treasureBuyer.buyAsTry(treasure, coinsCollected)
    } yield treasure
  }

  def playWithFutures(treasure: Treasure, user: User) = { 
    
    
    // Implementation with pattern matching
    
    
    // Implementation with flatMap
    // val coinsCollected = coinCollector.collectAsFuture(user)
    //coinsCollected flatMap(coins => { treasureBuyer.buyAsFuture(treasure, coins) })
    
    // Implementation with for-comprehensions
    // for {
    //   coinsCollected <- coinCollector.collectAsFuture(user)
    //   treasure <- treasureBuyer.buyAsFuture(treasure, coinsCollected)
    // } yield treasure
    
    // Implementation with async/await
    async {
      val coinsCollected =  await {coinCollector.collectAsFuture(user) }
      await { treasureBuyer.buyAsFuture(treasure, coinsCollected)}      
    }
  }
}



