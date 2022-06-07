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

package uk.gov.hmrc.essttpdates.models

import play.api.libs.json.Json
import uk.gov.hmrc.essttpdates.testsupport.UnitSpec
import uk.gov.hmrc.essttpdates.testsupport.testdata.TdAll

class ModelSerialisationSpec extends UnitSpec {

  "InitialPayment should" - {
    "serialise from InitialPayment" in {
      Json.toJson[InitialPayment](InitialPayment(true)) shouldBe Json.parse("true")
      Json.toJson[InitialPayment](InitialPayment(false)) shouldBe Json.parse("false")
    }
    "de-serialise as InitialPayment" in {
      Json.parse("true").as[InitialPayment] shouldBe InitialPayment(true)
      Json.parse("false").as[InitialPayment] shouldBe InitialPayment(false)
    }
  }

  "PreferredDayOfMonth should" - {
    "serialise from PreferredDayOfMonth" in {
      Json.toJson[PreferredDayOfMonth](PreferredDayOfMonth(28)) shouldBe Json.parse("28")
    }
    "de-serialise as PreferredDayOfMonth" in {
      Json.parse("28").as[PreferredDayOfMonth] shouldBe PreferredDayOfMonth(28)
    }
  }

  "StartDatesRequest should" - {
    "serialise from StartDatesRequest" in {
      Json.toJson[StartDatesRequest](TdAll.startDatesRequestWithUpfrontPayment) shouldBe TdAll.startDatesRequestJson
    }
    "de-serialise as StartDatesRequest" in {
      TdAll.startDatesRequestJson.as[StartDatesRequest] shouldBe TdAll.startDatesRequestWithUpfrontPayment
    }
  }

  "StartDatesResponse should" - {
    "serialise from StartDatesResponse - None for initialPaymentDate" in {
      Json.toJson[StartDatesResponse](TdAll.`startDatesResponse-NoInitialPaymentDate`) shouldBe TdAll.`startDatesResponseJson-NoInitialPaymentDate`
    }
    "de-serialise as StartDatesResponse - None for initialPaymentDate" in {
      TdAll.`startDatesResponseJson-NoInitialPaymentDate`.as[StartDatesResponse] shouldBe TdAll.`startDatesResponse-NoInitialPaymentDate`
    }
    "serialise from StartDatesResponse - Some for initialPaymentDate" in {
      Json.toJson[StartDatesResponse](TdAll.`startDatesResponse-WithInitialPaymentDate`) shouldBe TdAll.`startDatesResponseJson-WithInitialPaymentDate`
    }
    "de-serialise as StartDatesResponse - Some for initialPaymentDate" in {
      TdAll.`startDatesResponseJson-WithInitialPaymentDate`.as[StartDatesResponse] shouldBe TdAll.`startDatesResponse-WithInitialPaymentDate`
    }
  }
}
