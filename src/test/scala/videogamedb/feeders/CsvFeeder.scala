package videogamedb.feeders
import io.gatling.core.Predef._
import io.gatling.http.Predef._
class CsvFeeder extends Simulation{


val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
  .acceptHeader("application/json")

val csvFeeder = csv("testdata/gameCsvFile.csv").circular

def getSpecificVideoGame () ={

  repeat(10){
    feed(csvFeeder)
      .exec(http("Retrieving VideoGame with name - #{gameName}")
        .get("/videogame/#{gameId}")
        .check(jsonPath("$.name").is("#{gameName}"))
        .check(status.is(200)))
      .pause(2)
  }

}



val scn_feeder = scenario("Parameterization using CSV Feeder")
  .exec(getSpecificVideoGame())


setUp(scn_feeder.inject(atOnceUsers(1))).protocols(httpProtocol)








}
