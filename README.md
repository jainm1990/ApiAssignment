# User Application to Register, login and Upload, View and Delete Images REST API

To start this service, execute the below command:

    mvn spring-boot:run

## Register a User
POST request

    POST http://localhost:8080/v1/user/registration

Request body ::
```json
{
"firstName": "Mamta",
"lastName": "Mehta",
"password": "QWERTY",
"email": "mamta.mehta1@gmail.com",
"mobileNumber": "9770345678"
}
```

Response ::
```json
{
"responseCode": "1",
"responseMessage": "User Registration Success",
"status": "OK",
"timeStamp": "2023-09-11T06:32:55.913+00:00"
}
```

## Login to User to upload, view and delete Images

    POST http://localhost:8080/v1/user/authenticate

Request body ::
```json
{
"userName":"mamta.mehta1@gmail.com",
"password":"QWERTY"
}
```

Response ::
Access token will be returned to access all other APIs. Pass this in headers like
Bearer {access_token}
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYW10YS5tZWh0YTFAZ21haWwuY29tIiwiZXhwIjoxNjk0NDQ5OTg2LCJpYXQiOjE2OTQ0MTM5ODZ9.pIBw-vMnc-bEqxoPEFcON2jR46q4iKyP6AhJyRHrc-I

## Upload Image for Login User 

    POST http://localhost:8080/v1/images/upload

Headers ::
Authorization Bearer {access_token}

Request Body ::
![img.png](img.png)

## View Image for login user

    GET http://localhost:8080/v1/images/view

Headers ::
Authorization Bearer {access_token}

Request Body :: None

Response ::
This will return Image Link and hash from DB to delete the image

```json
{
"responseCode": "1",
"responseMessage": "[https://i.imgur.com/gHGJ9k.jpg,abcd],[https://i.imgur.com/gHGJ9k.jpg,ghjk]",
"status": "OK",
"timeStamp": "2023-09-11T06:34:31.521+00:00"
}
```

## Delete Image for login user
DELETE request

    Delete http://localhost:8080/v1/images/delete

Request body :: 

["1234", "abcd"]

Response body ::
```json
{
  "responseCode": "3",
  "responseMessage": "Delete Image Failed",
  "status": "Failed",
  "timeStamp": "2023-09-11T06:34:19.982+00:00"
}
```


