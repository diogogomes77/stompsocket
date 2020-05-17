
# stompsocket

Experiencing Web-Sockets with STOMP on Spring Boot

## To run

`docker-compose up`
The initial build takes some minutes depending on the Internet connection.
Wait until the terminal stops scrolling.

## To test

Open http://127.0.0.1 in a tab of the web-browser.

The form `#send_name_form`executes an ajax POST, crossdomain to the server API endpoint `http://127.0.0.1:8080/welcome` where SpringBoot is running. on port 8080.

This endpoint triggers a server broadcast ws message to all connected clients. The intent is to welcome the new client using the name inserted in the HTML form.
So, to see this feature working you must connect at least 2 browsers with different names.
Note: The endpoint can also be used by any other API client besides browsers.
For example, using cURL:
```
curl --location --request POST 'http://127.0.0.1:8080/welcome' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "diogo77"
}'
```
Other features included:

An initial user's list is received from the server on the response from the POST. This list is then updated by websocket messages when other users connect and disconnect.

Also, text sent on the form `#send_form` is broadcast as ws messages to a shared topic where all clients are subscribed by default.

For any question reach me mailto:diogo.gomes77[a]gmail . com