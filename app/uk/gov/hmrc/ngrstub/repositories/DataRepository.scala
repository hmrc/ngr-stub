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

package uk.gov.hmrc.ngrstub.repositories

import org.mongodb.scala.model.{IndexModel, IndexOptions, Indexes}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository
import uk.gov.hmrc.ngrstub.common.Constants
import uk.gov.hmrc.ngrstub.models.DataModel

import java.util.concurrent.TimeUnit
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class DataRepository @Inject()(mongo: MongoComponent)
                              (implicit ec: ExecutionContext) extends PlayMongoRepository[DataModel](
  mongoComponent = mongo,
  collectionName = "data",
  domainFormat   = DataModel.formats,
  indexes        = Seq(IndexModel(
    Indexes.ascending("creationTimestamp"),
    IndexOptions().name("expiry").expireAfter(Constants.timeToLiveInSeconds, TimeUnit.SECONDS)
  )),
  replaceIndexes = true
)

