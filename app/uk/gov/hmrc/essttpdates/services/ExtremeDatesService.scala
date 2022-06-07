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

package uk.gov.hmrc.essttpdates.services

import com.google.inject.{Inject, Singleton}
import essttp.rootmodel.dates.{InitialPayment, InitialPaymentDate}
import essttp.rootmodel.dates.extremedates.{EarliestPlanStartDate, ExtremeDatesRequest, ExtremeDatesResponse, LatestPlanStartDate}
import uk.gov.hmrc.essttpdates.services

import scala.concurrent.Future

@Singleton
class ExtremeDatesService @Inject() (datesService: DatesService) {
  def calculateExtremeDates(extremeDatesRequest: ExtremeDatesRequest): Future[Either[services.DatesService.Error, ExtremeDatesResponse]] = {
    val initialPaymentDate: Option[InitialPaymentDate] =
      datesService.initialPaymentDate(
        initialPayment    = extremeDatesRequest.initialPayment,
        numberOfDaysToAdd = datesService.`10.Days`
      )
    val earliestPlanStartDate: EarliestPlanStartDate = extremeDatesRequest.initialPayment match {
      case InitialPayment(true)  => EarliestPlanStartDate(datesService.todayPlusDays(datesService.`30.Days`))
      case InitialPayment(false) => EarliestPlanStartDate(datesService.todayPlusDays(datesService.`10.Days`))
    }
    val latestPlanStartDate: LatestPlanStartDate = extremeDatesRequest.initialPayment match {
      case InitialPayment(true)  => LatestPlanStartDate(datesService.todayPlusDays(datesService.`60.Days`))
      case InitialPayment(false) => LatestPlanStartDate(datesService.todayPlusDays(datesService.`40.Days`))
    }

    Future.successful(Right(
      ExtremeDatesResponse(
        initialPaymentDate    = initialPaymentDate,
        earliestPlanStartDate = earliestPlanStartDate,
        latestPlanStartDate   = latestPlanStartDate
      )
    ))
  }
}
