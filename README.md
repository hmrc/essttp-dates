
# essttp-dates

Dates µservice for essttp

## Requests and responses

* [POST /start-dates](https://github.com/hmrc/essttp-dates#post-start-dates)


### POST /start-dates
Endpoint locally: http://localhost:9219/essttp-dates/start-dates

Request body:
```json
{
  "initialPayment": true,
  "preferredDayOfMonth": 1
}
```

Response:

```json
HTTP/1.1 200 OK
Cache-Control: no-cache,no-store,max-age=0
Date: Tue, 07 Jun 2022 07:06:24 GMT
Content-Type: application/json
Content-Length: 70

{
  "initialPaymentDate": "2022-06-17",
  "instalmentStartDate": "2022-08-01"
}
```


### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").