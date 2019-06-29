# Bank-app

#### Challenge
Design and implement a RESTful API (including data model and the backing implementation) for
money transfers between accounts.

#### Build & Run

**Build**

```mvn clean install```

or  

```. build.sh```

**Run**

```mvn jetty:run```

or  

```. run.sh```

**Integration Tests**

```mvn integration-test -Pfailsafe```

or  

```. integration-test.sh```

#### Usage

**Import Postman Collection**

```bank-app.postman_collection.json```

**Balances**

```
    curl -X GET \
     http://localhost:8080/account/balances \
     -H 'Cache-Control: no-cache' \
     -H 'Postman-Token: e1af2ea0-cec2-4ef1-b8e1-abaa5acc709e'
```

**Deposit**

```
    curl -X POST \
     http://localhost:8080/account/deposit \
     -H 'Cache-Control: no-cache' \
     -H 'Content-Type: application/json' \
     -H 'Postman-Token: c520050b-681c-4c06-9ea7-8050b13e65c5' \
     -d '{
   	"account": {
   		"branch":"001",
   		"number":"1247",
   		"digit":"0"
   	},
   	"amount":"900.95"
   }'"
```
**Transfer**

```
    curl -X POST \
     http://localhost:8080/account/transfer \
     -H 'Cache-Control: no-cache' \
     -H 'Content-Type: application/json' \
     -H 'Postman-Token: ae7fae16-79be-47a3-938e-52468bc44302' \
     -d '{
   	"accountOrigin": {
   		"branch":"001",
   		"number":"1247",
   		"digit":"0"
   	},
   	"accountDestination": {
   		"branch":"001",
   		"number":"1246",
   		"digit":"0"
   	},
   	"amount":"900.00"
   }'
```

#### Assumptions

1. Every account is created at its first deposit (including the deposit within a transfer transaction);
2. The origin account has to have enough funds;
3. A transfer transaction is only allowed from an existent account;
4. In a transfer transaction origin and destination account must not to be the same.

#### System Requirements

- Java 8+
- Maven 3+

#### Updates

1. Addition of integration tests with AccountResourceIT.java
2. The integration tests use all default modules, avoiding only the StorageDefault, which is alike a "production DB", therefore StorageStub was implemented