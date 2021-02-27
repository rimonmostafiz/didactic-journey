# didactic-journey
## Task manager REST API

## Technology Used
- Java 11
- Maven
- MySQL
- Spring Boot
- Spring Security
- Spring Data JPA
- [JSON Web Token](https://jwt.io/)
- [Flyway](https://flywaydb.org/) (Database Migration)
- [Swagger](https://swagger.io/) (API Documentation and API Client)

### Optional Tools
- [cURL](https://curl.se/) (command line tool and library
for transferring data with URLs)
- [Postman](https://www.postman.com/) (API Client)

## Build
**Build Profile - dev**
```
mvn clean package -P dev -DskipTest
```

**Build Profile - prod**
```
mvn clean package -P orid -DskipTest
```

## Run
**Profile - dev**
```
mvn clean spring-boot:run -P prod -DskipTest
```

**Profile - prod**
```
mvn clean spring-boot:run -P prod -DskipTest
```

**NOTE:** 
- On application startup a new database with name `taskdb` will be created(if not exists already).
- Flyway will migrate db. Will create tables and add initial data.
- Two default user will be created while db migration.

    | Username      | Password    | ROLE        |             |
    | -----------   | ----------- | ----------- | ----------- |
    | admin         | admin1      | ADMIN       |             |
    | user          | password    | USER        |             |

# API Description
Root URL of the project is `/task-manager`

## Swagger URL
Click swagger link below to see all the API endpoints after running the project successfully
http://localhost:8080/task-manager/swagger-ui/#/

![swagger](swagger.jpg)

## Authencation
There are two endpoint for authentication purpose

**POST /auth/login** - Request with Username and Password, If authentication is successful then in response it will send jwt `access_token`, `refresh_token`. For every other subsequent request a valid jwt `access_token` is expected in `Autherization` header.  
- Token Validity (can be configured in `application.properties`)
    - `access_token` is 60 Minutes(1 Hour)
    - `refresh_token` is 360 Minutes(6 Hours)

### Example:  
#### **Request**
```

$ curl -X POST 'http://localhost:8080/task-manager/auth/login' -H 'Content-Type: application/json' -d '{"username":"admin","password":"admin1"}' | json_pp
```
_NOTE:_ `| json_pp` is for pretty printing json output. It is completely optional.
#### **Response**
```
{
   "statusCode" : "SUCCESS",
   "success" : {
      "data" : {
         "refresh_token" : "eyJ0eXAiOiJyZWZyZXNoX3Rva2VuIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYxNDQyNzQ1NCwiZXhwIjoxNjE0NDQ5MDU0fQ.BbBPihvf88j2vS1Yow04pZEDpYhlRRgRNNegL_cLxvc",
         "access_token" : "eyJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIkFETUlOIl0sImlhdCI6MTYxNDQyNzQ1NCwiZXhwIjoxNjE0NDMxMDU0fQ.oqu-7OPSj4ZDwWdLT_0gkqgkIhwKSqs0fs3eeb0PEPY",
         "username" : "admin"
      }
   },
   "status" : "OK"
}
```

**POST /auth/refresh** - This is a special API endpoint. When `access_token` is expired then user can request new access token with `refresh_token`. Token should be sent on `Authorization` header with the prefix `Bearer<space>`.
### Example:  
#### **Request**
```
curl -X POST 'http://localhost:8080/task-manager/auth/refresh' -H 'Authorization: Bearer eyJ0eXAiOiJyZWZyZXNoX3Rva2VuIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYxNDQyNzY5NiwiZXhwIjoxNjE0NDQ5Mjk2fQ.Qf5-WRNHHVkHLC-NERHWfemzcDapLO327CFgQ5IO0IA' | json_pp
```
#### **Response**
```
{
   "statusCode" : "SUCCESS",
   "status" : "CREATED",
   "success" : {
      "data" : {
         "username" : "admin",
         "access_token" : "eyJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIkFETUlOIl0sImlhdCI6MTYxNDQyNzg4OSwiZXhwIjoxNjE0NDMxNDg5fQ.6_hhaE8BLUQO25HgSduFlilc6A-vOJPe6RzpGzNEsKc"
      }
   }
}
```
