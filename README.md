# Microservicio de Creación y Consulta de Usuarios

## Requerimientos

- Java 11
- Spring Boot 2.5.14
- Gradle 7.4
- H2 Database

# Clonar el repositorio
git clone git@github.com:chcalderon/pocbanco.git
# Construir el proyecto
cd user-service
./gradlew build
o
gradle build
# Ejecutar la aplicación
java -jar build/libs/user-service-0.0.1-SNAPSHOT.jar

# para solo ejecutar desde linea de comandos con gradle
./gradlew bootRun
o
gradle bootRun

# Ejecucion de test
./gradlew test
o
gradle test

# Generacion de xml para revision con jacoco
./gradlew test jacocoTestReport
o
gradle test jacocoTestReport

# Revision de test
build/jacocoHtml/index.html

# Para verificar si se cumple el 80% de cobertura:

./gradlew check
o
gradle check

# Ejemplo de ejecucion

1. Registro de Usuario - POST /sign-up

curl -X POST http://localhost:8080/api/users/sign-up \
-H "Content-Type: application/json" \
-d '{
    "name": "John Doe",
    "email": "johndoe@example.com",
    "password": "a2asffDdfdf4",
    "phones": [
        {"number": 123456789, "citycode": 1, "countrycode": "57"}
    ]
}'

Respuesta esperada:

201- created
{
    "id": "6f47a918-fca6-4a08-95ed-88d2e0d33705",
    "created": "2025-01-17T17:53:35.9480042",
    "lastLogin": "2025-01-17T17:53:35.9480042",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lQGV4YW1wbGUuY29tIiwiaWF0IjoxNzM3MTQ3MjE1LCJleHAiOjE3MzcxNTA4MTV9.6nWLc2-rchGRHpjMvUZUvhhqrpHCsJugyxJCam8rg34",
    "active": true
}

2. Login de Usuario - GET /login

curl -X GET "http://localhost:8080/api/users/login" \
-H "Authorization: Bearer token" //reemplace token por el valor entregado en el paso 1

respuesta esperada:

200-ok

{
    "id": "6f47a918-fca6-4a08-95ed-88d2e0d33705",
    "created": "2025-01-17T17:53:35.948004",
    "lastLogin": "2025-01-17T17:54:21.811622300",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lQGV4YW1wbGUuY29tIiwiaWF0IjoxNzM3MTQ3MjYxLCJleHAiOjE3MzcxNTA4NjF9.zhLmxPtE_sK3k4OgtYOi9_XXrr5jYX0rKlDfiGjgMX8",
    "name": "John Doe",
    "email": "johndoe@example.com",
    "password": "a2asffDdfdf4",
    "phones": [
        {
            "number": 123456789,
            "citycode": 1,
            "countrycode": "57"
        }
    ],
    "active": true
}

# Swagger para muestreo de endpoints
http://localhost:8080/swagger-ui/index.html