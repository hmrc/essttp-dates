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

import essttp.rootmodel.dates.extremedates.ExtremeDatesRequest
import essttp.rootmodel.dates.startdates.StartDatesRequest
import play.api.libs.json.Json
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.essttpdates.services.{ExtremeDatesService, StartDatesService}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton()
class DatesController @Inject() (
    startDatesService:   StartDatesService,
    extremeDatesService: ExtremeDatesService,
    cc:                  ControllerComponents
)(implicit executionContext: ExecutionContext)
  extends BackendController(cc) {

  def startDates(): Action[StartDatesRequest] = Action.async(parse.json[StartDatesRequest]) { implicit request =>
    startDatesService.calculateStartDates(request.body).map {
      case Left(_)      => BadRequest
      case Right(value) => Ok(Json.toJson(value))
    }
  }

  def extremeDates(): Action[ExtremeDatesRequest] = Action.async(parse.json[ExtremeDatesRequest]) { implicit request =>
    extremeDatesService.calculateExtremeDates(request.body).map {
      case Left(_)      => BadRequest
      case Right(value) => Ok(Json.toJson(value))
    }
  }

}
