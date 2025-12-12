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


### Populate all data 
URLs:
* `ngr-stub/add-all-data`

Methods:
* `POST`

Request body:
* An array of valid JSON data items (Read all JSON files in the stubData folder and insert them as a sequence into MongoDB using addMany)

Response codes:
* `200` - when population was successful
* `500` - when population was unsuccessful due to an unexpected reason

## JSON formats

### Data
```
{
  "_id": "/ratepayer/12345",
  "method": "GET",
  "status": 200,
  "response": {
    //the JSON body the stubbed API will return
  }
}
```

* `_id` - the URL
* `method` - the HTTP method for the API request
* `status` - the status code returned by the API
* `response` - the JSON body returned by the API (optional)

### The data used to populate the mongo db can be found in the `stubData` folder.

Each JSON file in the `stubData` folder represents a stubbed API response. The file name typically matches the API
endpoint, and its contents follow the format described above, including the `_id` (URL), `method`, `status`, and
`response` fields. These files are used to populate the MongoDB database, allowing the stub service to return predefined
responses for specific API requests.

### Example stub data

| **\_id**                                                                | **method**  | **status** | **response**   |
|-------------------------------------------------------------------------|-------------|------------|----------------|
| /ngr-stub/external-ndr-list-api/properties?postcode=BH1                 | GET         | 200        | json response  |
| /ngr-stub/external-ndr-list-api/properties?postcode=LS1                 | GET         | 404        | json response  |
| /ngr-stub/get-property-linking-user-answers/credld/9900000000000101     | GET         | 200        | json response  |
| /ngr-stub/get-ratepayer/credld/12378912                                 | GET         | 200        | json response  |
| /ngr-stub/get-ratepayer/credld/1237891256                               | GET         | 200        | json response  |
| /ngr-stub/get-ratepayer/credld/12378912678                              | GET         | 200        | json response  |
| /ngr-stub/get-ratepayer/credld/1237891267812                            | GET         | 200        | json response  |
| /ngr-stub/get-ratepayer/credld/99999                                    | GET         | 200        | json response  |
| /ngr-stub/hip-ratepayer-status/testCred123                              | GET         | 200        | json response  |
| /ngr-stub/hip/job/physical                                              | POST        | 200        | json response  |
| /ngr-stub/hip/job/ratepayer                                             | POST        | 200        | json response  |
| /ngr-stub/hip/job/ratepayer/1234567891255                               | GET         | 400        | json response  |
| /ngr-stub/hip/job/ratepayer/GGID123345                                  | GET         | 200        | json response  |
| /ngr-stub/hip/voa/v1/job                                                | POST        | 200        | json response  |
| /ngr-stub/hip/voa/v1/job/properties?assessmentld=\*&personForeignld=\*  | GET         | 200        | json response  |
| /ngr-stub/hip/voa/v1/job/property                                       | POST        | 200        | json response  |
| /ngr-stub/hip/voa/v1/job/ratepayers/\*                                  | GET         | 200        | json response  |
| /ngr-stub/hip/voa/v1/job/ratepayers/\*/dashboard                        | GET         | 200        | json response  |
| /ngr-stub/ngrPropertyStatus/AA000002D                                   | GET         | 200        | json response  |
| /ngr-stub/ngrPropertyStatus/AA000003D                                   | GET         | 200        | json response  |
| /ngr-stub/ngrPropertyStatus/AA000007D                                   | GET         | 200        | json response  |


Create stub data in the above format to add more stubbed API responses as needed by creating new JSON files in the `stubData` folder.


## Running the service
The service can be started with `sbt run`

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").