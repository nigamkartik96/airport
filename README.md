# Airport Service

---

## First API, Weight API

### Problem Statement

For requested Flight Number and date will respond with following :

- Cargo Weight for requested Flight
- Baggage Weight for requested Flight
- Total Weight for requested Flight

### Request Example

~~~
curl --location --request GET 
'http://localhost:8080/airport/get_weight?
flightNumber=7755&date=2020-09-01T02:02:02Z'
~~~

### Response Example

1. Correct Response - `HTTP Status Code: 200`

~~~
{
    "cargoWeight": 1533.57,
    "baggageWeight": 793.66,
    "totalWeight": 2327.24,
    "error": null
}
~~~

2. Bad Request-  `HTTP Status Code: 400`

~~~
{
    "cargoWeight": null,
    "baggageWeight": null,
    "totalWeight": null,
    "error": "Flight Number (flightNumber) and Date (date) cannot be empty"
}
~~~

3. Incorrect Date Format - `HTTP Status Code: 400`

~~~
{
    "cargoWeight": null,
    "baggageWeight": null,
    "totalWeight": null,
    "error": "Enter Date (date) in UTC format (YYYY-MM-ddThh:mm:ssZ)"
}
~~~

---

## Second API, Airport Details API

### Problem Statement
For requested IATA Airport Code and date will respond with following :
- Number of flights departing from this airport,
- Number of flights arriving to this airport,
- Total number (pieces) of baggage arriving to this airport,
- Total number (pieces) of baggage departing from this airport.

### Request Example

~~~
curl --location --request GET 
'http://localhost:8080/airport/airport_details?
iATAAirportCode=YYZ&date=2020-09-01T02:02:02Z'
~~~

### Response Example

1. Correct Response - `HTTP Status Code: 200`

~~~
{
    "numberOfFlightDeparting": 2,
    "numberOfFlightArriving": 0,
    "totalNumberOfPiecesArriving": 0,
    "totalNumberOfPiecesDeparting": 9241,
    "error": null
}
~~~

2. Bad Request-  `HTTP Status Code: 400`

~~~
{
    "numberOfFlightDeparting": -1,
    "numberOfFlightArriving": -1,
    "totalNumberOfPiecesArriving": -1,
    "totalNumberOfPiecesDeparting": -1,
    "error": "IATA Airport Code (iATAAirportCode) and Date (date) cannot be empty"
}
~~~

3. Incorrect Date Format - `HTTP Status Code: 400`

~~~
{
    "numberOfFlightDeparting": -1,
    "numberOfFlightArriving": -1,
    "totalNumberOfPiecesArriving": -1,
    "totalNumberOfPiecesDeparting": -1,
    "error": "Enter Date (date) in UTC format (YYYY-MM-ddThh:mm:ssZ)"
}
~~~
4. Flight Details not found - `HTTP Status Code: 200`
~~~
{
    "numberOfFlightDeparting": -1,
    "numberOfFlightArriving": -1,
    "totalNumberOfPiecesArriving": -1,
    "totalNumberOfPiecesDeparting": -1,
    "error": "IATAAirportCode=YYXZ doesn't have any flight details"
}
~~~