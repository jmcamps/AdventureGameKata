package cat.xampi.kata.adventure

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Try
import scala.util.Success

class InsufficientFundsException extends Exception

trait TreasureBuyer { 
  def buy(treasure: Treasure, coins: List[Coin]): Treasure
  def buyAsTry(treasure: Treasure, coins: List[Coin]): Try[Treasure]
  def buyAsFuture(treasure: Treasure, coins: List[Coin]): Future[Treasure]
}
class TreasureBuyerImpl extends TreasureBuyer {   
  def buy(treasure: Treasure, coins: List[Coin]) = {     
    if(totalAmount(coins) >= treasure.value) treasure else throw new InsufficientFundsException
  }
  
  def buyAsTry(treasure: Treasure, coins: List[Coin]) = try {
    Success(buy(treasure, coins))
  } catch {
    case e: Exception => Failure(e)
  }
  
  def buyAsFuture(treasure: Treasure, coins: List[Coin]) = Future { buy(treasure, coins) }
  
  private def totalAmount(coins: List[Coin]): Int = coins.foldLeft(0)((x,y) => x + y.value)
}