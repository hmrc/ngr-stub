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
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.{defaultAwaitTimeout, status}
import uk.gov.hmrc.ngrstub.models.DataModel


class SetupDataControllerSpec extends TestSupport with MockDataService {

  object TestSetupDataController extends SetupDataController(mockDataService, cc)

  "SetupDataController.addData" when {

    val model: DataModel = DataModel(
      _id = "1234",
      method = "GET",
      status = Status.OK,
      response = Some(Json.parse("{}"))
    )

    "a stub entry is loaded into the stub" should {

      "return Status OK (200) if the data is loaded successfully" in {
        lazy val request = FakeRequest().withBody(Json.toJson(model)).withHeaders(("Content-Type", "application/json"))
        lazy val result = TestSetupDataController.addData(request)

        mockAddEntry(successWriteResult)
        status(result) shouldBe Status.OK

      }

      "return Status InternalServerError (500) if unable to add data to the stub" in {
        lazy val request = FakeRequest().withBody(Json.toJson(model)).withHeaders(("Content-Type", "application/json"))
        lazy val result = TestSetupDataController.addData(request)

        mockAddEntry(errorWriteResult)
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }

      "not a GET request" should {

        "return Status BadRequest (400)" in {
          lazy val request = FakeRequest()
            .withBody(Json.toJson(model.copy(method = "DELETE"))).withHeaders(("Content-Type", "application/json"))
          lazy val result = TestSetupDataController.addData(request)

          status(result) shouldBe Status.BAD_REQUEST
        }
      }

      "SetupDataController.removeData" should {

        "return Status OK (200) on successful removal of data from the stub" in {
          lazy val request = FakeRequest()
          lazy val result = TestSetupDataController.removeData("someUrl")(request)

          mockRemoveById(successDeleteResult)

          status(result) shouldBe Status.OK
        }

        "return Status InternalServerError (500) on unsuccessful removal of data from the stub" in {
          lazy val request = FakeRequest()
          lazy val result = TestSetupDataController.removeData("someUrl")(request)

          mockRemoveById(errorDeleteResult)

          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        }
      }

      "SetupDataController.populateAllData" should {
        "return Status OK (200) on successful insertion of all stubbed data" in {
          lazy val request = FakeRequest()
          lazy val result = TestSetupDataController.populateAllData()(request)

          mockAddMany(successManyWriteResult)

          status(result) shouldBe Status.OK
        }

        "return Status InternalServerError (500) on unsuccessful insertion of all stubbed data" in {
          lazy val request = FakeRequest()
          lazy val result = TestSetupDataController.populateAllData()(request)

          mockAddMany(errorManyWriteResult)

          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        }
      }

      "SetupDataController.removeAllData" should {

        "return Status OK (200) on successful removal of all stubbed data" in {
          lazy val request = FakeRequest()
          lazy val result = TestSetupDataController.removeAll()(request)

          mockRemoveAll(successDeleteResult)

          status(result) shouldBe Status.OK
        }

        "return Status InternalServerError (500) on successful removal of all stubbed data" in {
          lazy val request = FakeRequest()
          lazy val result = TestSetupDataController.removeAll()(request)
          mockRemoveAll(errorDeleteResult)
          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        }
      }
    }
    }
}
