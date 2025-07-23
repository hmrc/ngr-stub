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
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.{defaultAwaitTimeout, status}
import play.mvc.Http.Status.OK


class CallbackControllerSpec extends TestSupport {

  private val callbackController = inject[CallbackController]

  "CallbackController" should {
    "log received callback with JSON body and return OK" in {
      val result = callbackController.receivedCallback(
        FakeRequest("POST", "/").withJsonBody(Json.obj("param" -> "value"))
      )

      status(result) shouldBe OK
    }

    "log received callback with any body and return OK" in {
      val result = callbackController.receivedCallback(FakeRequest())

      status(result) shouldBe OK
    }
  }

}
