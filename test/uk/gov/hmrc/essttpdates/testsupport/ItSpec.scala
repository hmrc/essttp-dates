/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.essttpdates.testsupport

import com.github.ghik.silencer.silent
import com.google.inject.{AbstractModule, Provides, Singleton}
import org.scalatest.TestData
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import play.api.inject.guice.{GuiceApplicationBuilder, GuiceableModule}
import play.api.test.{DefaultTestServerFactory, RunningServer}
import play.api.{Application, Mode}
import play.core.server.ServerConfig
import uk.gov.hmrc.http.{HeaderCarrier, HttpReads, HttpReadsInstances, HttpResponse}

import java.time.Clock
import scala.concurrent.ExecutionContext

trait ItSpec
  extends AnyFreeSpec
  with RichMatchers
  with GuiceOneServerPerTest
  with Matchers
  with TableDrivenPropertyChecks {
  self =>

  val testServerPort = 19001
  val baseUrl: String = s"http://localhost:$testServerPort"

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
  implicit val emptyHeaderCarrier: HeaderCarrier = HeaderCarrier()
  implicit val readResponse: HttpReads[HttpResponse] = HttpReadsInstances.throwOnFailure(HttpReadsInstances.readEitherOf(HttpReadsInstances.readRaw))

  override implicit val patienceConfig: PatienceConfig = PatienceConfig(
    timeout  = scaled(Span(20, Seconds)),
    interval = scaled(Span(300, Millis))
  )

  def config: Map[String, Any] = Map(
    "auditing.enabled" -> false,
    "microservice.services.essttp-dates.protocol" -> "http",
    "microservice.services.essttp-dates.host" -> "localhost",
    "microservice.services.essttp-dates.port" -> testServerPort
  )

  lazy val overridingsModule: AbstractModule = new AbstractModule {
    override def configure(): Unit = ()

    @Provides
    @Singleton
    @silent // silence "method never used" warning
    def clock: Clock = {
      FrozenTime.reset()
      FrozenTime.getClock
    }
  }

  override def newAppForTest(testData: TestData): Application = new GuiceApplicationBuilder()
    .configure(config)
    .overrides(GuiceableModule.fromGuiceModules(Seq(overridingsModule)))
    .build()

  override protected def newServerForTest(app: Application, testData: TestData): RunningServer =
    TestServerFactory.start(app)

  object TestServerFactory extends DefaultTestServerFactory {
    override protected def serverConfig(app: Application): ServerConfig = {
      val sc = ServerConfig(port    = Some(testServerPort), sslPort = Some(0), mode = Mode.Test, rootDir = app.path)
      sc.copy(configuration = sc.configuration.withFallback(overrideServerConfiguration(app)))
    }
  }
}
