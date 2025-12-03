/*
 * Copyright 2025 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package uk.gov.hmrc.ngrstub.controllers

import helpers.TestSupport
import mocks.MockDataService
import play.api.http.Status.{ACCEPTED, CREATED, NOT_FOUND, OK}
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout, status}
import uk.gov.hmrc.ngrstub.models.DataModel

class RequestHandlerControllerSpec extends TestSupport with MockDataService {

  object TestRequestHandlerController extends RequestHandlerController(mockDataService, cc)

  lazy val successModel: DataModel = DataModel(
    _id = "/test",
    method = "GET",
    status = OK,
    response = None
  )

  lazy val wildcardModel: DataModel = DataModel(
    _id = "/test/*",
    method = "GET",
    status = OK,
    response = Some(Json.parse("""{"something":"wildcard"}"""))
  )

  lazy val successWithBodyModel: DataModel = DataModel(
    _id = "/test",
    method = "GET",
    status = OK,
    response = Some(Json.parse("""{"something":"hello"}"""))
  )

  lazy val successPOSTModel: DataModel = DataModel(
    _id = "/test",
    method = "POST",
    status = CREATED,
    response = None
  )

  lazy val successPUTModel: DataModel = DataModel(
    _id = "/test",
    method = "PUT",
    status = ACCEPTED,
    response = None
  )

  "The getRequestHandler method" should {

    "return the status code specified in the model" in {
      mockFind(List(successModel))
      val result = TestRequestHandlerController.getRequestHandler("/test")(
        FakeRequest("GET", "/test")
      )
      status(result) shouldBe OK
    }

    "return the status and body" in {
      mockFind(List(successWithBodyModel))
      val result = TestRequestHandlerController.getRequestHandler("/test")(
        FakeRequest("GET", "/test")
      )
      status(result) shouldBe OK
      contentAsString(result) shouldBe s"${successWithBodyModel.response.get}"
    }

    "return the status and body for wildcard match" in {
      mockFind(List(wildcardModel))
      val result = TestRequestHandlerController.getRequestHandler("/test/cheese")(
        FakeRequest("GET", "/test/cheese")
      )
      status(result) shouldBe OK
      contentAsString(result) shouldBe s"${wildcardModel.response.get}"
    }

    "return the status code specified for POST model" in {
      mockFind(List(successPOSTModel))
      val result = TestRequestHandlerController.postRequestHandler("/test")(
        FakeRequest("POST", "/test")
      )
      status(result) shouldBe CREATED
    }

    "return the status code specified for PUT model" in {
      mockFind(List(successPUTModel))
      val result = TestRequestHandlerController.putRequestHandler("/test")(
        FakeRequest("PUT", "/test")
      )
      status(result) shouldBe ACCEPTED
    }

    "return a 404 status when the endpoint cannot be found" in {
      mockFind(List())
      val result = TestRequestHandlerController.getRequestHandler("/notfound")(
        FakeRequest("GET", "/notfound")
      )
      status(result) shouldBe NOT_FOUND
    }
  }
}
