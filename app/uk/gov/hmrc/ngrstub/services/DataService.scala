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

package uk.gov.hmrc.ngrstub.services

import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.result.{DeleteResult, InsertOneResult}
import org.mongodb.scala.model.Filters.{and, equal, or, regex}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.ngrstub.models.DataModel
import uk.gov.hmrc.ngrstub.repositories.DataRepository

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class DataService @Inject()(mongoComponent: MongoComponent)(implicit ec: ExecutionContext) {

  lazy val repository: DataRepository = new DataRepository(mongoComponent)

  def removeAll(): Future[DeleteResult] =
    repository.collection.deleteMany(org.mongodb.scala.model.Filters.empty()).toFuture()

  def removeById(url: String): Future[DeleteResult] =
    repository.collection.deleteOne(equal("_id", url)).toFuture()

  def addEntry(document: DataModel): Future[InsertOneResult] =
    repository.collection.insertOne(document).toFuture()

  def find(query: Seq[(String, String)]): Future[Seq[DataModel]] = {
    println(s"query: $query")
    val filters = query.map { case (key, value) =>
      if (key == "_id") {
        if (value.contains("*")) {
          val regexValue = value.replace("*", ".*")
          regex("_id", regexValue)
        } else {
          equal("_id", value)
        }
      } else {
        equal(key, value)
      }
    }

    val finalFilter = filters match {
      case Nil          => Document()
      case head :: Nil  => head
      case _            => and(filters: _*)
    }

    repository.collection.find(finalFilter).toFuture()
  }
}
