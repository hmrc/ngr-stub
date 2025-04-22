import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  private val bootstrapVersion = "9.6.0"
  val hmrcMongoVersion = "2.0.0"

  val compile = Seq(
    "uk.gov.hmrc.mongo"  %% "hmrc-mongo-play-30"        % hmrcMongoVersion,
    "uk.gov.hmrc"        %% "bootstrap-backend-play-30" % bootstrapVersion
  )

  val test = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-30"  % bootstrapVersion  % Test,
    "org.scalamock"          %% "scalamock"                % "6.0.0"           % Test,
    "uk.gov.hmrc.mongo"      %% "hmrc-mongo-test-play-30"  % hmrcMongoVersion  % Test
    
  )

  val it = Seq.empty
}
