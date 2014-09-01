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

// add JUnit runner annotation to allow Infinitest work with Eclipse
@RunWith(classOf[JUnitRunner])
class TreasureBuyerSpec extends FlatSpec with Matchers with ScalaFutures{  
  
  val listOfInsuficientCoins = List(Coin(1))
  val listOfCoins = List(Coin(1), Coin(5), Coin(7))
  val treasure = Treasure(13)
      
  "buy" should "return the Treasure" in {
    // Arrange
    val buyer: TreasureBuyer = new TreasureBuyerImpl

    // Act 
    val result = buyer.buy(treasure, listOfCoins)

    // Assert
    result should be (treasure)
  }
  
  it should "throw InsufficientFundsException if called with insufficient funds" in {
    // Arrange
    val buyer: TreasureBuyer = new TreasureBuyerImpl

    // Act & Assert
    intercept[InsufficientFundsException] {
      buyer.buy(treasure, listOfInsuficientCoins)
    }      
  }
  
  "buyAsTry" should "return the Treasure" in {
    // Arrange
    val buyer: TreasureBuyer = new TreasureBuyerImpl
    import org.scalatest.TryValues.convertTryToSuccessOrFailure

    // Act 
    val result = buyer.buyAsTry(treasure, listOfCoins)

    // Assert
    result.success.value should be (treasure)
  }
  
  it should "return Failure[InsufficientFundsException] if called with insufficient funds" in {
    // Arrange
    val buyer: TreasureBuyer = new TreasureBuyerImpl
    import org.scalatest.TryValues.convertTryToSuccessOrFailure

    // Act 
    val result = buyer.buyAsTry(treasure, listOfInsuficientCoins)

    // Assert
    result.failure.exception.getMessage() should be (null)   
  }
  
  "buyAsFuture" should "success the Future and return the Treasure" in {
    // Arrange
    val buyer: TreasureBuyer = new TreasureBuyerImpl
    
    // Act
    val result = buyer.buyAsFuture(treasure, listOfCoins)
    
    // Assert
    whenReady(result, timeout(Span(6, Seconds))) { res =>
      res should be (treasure)
    }
  }    
  
  it should "fail the Future with InsufficientFundsException if called with insufficient funds" in {
    // Arrange
    val buyer: TreasureBuyer = new TreasureBuyerImpl
    
    // Act
    val result = buyer.buyAsFuture(treasure, listOfInsuficientCoins)
    
    // Assert
    whenReady(result.failed, timeout(Span(6, Seconds))) { ex =>
      ex shouldBe an [InsufficientFundsException]
    }
  }
}