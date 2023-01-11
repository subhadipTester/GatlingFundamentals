package videogamedb.scriptfundamentals

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class CodeReuse extends Simulation{


  // 1.Http Configuration
  val httpProtocol = http.baseUrl("https://www.videogamedb.uk:443/api/v2/")
    .acceptHeader("application/json");

  // Methods created for code reuse

  def getAllVideoGames() ={
   repeat(3)
    {
      exec(http("Get All Video Games from Endpoint")
        .get("videogame")
        .check(status.is(200)))
    }

  }

  def getSpecificVideoGame() ={
    repeat(5, counterName = "counter"){
    exec(http("Get Specific Video Game with id: #{counter}")
      .get("videogame/#{counter}")
      .check(status in (200 to 210)))
  }
  }
  // Scenario Definition
  val scenario_repeat = scenario("Code Reuse")
    .exec(getAllVideoGames())
    .pause(5)
    .exec(getSpecificVideoGame())
    .pause(5000.milliseconds)
    .repeat(1){
      getAllVideoGames()
    }

  //3. Load Scenario Design
  setUp(scenario_repeat.inject(atOnceUsers(1))).protocols(httpProtocol)









}
