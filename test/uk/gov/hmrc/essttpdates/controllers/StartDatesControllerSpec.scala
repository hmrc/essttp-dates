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

package uk.gov.hmrc.essttpdates.controllers

import uk.gov.hmrc.essttpdates.models._
import uk.gov.hmrc.essttpdates.testsupport.testdata.TdAll
import uk.gov.hmrc.essttpdates.testsupport.testdata.TdAll.TdDates
import uk.gov.hmrc.essttpdates.testsupport.{FrozenTime, ItSpec, TestDatesConnector}
import uk.gov.hmrc.http.HttpResponse

import java.time.LocalDate

class StartDatesControllerSpec extends ItSpec {

  def connector: TestDatesConnector = app.injector.instanceOf[TestDatesConnector]

  "POST /start-dates" - {

    val testDataTable = Table(
      ("Current date", "Preferred day of month", "Initial payment", "Earliest InitialPaymentDate", "Earliest InstalmentStartDate"),
      (TdDates.`1stJan2022`, 1, false, None, TdDates.`1stFeb2022`),
      (TdDates.`1stJan2022`, 2, false, None, TdDates.`2ndFeb2022`),
      (TdDates.`1stJan2022`, 10, false, None, TdDates.`10thFeb2022`),
      (TdDates.`1stJan2022`, 11, false, None, TdDates.`11thJan2022`),
      (TdDates.`1stJan2022`, 15, false, None, TdDates.`15thJan2022`),
      (TdDates.`1stJan2022`, 28, false, None, TdDates.`28thJan2022`),

      (TdDates.`15thJan2022`, 1, false, None, TdDates.`1stFeb2022`),
      (TdDates.`15thJan2022`, 2, false, None, TdDates.`2ndFeb2022`),
      (TdDates.`15thJan2022`, 10, false, None, TdDates.`10thFeb2022`),
      (TdDates.`15thJan2022`, 15, false, None, TdDates.`15thFeb2022`),
      (TdDates.`15thJan2022`, 25, false, None, TdDates.`25thJan2022`),
      (TdDates.`15thJan2022`, 28, false, None, TdDates.`28thJan2022`),

      (TdDates.`28thJan2022`, 1, false, None, TdDates.`1stMar2022`),
      (TdDates.`28thJan2022`, 2, false, None, TdDates.`2ndMar2022`),
      (TdDates.`28thJan2022`, 15, false, None, TdDates.`15thFeb2022`),
      (TdDates.`28thJan2022`, 28, false, None, TdDates.`28thFeb2022`),

      (TdDates.`1stFeb2022`, 1, false, None, TdDates.`1stMar2022`),
      (TdDates.`1stFeb2022`, 2, false, None, TdDates.`2ndMar2022`),
      (TdDates.`1stFeb2022`, 15, false, None, TdDates.`15thFeb2022`),
      (TdDates.`1stFeb2022`, 28, false, None, TdDates.`28thFeb2022`),
      (TdDates.`15thFeb2022`, 1, false, None, TdDates.`1stMar2022`),
      (TdDates.`15thFeb2022`, 2, false, None, TdDates.`2ndMar2022`),
      (TdDates.`15thFeb2022`, 15, false, None, TdDates.`15thMar2022`),
      (TdDates.`15thFeb2022`, 25, false, None, TdDates.`25thFeb2022`),

      (TdDates.`1stJan2022`, 1, true, Some(TdDates.`11thJan2022`), TdDates.`1stFeb2022`),
      (TdDates.`1stJan2022`, 2, true, Some(TdDates.`11thJan2022`), TdDates.`2ndFeb2022`),
      (TdDates.`1stJan2022`, 15, true, Some(TdDates.`11thJan2022`), TdDates.`15thFeb2022`),
      (TdDates.`1stJan2022`, 28, true, Some(TdDates.`11thJan2022`), TdDates.`28thFeb2022`),

      (TdDates.`15thJan2022`, 1, true, Some(TdDates.`25thJan2022`), TdDates.`1stMar2022`),
      (TdDates.`15thJan2022`, 2, true, Some(TdDates.`25thJan2022`), TdDates.`2ndMar2022`),
      (TdDates.`15thJan2022`, 15, true, Some(TdDates.`25thJan2022`), TdDates.`15thFeb2022`),
      (TdDates.`15thJan2022`, 28, true, Some(TdDates.`25thJan2022`), TdDates.`28thFeb2022`),

      (TdDates.`28thJan2022`, 1, true, Some(TdDates.`7thFeb2022`), TdDates.`1stMar2022`),
      (TdDates.`28thJan2022`, 2, true, Some(TdDates.`7thFeb2022`), TdDates.`2ndMar2022`),
      (TdDates.`28thJan2022`, 15, true, Some(TdDates.`7thFeb2022`), TdDates.`15thMar2022`),

      (TdDates.`25thDec2022`, 1, false, None, TdDates.`1stFeb2023`),
      (TdDates.`25thDec2022`, 1, true, Some(TdDates.`4thJan2023`), TdDates.`1stFeb2023`),
      (TdDates.`25thDec2022`, 15, false, None, TdDates.`15thJan2023`),
      (TdDates.`25thDec2022`, 15, true, Some(TdDates.`4thJan2023`), TdDates.`15thFeb2023`),
      (TdDates.`25thDec2022`, 28, false, None, TdDates.`28thJan2023`),
      (TdDates.`25thDec2022`, 28, true, Some(TdDates.`4thJan2023`), TdDates.`28thJan2023`)
    )
    forAll(testDataTable) { (
      currentDate: String,
      preferredDayOfMonth: Int,
      initialPayment: Boolean,
      earliestInitialPaymentDate: Option[String],
      earliestInstalmentStartDate: String
    ) =>
      s"[CurrentDay: $currentDate][PreferredDayOfMonth: ${preferredDayOfMonth.toString}][InitialPayment:${initialPayment.toString}][ExpectedStartDate: $earliestInstalmentStartDate]" in {
        FrozenTime.setTime(currentDate)
        val initialPaymentDate: Option[InitialPaymentDate] = earliestInitialPaymentDate.map(someDate => InitialPaymentDate(LocalDate.parse(someDate)))
        val request: StartDatesRequest = TdAll.startDatesRequest(InitialPayment(initialPayment), PreferredDayOfMonth(preferredDayOfMonth))
        val response: HttpResponse = connector.startDates(request).futureValue
        response.json.as[StartDatesResponse] shouldBe TdAll.startDatesResponse(initialPaymentDate, earliestInstalmentStartDate)
      }
    }
  }
}
