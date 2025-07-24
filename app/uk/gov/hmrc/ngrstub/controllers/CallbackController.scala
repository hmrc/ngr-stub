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
import play.api.libs.json.JsString
import play.api.mvc.{Action, AnyContent, ControllerComponents, Request}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}


/**
 * @author Yuriy Tumakha
 */
@Singleton
class CallbackController @Inject()(cc: ControllerComponents)
  extends BackendController(cc)
    with Logging {

  def receivedCallback: Action[AnyContent] = Action { implicit request =>
    logger.info(s"Received callback from User-Agent: ${printHeader(USER_AGENT)}\n$hc\nBody: $printBody")
    Ok(JsString("OK"))
  }

  private def printHeader(headerName: String)(implicit request: Request[AnyContent]): String =
    request.headers.get(headerName).getOrElse("")

  private def printBody(implicit request: Request[AnyContent]): String =
    request.body.asJson
      .fold(request.body.toString)(_.toString)

}
