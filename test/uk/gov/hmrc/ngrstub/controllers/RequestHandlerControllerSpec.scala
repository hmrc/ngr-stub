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
import mocks.MockDataService
import uk.gov.hmrc.ngrstub.models.DataModel
import play.mvc.Http.Status
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, status}
import play.api.test.Helpers.defaultAwaitTimeout

class RequestHandlerControllerSpec extends TestSupport with MockDataService {

  object TestRequestHandlerController extends RequestHandlerController(mockDataService, cc)

  lazy val successModel: DataModel = DataModel(
    _id = "test",
    method = "GET",
    status = Status.OK,
    response = None
  )

  lazy val successWithBodyModel: DataModel = DataModel(
    _id = "test",
    method = "GET",
    status = Status.OK,
    response = Some(Json.parse("""{"something" : "hello"}"""))
  )


  "The getRequestHandler method" should {

    "return the status code specified in the model" in {
      lazy val result = TestRequestHandlerController.getRequestHandler("/test")(FakeRequest())

      mockFind(List(successModel))
      status(result) shouldBe Status.OK
    }

    "return the status and body" in {
      lazy val result = TestRequestHandlerController.getRequestHandler("/test")(FakeRequest())

      mockFind(List(successWithBodyModel))
      status(result) shouldBe Status.OK
      contentAsString(result) shouldBe s"${successWithBodyModel.response.get}"
    }

    "return a 404 status when the endpoint cannot be found" in {
      lazy val result = TestRequestHandlerController.getRequestHandler("/test")(FakeRequest())

      mockFind(List())
      status(result) shouldBe Status.NOT_FOUND
    }
  }
}

