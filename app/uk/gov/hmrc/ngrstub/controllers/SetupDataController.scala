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

import play.api.Logging
import uk.gov.hmrc.ngrstub.models.DataModel

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent, ControllerComponents, Result}
import uk.gov.hmrc.ngrstub.models.HttpMethod._
import uk.gov.hmrc.ngrstub.services.DataService
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

@Singleton
class SetupDataController @Inject()(DataService: DataService, cc: ControllerComponents)(implicit executionContext: ExecutionContext) extends BackendController(cc) with Logging{

  val addData: Action[JsValue] = Action.async(parse.json) {
    implicit request => withJsonBody[DataModel](
      json => json.method.toUpperCase match {
        case GET | POST | PUT =>
          addStubDataToDB(json)
        case x =>
          val message = s"The method: $x is currently unsupported"
          logger.warn(s"[SetupDataController][addData] - $message")
          Future.successful(BadRequest(message))
      }
    ). recover {
      case ex =>
        val message = s"Error Parsing Json DataModel due to exception: ${ex.getMessage}"
        logger.warn(s"[SetupDataController][addData] - $message")
        InternalServerError(message)
    }
  }

  private def addStubDataToDB(json: DataModel): Future[Result] = {
    DataService.addEntry(json).map {
      case result if result.wasAcknowledged() => Ok(s"The following JSON was added to the stub: \n\n${Json.toJson(json)}")
      case _ =>
        val message = "Failed to add data to Stub."
        logger.warn(s"[SetupDataController][addStubDataToDB] - $message")
        InternalServerError(message)
    }
  }

  val removeData: String => Action[AnyContent] = url => Action.async {
    DataService.removeById(url).map {
      case result if result.wasAcknowledged() => Ok("Success")
      case _ =>
        val message = "Could not delete data"
        logger.warn(s"[SetupDataController][removeData] - $message")
        InternalServerError(message)
    }
  }

  val removeAll: Action[AnyContent] = Action.async {
    DataService.removeAll().map {
      case result if result.wasAcknowledged() => Ok("Removed All Stubbed Data")
      case _ =>
        val message = "Unexpected Error Clearing MongoDB."
        logger.warn(s"[SetupDataController][removeAll] - $message")
        InternalServerError(message)
    }
  }
}
