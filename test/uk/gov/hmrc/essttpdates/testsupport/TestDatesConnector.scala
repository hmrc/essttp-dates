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

import essttp.rootmodel.dates.extremedates.{ExtremeDatesRequest, ExtremeDatesResponse}
import essttp.rootmodel.dates.startdates.{StartDatesRequest, StartDatesResponse}

import javax.inject.{Inject, Singleton}
import uk.gov.hmrc.http.{HeaderCarrier, HttpReads, HttpReadsInstances, HttpResponse}
import uk.gov.hmrc.http.HttpClient
import uk.gov.hmrc.http.HttpReads.Implicits._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TestDatesConnector @Inject() (httpClient: HttpClient)(implicit executionContext: ExecutionContext) {
  private val port = 19001
  private val essttpDatesBaseUrl = s"http://localhost:$port/essttp-dates"
  implicit val readResponse: HttpReads[HttpResponse] = HttpReadsInstances.throwOnFailure(HttpReadsInstances.readEitherOf(HttpReadsInstances.readRaw))

  def startDates(startDatesRequest: StartDatesRequest)(implicit hc: HeaderCarrier): Future[StartDatesResponse] =
    httpClient.POST[StartDatesRequest, StartDatesResponse](s"$essttpDatesBaseUrl/start-dates", startDatesRequest)

  def extremeDates(extremeDatesRequest: ExtremeDatesRequest)(implicit hc: HeaderCarrier): Future[ExtremeDatesResponse] =
    httpClient.POST[ExtremeDatesRequest, ExtremeDatesResponse](s"$essttpDatesBaseUrl/extreme-dates", extremeDatesRequest)

}
