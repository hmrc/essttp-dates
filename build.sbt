import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import play.sbt.PlayImport.PlayKeys
import scalariform.formatter.preferences.{AlignArguments, AlignParameters, AlignSingleLineCaseStatements, AllowParamGroupsOnNewlines, CompactControlReadability, CompactStringConcatenation, DanglingCloseParenthesis, DoubleIndentConstructorArguments, DoubleIndentMethodDeclaration, FirstArgumentOnNewline, FirstParameterOnNewline, Force, FormatXml, IndentLocalDefs, IndentPackageBlocks, IndentSpaces, IndentWithTabs, MultilineScaladocCommentsStartOnFirstLine, NewlineAtEndOfFile, PlaceScaladocAsterisksBeneathSecondAsterisk, PreserveSpaceBeforeArguments, RewriteArrowSymbols, SpaceBeforeColon, SpaceBeforeContextColon, SpaceInsideBrackets, SpaceInsideParentheses, SpacesAroundMultiImports, SpacesWithinPatternBinders}
import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings
import wartremover.Wart
import wartremover.WartRemover.autoImport.{wartremoverErrors, wartremoverExcluded}

val appName = "essttp-dates"

val silencerVersion = "1.7.7"

lazy val scalaCompilerOptions: Seq[String] = Seq(
  "-Xfatal-warnings",
  "-Xlint:-missing-interpolator,_",
  "-Yno-adapted-args",
  "-Ywarn-value-discard",
  "-Ywarn-dead-code",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:implicitConversions",
  "-Ypartial-unification" //required by cats
)

lazy val scalariformSettings: Def.SettingsDefinition = {
  // description of options found here -> https://github.com/scala-ide/scalariform
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(AlignArguments, true)
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(AllowParamGroupsOnNewlines, true)
    .setPreference(CompactControlReadability, false)
    .setPreference(CompactStringConcatenation, false)
    .setPreference(DanglingCloseParenthesis, Force)
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(DoubleIndentMethodDeclaration, true)
    .setPreference(FirstArgumentOnNewline, Force)
    .setPreference(FirstParameterOnNewline, Force)
    .setPreference(FormatXml, true)
    .setPreference(IndentLocalDefs, true)
    .setPreference(IndentPackageBlocks, true)
    .setPreference(IndentSpaces, 2)
    .setPreference(IndentWithTabs, false)
    .setPreference(MultilineScaladocCommentsStartOnFirstLine, false)
    .setPreference(NewlineAtEndOfFile, true)
    .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, false)
    .setPreference(PreserveSpaceBeforeArguments, true)
    .setPreference(RewriteArrowSymbols, false)
    .setPreference(SpaceBeforeColon, false)
    .setPreference(SpaceBeforeContextColon, false)
    .setPreference(SpaceInsideBrackets, false)
    .setPreference(SpaceInsideParentheses, false)
    .setPreference(SpacesAroundMultiImports, false)
    .setPreference(SpacesWithinPatternBinders, true)
}

lazy val wartRemoverSettings =
  Seq(
    (Compile / compile / wartremoverErrors) ++= Warts.allBut(
      Wart.DefaultArguments,
      Wart.ImplicitConversion,
      Wart.ImplicitParameter,
      Wart.Nothing,
      Wart.Overloading,
      Wart.Throw,
      Wart.ToString
    ),
    Test / compile / wartremoverErrors --= Seq(
      Wart.Any,
      Wart.Equals,
      Wart.GlobalExecutionContext,
      Wart.Null,
      Wart.NonUnitStatements,
      Wart.PublicInference
    ),
    wartremoverExcluded ++= (
      (baseDirectory.value ** "*.sc").get ++
        (Compile / routes).value
      )
  )

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(
    majorVersion                     := 0,
    scalaVersion                     := "2.12.15",
    libraryDependencies              ++= AppDependencies.compile ++ AppDependencies.test,
    // ***************
    // Use the silencer plugin to suppress warnings
    scalacOptions += "-P:silencer:pathFilters=routes",
    libraryDependencies ++= Seq(
      compilerPlugin("com.github.ghik" % "silencer-plugin" % silencerVersion cross CrossVersion.full),
      "com.github.ghik" % "silencer-lib" % silencerVersion % Provided cross CrossVersion.full
    ),
    // ***************
    PlayKeys.playDefaultPort := 9219,
  )
  .settings(publishingSettings: _*)
  .configs(IntegrationTest)
  .settings(integrationTestSettings(): _*)
  .settings(resolvers += Resolver.jcenterRepo)
  .settings(CodeCoverageSettings.settings: _*)
  .settings(scalacOptions ++= scalaCompilerOptions)
  .settings(scalariformSettings: _*)
  .settings(wartRemoverSettings)
  .settings(
    wartremoverExcluded ++= (Compile / routes).value,
    Compile / doc / wartremoverErrors := Seq(),
    Compile / doc / scalacOptions := Seq() //this will allow to have warnings in `doc` task
  )
  .settings(routesImport ++= Seq())
