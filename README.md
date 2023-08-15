# CryptoPriceHistory
## What is this project?
This project is a simple Android app that displays crypto price history in a list. The project does not contain complex logic and it is not ready-to-use production app. 
The main goal is to get paging data from API and store it in cache using Retrofit, Room and Paging library.
So consider it as fully implemented paging functionality for API data, no more no less.
## How it works
First of all, I picked API for crypto price history, which is https://api.binance.com/api/v3/klines. This endpoint requires mandatory symbol query param, which is currency pair. Using this API, my app exposes price change every 1 hour for a given symbol. The endpoint does not support paging explicitly using id or page, so I came up with a different solution: page through data using startTime and limit query params. This allows to specify page size (limit) and offset/page (startTime). So when I launch next page, I just decrement start time so that new data is queried.
## Features implemented
* Custom API deserialization (because the API doesn't support customary key-value pairs and returns data as array of arrays).
* Data caching using Room Database. Actually, trading data and next/prev page for any given trading data is cached.
* RemoteMediator, which is responsible for loading data from API when all cached data is already displayed and saving it to db.
* Custom separators - items between trading data info items. In my case, seaprators are placed whenever trading data for a new day is displayed.
* Progress indicator - corresponds to the loading state of the data.
* Retry button - if data needs to be loaded but the user has no internet connection, they will have the ability to reload data when their internet connectin is back on. 
