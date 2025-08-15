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
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent, ControllerComponents, Request}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}

@Singleton
class StatusController @Inject()(cc: ControllerComponents) extends BackendController(cc) with Logging {
  def ratepayerStatus(id: String): Action[AnyContent] = Action { implicit request =>
    logger.info(s"Received ratepayerStatus call with id: $id from user agent: ${printHeader(USER_AGENT)}\n$hc\nBody: $printBody")

    val response = id match {
      case "TEST_UNKNOWN" => buildRatepayerStatusResponse("UNKNOWN", "Unknown. The bridge has no details of this ratepayer. Possibly a signal that something has gone wrong if the Ratepayer has registered via a frontend service.")
      case "TEST_INPROGRESS" => buildRatepayerStatusResponse("INPROGRESS", "In progress. Case officers are examining the ratepayer application but have not yet decided.")
      case "TEST_ACCEPTED" => buildRatepayerStatusResponse("ACCEPTED", "Registered. The ratepayer details have been accepted by the VOA.")
      case "TEST_REJECTED" => buildRatepayerStatusResponse("REJECTED", "Rejected. The ratepayer details have been rejected by the VOA.")
      case _ => buildRatepayerStatusResponse("UNKNOWN", "Unknown. The bridge has no details of this ratepayer. Possibly a signal that something has gone wrong if the Ratepayer has registered via a frontend service.")
    }

    Ok(response)
  }

  private def buildRatepayerStatusResponse(ratepayerStatus: String, error: String): JsValue = {
    Json.obj(
      "ratepayerStatus" -> ratepayerStatus,
      "error" -> Some(error)
    )
  }

  private def printHeader(headerName: String)(implicit request: Request[AnyContent]): String =
    request.headers.get(headerName).getOrElse("")

  private def printBody(implicit request: Request[AnyContent]): String =
    request.body.asJson
      .fold(request.body.toString)(_.toString)
}


