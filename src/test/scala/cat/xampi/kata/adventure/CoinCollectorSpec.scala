package cat.xampi.kata.adventure

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.util.Failure
import scala.util.Success
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.concurrent._
import org.scalatest.time.Span
import org.scalatest.time.Seconds
import org.scalatest.TryValues
import org.scalatest.TryValues.SuccessOrFailure
import scala.util.Try
import cat.xampi.kata.adventure.InvalidUserException

// add JUnit runner annotation to allow Infinitest work with Eclipse
@RunWith(classOf[JUnitRunner])
class CoinCollectorSpec extends FlatSpec with Matchers with ScalaFutures{
  
  import scala.language.implicitConversions

  val user = User("xamar")
  val emptyUser = User("")
  val listOfCoins = List(Coin(1), Coin(2), Coin(1), Coin(1))
      
  "collect" should "return a list with correct coins" in {
    // Arrange
    val collector: CoinCollector = new CoinCollectorImpl

    // Act
    val result = collector collect user

    // Assert
    result should be (listOfCoins)
  }
  
  it should "throw InvalidUserException if called with empty user.name" in {
    // Arrange
    val collector: CoinCollector = new CoinCollectorImpl

    // Act & Assert
    intercept[InvalidUserException] {
      collector collect emptyUser
    }      
  }
  
  "collectAsTry" should "return a list with correct coins" in {
    // Arrange
    val collector: CoinCollector = new CoinCollectorImpl    
    import org.scalatest.TryValues.convertTryToSuccessOrFailure
    
    // Act
    val result = collector.collectAsTry(user)

    // Assert
    result.success.value should be (listOfCoins) 
  }
  
  it should "return Failure(InvalidUserException) if called with empty user.name" in {
    // Arrange
    val collector: CoinCollector = new CoinCollectorImpl
    import org.scalatest.TryValues.convertTryToSuccessOrFailure
    
    // Act
    val result = collector.collectAsTry(emptyUser)

    // Assert
    result.failure.exception.getMessage() should be (null) 
  }

  "collectAsFuture" should "return a future with a correct coins list" in {
    // Arrange
    val collector: CoinCollector = new CoinCollectorImpl
    
    // Act
    val result = collector.collectAsFuture(user)

    // Assert
    // This won't work in a test as the test will finish before the future completes and the assertions are on a different thread.
    /*result onComplete {
      case Success(value) => value should be(List(Coin(2), Coin(52), Coin(7)))
      case Failure(exp) => fail(exp)
    }*/

    // Assert
    // ScalaFutures to the rescue! Upgrade to ScalaTest 2.0+ and mix in the ScalaFutures trait. Now you can test using whenReady:
    whenReady(result, timeout(Span(6, Seconds))) { res =>
      res should be (listOfCoins)
    }
  }
  
  it should "fail the Future with InvalidUserException if called with empty user.name" in {
    // Arrange
    val collector: CoinCollector = new CoinCollectorImpl
    
    // Act
    val result = collector.collectAsFuture(emptyUser)
    
    // Assert
    whenReady(result.failed, timeout(Span(6, Seconds))) { ex =>
      ex shouldBe an [InvalidUserException] 
    }
  }

}