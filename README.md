
# essttp-dates

Dates µservice for essttp

---

## Models
The majority of the models live in the [essttp-backend service](https://github.com/hmrc/essttp-backend/tree/main/cor-journey/src/main/scala/essttp) for ease of reuse.

## Requests and responses

* [POST /start-dates](https://github.com/hmrc/essttp-dates#post-start-dates)
* [POST /extreme-dates](https://github.com/hmrc/essttp-dates#post-extreme-dates)

---

### POST /start-dates
Endpoint locally: http://localhost:9219/essttp-dates/start-dates

#### Initial payment true
Request body:
```json
{
  "initialPayment": true,
  "preferredDayOfMonth": 1
}
```
Response:

```
HTTP/1.1 200 OK
Content-Type: application/json
```
```json
{
  "initialPaymentDate": "2022-06-17",
  "instalmentStartDate": "2022-08-01"
}
```

#### Initial payment false
Request body:
```json
{
  "initialPayment": false,
  "preferredDayOfMonth": 1
}
```
Response:

```
HTTP/1.1 200 OK
Content-Type: application/json
```
```json
{
  "instalmentStartDate": "2022-08-01"
}
```

---

### POST /extreme-dates
Endpoint locally: http://localhost:9219/essttp-dates/extreme-dates

#### Initial payment true
Request body:
```json
{
  "initialPayment": true
}
```

Response:
```
HTTP/1.1 200 OK
Content-Type: application/json
```
```json
{
  "initialPaymentDate": "2022-06-17",
  "earliestPlanStartDate": "2022-08-01",
  "latestPlanStartDate": "2022-08-01"
}
```

#### Initial payment false
Request body:
```json
{
  "initialPayment": false
}
```

Response:
```
HTTP/1.1 200 OK
Content-Type: application/json
```
```json
{
  "earliestPlanStartDate": "2022-08-01",
  "latestPlanStartDate": "2022-08-01"
}
```

---

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").