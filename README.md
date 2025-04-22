# ngr-stub

## Summary
This stub microservice is used to receive, store and serve any of kind of stub data for the Next Generation Rates project. It is dynamic, allowing it to be a stub for any request to any API without needing to implement individual static routes. This is achieved by storing JSON objects in a Mongo database with IDs that are valid API URLs. When an incoming request is made, if a matching record is found then the JSON inside the `response` field of the record will be returned.

## Using the service
The stub can be used to store data that corresponds to a specific URL

### Storing
URLs:
* `ngr-stub/data`

Methods:
* `POST`

Request body:
* A valid JSON data item (see [data format](#data))

Response codes:
* `200` - when population was successful
* `400` - when population was unsuccessful due to an unsupported HTTP method
* `500` - when population was unsuccessful due to an unexpected reason

### Requesting data
URLs:
* `ngr-stub/*` - this can be anything and is intended to substitute a URL for the given microservice. If a JSON record exists in the database with an ID matching this URL, it will serve the data.

Methods:
* `GET`

Response codes:
* `*` - if an existing record is found that matches the requested URL, the status code provided in the record will be returned
* `404` - when no matching record is found

### Removing data
URLs:
* `ngr-stub/all-data`

Methods:
* `DELETE`

Response codes:
* `200` - when deletion was successful
* `500` - when deletion was unsuccessful due to an unexpected reason

## JSON formats

### Data
```
{
  "_id": "/ratepayer/12345",
  "schemaId": "getRatePayer",
  "method": "GET",
  "status": 200,
  "response": {
    //the JSON body the stubbed API will return
  }
}
```

* `_id` - the URL
* `schemaId` - the unique ID that must match a schema populated in the stub
* `method` - the HTTP method for the API request
* `status` - the status code returned by the API
* `response` - the JSON body returned by the API (optional)

## Running the service
The service can be started with `sbt run`

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").