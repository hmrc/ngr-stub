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

package helpers

import com.mongodb.client.result.{DeleteResult, InsertManyResult, InsertOneResult}
import org.mongodb.scala.bson.{BsonInt32, BsonObjectId, BsonValue}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Json
import play.api.mvc.ControllerComponents
import play.api.test.Helpers.stubControllerComponents
import play.api.test.Injecting
import uk.gov.hmrc.ngrstub.models.DataModel

import scala.concurrent.ExecutionContext

trait TestSupport extends AnyWordSpec with GuiceOneAppPerSuite with Injecting
  with Matchers {

  val successWriteResult: InsertOneResult = InsertOneResult.acknowledged(BsonObjectId())
  val errorWriteResult: InsertOneResult = InsertOneResult.unacknowledged()
  val successManyWriteResult: InsertManyResult = InsertManyResult.acknowledged(
    new java.util.HashMap[Integer, BsonValue] {
      Integer.valueOf(1) -> new BsonInt32(32)
    }
  )
  val errorManyWriteResult: InsertManyResult = InsertManyResult.unacknowledged()
  val successDeleteResult: DeleteResult = DeleteResult.acknowledged(1)
  val errorDeleteResult: DeleteResult = DeleteResult.unacknowledged()

  implicit val ec: ExecutionContext = inject[ExecutionContext]

  lazy val cc: ControllerComponents = stubControllerComponents()

  val dataModel: DataModel = DataModel("/test", "GET", 200, response = Some(Json.parse("{}")))


}
