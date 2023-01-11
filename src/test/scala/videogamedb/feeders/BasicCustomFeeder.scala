package videogamedb.feeders
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicCustomFeeder extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

  var idNumbers = (1 to 10).iterator

  val customFeeder = Iterator.continually(Map("gameId" -> idNumbers.next()))

  def getSpecificVideoGame () ={

    repeat(10){
      feed(customFeeder)
        .exec(http("Retrieving VideoGame with ID - #{gameId}")
          .get("/videogame/#{gameId}")
          .check(status.is(200)))
          .pause(2)
    }

  }
  val scenario_customfeeder = scenario("Parameterization using Custom Feeder")
    .exec(getSpecificVideoGame())


  setUp(scenario_customfeeder.inject(atOnceUsers(1))).protocols(httpProtocol)


}
