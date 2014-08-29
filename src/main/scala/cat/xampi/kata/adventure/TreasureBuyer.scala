package cat.xampi.kata.adventure

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class InsufficientFundsException extends Exception

trait TreasureBuyer { 
  def buy(treasure: Treasure, coins: List[Coin]): Treasure
  def buyAsFuture(treasure: Treasure, coins: List[Coin]): Future[Treasure]
}
class TreasureBuyerImpl extends TreasureBuyer {   
  def buy(treasure: Treasure, coins: List[Coin]) = {
      val totalAmount =  coins.foldLeft(0)((x,y) => x + y.value)
      if(totalAmount >= treasure.value) treasure else throw new InsufficientFundsException
  }
  
  def buyAsFuture(treasure: Treasure, coins: List[Coin]) = Future { buy(treasure, coins) }  
}