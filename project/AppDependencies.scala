import sbt.*

object AppDependencies {

  val bootstrapVersion = "10.1.0"
  val hmrcMongoVersion = "2.7.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc.mongo"  %% "hmrc-mongo-play-30"        % hmrcMongoVersion,
    "uk.gov.hmrc"        %% "bootstrap-backend-play-30" % bootstrapVersion
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-30"  % bootstrapVersion  % Test,
    "org.scalamock"          %% "scalamock"                % "7.4.2"           % Test,
    "uk.gov.hmrc.mongo"      %% "hmrc-mongo-test-play-30"  % hmrcMongoVersion  % Test
    
  )

  val it: Seq[ModuleID] = Seq.empty

}
