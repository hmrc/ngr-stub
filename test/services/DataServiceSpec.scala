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

package services

import helpers.TestSupport
import org.mongodb.scala.bson.Document
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.mongo.test.DefaultPlayMongoRepositorySupport
import uk.gov.hmrc.ngrstub.models.DataModel
import uk.gov.hmrc.ngrstub.repositories.DataRepository
import uk.gov.hmrc.ngrstub.services.DataService

class DataServiceSpec extends TestSupport with DefaultPlayMongoRepositorySupport[DataModel]{

  override lazy val repository = new DataRepository(mongoComponent)
  lazy val service = new DataService(mongoComponent)

  "DataService" should {

    "find documents using equal(key, value) branch" in {
      val document = DataModel(
        _id = "/some-other",
        method = "GET",
        status = 200,
        response = None
      )

      await(service.addEntry(document))

      await(service.repository.collection.createIndex(Document("method" -> 1)).toFuture())

      val result = await(service.find(Seq("method" -> "GET")))

      result should contain(document)
    }

    "find matching documents in the collection" in {
      val result = {
        await(service.addEntry(dataModel))
        await(service.find(Seq("_id" -> "/test")))
      }
      result shouldBe List(dataModel)
    }

    "remove one document from the collection" in {
      val result = {
        await(service.addEntry(dataModel))
        await(service.removeById("/test"))
      }
      result shouldBe successDeleteResult
    }

    "remove all documents from the collection" in {
      val result = {
        await(service.addEntry(dataModel))
        await(service.removeAll())
      }
      result shouldBe successDeleteResult
    }

    "insert a given document" in {
      val result = await(service.addEntry(dataModel))
      result.wasAcknowledged() shouldBe true
    }

    "contain a DataRepository" in {
      service.repository.getClass shouldBe repository.getClass
    }
  }
}
