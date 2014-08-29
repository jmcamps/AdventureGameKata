package cat.xampi.kata.adventure

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

// add JUnit runner annotation to allow Infinitest work with Eclipse
@RunWith(classOf[JUnitRunner])
class AdventureGameSpec extends FlatSpec with Matchers {

  val treasure = Treasure(5)
  val user = User("xampi")
  
  "play" should "get the Treasure" in { 
    // Arrange
    val game = new AdventureGameImpl(new CoinCollectorImpl, new TreasureBuyerImpl)
    
    // Act
    val result = game.play(treasure, user)
    
    // Assert
    result should be (treasure)
  }

  
}