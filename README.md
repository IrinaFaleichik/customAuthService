# Custom Authentication Service
## Small web authentication service built with ZIO

project (will be) consists of two parts:
1) auth-core: library with authentication logic
2) auth-service: REST API for authentication

meant to be used for other web projects

### (Future) Structure:
//todo get from grille-encoder
```
auth-project/
├── auth-core/
│   ├── src/
│   │   ├── main/scala/
│   │   │   ├── com/irka/auth/
│   │   │   │   ├── identity/
│   │   │   │   │   ├── Identity.scala
│   │   │   │   │   ├── EmailIdentity.scala
│   │   │   │   │   └── UsernameIdentity.scala
│   │   │   │   ├── password/
│   │   │   │   │   ├── HashedPassword.scala
│   │   │   │   │   └── HashingUtils.scala
│   │   │   │   ├── token/
│   │   │   │   │   ├── AuthToken.scala
│   │   │   │   │   └── TokenValidator.scala
│   │   │   │   └── errors/
│   │   │   │       └── AuthError.scala
│   │   │   └── build.sbt
├── auth-service/
│   ├── src/
│   │   ├── main/scala/
│   │   │   ├── com/irka/auth/
│   │   │   │   ├── api/
│   │   │   │   │   ├── AuthApi.scala
│   │   │   │   │   ├── UserApi.scala
│   │   │   │   │   └── TokenApi.scala
│   │   │   │   ├── service/
│   │   │   │   │   ├── AuthService.scala
│   │   │   │   │   └── TokenService.scala
│   │   │   │   └── persistence/
│   │   │   │       └── UserRepository.scala
│   │   │   └── build.sbt
└── build.sbt
```
### Service usage:

POST host:port/authenticate with JSON:
```
{"username": "username", "password": "password"}
```
or
```
{"email": "username@yo.com", "password": "password"}
```
Results with JWT token in the response body.

- Supports only JSON

### Defined service behavior:

### Lib usage:

### Common info:
Identity supported:
- username identity
- email identity

### Defined behaviour:

### Build and run and/or integrate:

```
git clone https://github.com/IrinaFaleichik/customAuthService.git
```

