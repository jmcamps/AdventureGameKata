package cat.xampi.kata.adventure

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.util.Failure
import org.scalatest.time.Span
import org.scalatest.time.Seconds
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.concurrent._

// add JUnit runner annotation to allow Infinitest work with Eclipse
@RunWith(classOf[JUnitRunner])
class AdventureGameSpec extends FlatSpec with Matchers with ScalaFutures {

  val treasure = Treasure(5)
  val user = User("xampi")
  val emptyUser = User("")
  
  "play" should "get the Treasure" in { 
    // Arrange
    val game = new AdventureGameImpl(new CoinCollectorImpl, new TreasureBuyerImpl)
    
    // Act
    val result = game.play(treasure, user)
    
    // Assert
    result should be (treasure)
  }
  
  it should "throw InvalidUserException if called with empty user.name" in {
    // Arrange
    val collector: CoinCollector = new CoinCollectorImpl

    // Act & Assert
    intercept[InvalidUserException] {
      collector collect emptyUser
    }      
  }
  
  "playWithTry" should "get the Treasure" in { 
    // Arrange
    val game = new AdventureGameImpl(new CoinCollectorImpl, new TreasureBuyerImpl)
    import org.scalatest.TryValues.convertTryToSuccessOrFailure
    
    // Act
    val result = game.playWithTry(treasure, user)
    
    // Assert
    result.success.value should be (treasure)
  }
  
  it should "return Failure(InvalidUserException) if called with empty user.name" in { 
    // Arrange
    val game = new AdventureGameImpl(new CoinCollectorImpl, new TreasureBuyerImpl)
    import org.scalatest.TryValues.convertTryToSuccessOrFailure
    
    // Act
    val result = game.playWithTry(treasure, emptyUser)
    
    // Assert
    assert(result.failure.exception.isInstanceOf[InvalidUserException])
  }
  
  it should "return Failure[InsufficientFundsException] if called with insufficient funds" in { 
    // Arrange
    val game = new AdventureGameImpl(new CoinCollectorImpl, new TreasureBuyerImpl)
     val treasure = Treasure(1000)
    import org.scalatest.TryValues.convertTryToSuccessOrFailure
    
    // Act
    val result = game.playWithTry(treasure, user)
    
    // Assert
    assert(result.failure.exception.isInstanceOf[InsufficientFundsException])
  }
  
  "playWithFutures" should "get the Treasure" in { 
    // Arrange
    val game: AdventureGame = new AdventureGameImpl(new CoinCollectorImpl, new TreasureBuyerImpl)   
    
    // Act
    val result = game.playWithFutures(treasure, user)
    
    // Assert    
    whenReady(result, timeout(Span(2, Seconds))) { res =>
      res should be (treasure)
    }
  }
    
  it should "return Failure(InvalidUserException) if called with empty user.name" in { 
    // Arrange
    val game = new AdventureGameImpl(new CoinCollectorImpl, new TreasureBuyerImpl)
    import org.scalatest.TryValues.convertTryToSuccessOrFailure
    
    // Act
    val result = game.playWithFutures(treasure, emptyUser)
    
    // Assert 
    whenReady(result.failed, timeout(Span(6, Seconds))) { ex =>
      ex shouldBe an [InvalidUserException] 
    }
  }
  
  it should "return Failure[InsufficientFundsException] if called with insufficient funds" in { 
    // Arrange
    val game = new AdventureGameImpl(new CoinCollectorImpl, new TreasureBuyerImpl)
     val treasure = Treasure(1000)
    import org.scalatest.TryValues.convertTryToSuccessOrFailure
    
    // Act
    val result = game.playWithFutures(treasure, user)
    
    // Assert
    whenReady(result.failed, timeout(Span(6, Seconds))) { ex =>
      ex shouldBe an [InsufficientFundsException] 
    }
  }
  
  

  
}