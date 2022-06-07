import sbt._

object AppDependencies {

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-28"  % "5.24.0",
    "org.typelevel"           %% "cats-core"                  % "2.7.0"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"     % "5.24.0"  % "test, it",
    "com.vladsch.flexmark"    %  "flexmark-all"               % "0.36.8"  % "test, it"
  )
}
