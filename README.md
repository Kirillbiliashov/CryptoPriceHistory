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
## Screenshots
### List with trading data items
<img width="412" alt="paging_list" src="https://github.com/Kirillbiliashov/CryptoPriceHistory/assets/81979605/793caad9-6a35-4e39-8fb0-9ae756088d70">

### Progress bar when data is loading
<img width="368" alt="refresh_loading" src="https://github.com/Kirillbiliashov/CryptoPriceHistory/assets/81979605/001c0e24-023a-4970-ad51-f67f41dcd16a">
<img width="381" alt="append_loading" src="https://github.com/Kirillbiliashov/CryptoPriceHistory/assets/81979605/0cc32573-a50a-4400-a208-91dd509b4314">

### Error message with try again button

<img width="392" alt="refresh_error" src="https://github.com/Kirillbiliashov/CryptoPriceHistory/assets/81979605/0c4e3495-4d2a-4c19-bc05-939ad29cdaa3">

<img width="386" alt="append_error" src="https://github.com/Kirillbiliashov/CryptoPriceHistory/assets/81979605/f7bb50c5-80e7-420c-bb51-17f582e59499">

### Empty result set or incorrect query scenario

<img width="391" alt="no_result" src="https://github.com/Kirillbiliashov/CryptoPriceHistory/assets/81979605/fb2fb495-7c5a-46dd-bb5c-8e5f7de071c2">
