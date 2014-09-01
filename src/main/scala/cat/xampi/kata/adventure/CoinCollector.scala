package cat.xampi.kata.adventure

import cat.xampi.kata.adventure._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import scala.util.Try

class InvalidUserException extends Exception

trait CoinCollector { 
  def collect(user: User): List[Coin]
  def collectAsTry(user: User): Try[List[Coin]]
  def collectAsFuture(user: User): Future[List[Coin]]
}
class CoinCollectorImpl extends CoinCollector { 
  // Coins collected are the repetitions of letters in user name
  def collect(user: User) = {
    if(user.name.isEmpty()) throw new InvalidUserException    
    getCoinsForeachOccurrence { countLetterOcurrences { user.name } }
  }
  
  def collectAsTry(user: User) = {
    try {
      Success(collect(user))
    } catch {
      case e: Exception => Failure(e) 
    }
  }
  
  
  def collectAsFuture(user: User) = Future {
    Thread.sleep(1000)
    collect(user)
  }
  
  private def countLetterOcurrences(input: String): Map[Char, Int] = {
    input.foldLeft[Map[Char, Int]](Map.empty)((m, c) => m + (c -> (m.getOrElse(c, 0) + 1)))
  }
  
  private def getCoinsForeachOccurrence(occurrences: Map[Char, Int]): List[Coin] = { occurrences.values.map { Coin(_) } } toList
}