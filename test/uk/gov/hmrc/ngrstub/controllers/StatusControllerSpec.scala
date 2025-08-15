/*
 * Copyright 2025 HM Revenue & Customs
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

package uk.gov.hmrc.ngrstub.controllers

import helpers.TestSupport
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsJson, defaultAwaitTimeout, status}
import play.mvc.Http.Status.OK

class StatusControllerSpec extends TestSupport {
  private val statusController = inject[StatusController]

  "StatusController" should {
    "return UNKNOWN status for TEST_UNKNOWN" in {
      val result = statusController.ratepayerStatus("TEST_UNKNOWN")(FakeRequest())
      status(result) shouldBe OK

      val json = contentAsJson(result)
      (json \ "ratepayerStatus").as[String] shouldBe "UNKNOWN"
      (json \ "error").asOpt[String] shouldBe Some("Unknown. The bridge has no details of this ratepayer. Possibly a signal that something has gone wrong if the Ratepayer has registered via a frontend service.")
    }

    "return INPROGRESS status for TEST_INPROGRESS" in {
      val result = statusController.ratepayerStatus("TEST_INPROGRESS")(FakeRequest())
      status(result) shouldBe OK

      val json = contentAsJson(result)
      (json \ "ratepayerStatus").as[String] shouldBe "INPROGRESS"
      (json \ "error").asOpt[String] shouldBe Some("In progress. Case officers are examining the ratepayer application but have not yet decided.")
    }

    "return ACCEPTED status for TEST_ACCEPTED" in {
      val result = statusController.ratepayerStatus("TEST_ACCEPTED")(FakeRequest())
      status(result) shouldBe OK

      val json = contentAsJson(result)
      (json \ "ratepayerStatus").as[String] shouldBe "ACCEPTED"
      (json \ "error").asOpt[String] shouldBe Some("Registered. The ratepayer details have been accepted by the VOA.")
    }

    "return REJECTED status for TEST_REJECTED" in {
      val result = statusController.ratepayerStatus("TEST_REJECTED")(FakeRequest())
      status(result) shouldBe OK

      val json = contentAsJson(result)
      (json \ "ratepayerStatus").as[String] shouldBe "REJECTED"
      (json \ "error").asOpt[String] shouldBe Some("Rejected. The ratepayer details have been rejected by the VOA.")
    }

    "return UNKNOWN status for any other id" in {
      val result = statusController.ratepayerStatus("SOME_OTHER_ID")(FakeRequest())
      status(result) shouldBe OK

      val json = contentAsJson(result)
      (json \ "ratepayerStatus").as[String] shouldBe "UNKNOWN"
      (json \ "error").asOpt[String] shouldBe Some("Unknown. The bridge has no details of this ratepayer. Possibly a signal that something has gone wrong if the Ratepayer has registered via a frontend service.")
    }
  }
}
