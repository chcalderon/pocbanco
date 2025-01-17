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

#Ejemplo de ejecucion

1. Registro de Usuario - POST /sign-up

curl -X POST http://localhost:8080/users/sign-up \
-H "Content-Type: application/json" \
-d '{
  "name": "Julio Gonzalez",
  "email": "julio@testssw.cl",
  "password": "a2asfGfdfdf4",
  "phones": [
    {
      "number": 87650009,
      "citycode": 7,
      "contrycode": "25"
    }
  ]
}'

Respuesta esperada:

201- created
{
  "id": "e5c6cf84-8860-4c00-91cd-22d3be28904e",
  "created": "2025-01-13T12:00:00",
  "lastLogin": "2025-01-13T12:00:00",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWxpb0B0ZXN0...",
  "isActive": true,
  "name": "Julio Gonzalez",
  "email": "julio@testssw.cl",
  "password": "a2asfGfdfdf4",
  "phones": [
    {
      "number": 87650009,
      "citycode": 7,
      "contrycode": "25"
    }
  ]
}

2. Login de Usuario - GET /login

curl -X GET "http://localhost:8080/users/login" \
-H "Authorization: Bearer token" //reemplace token por el valor entregado en el paso 1

respuesta esperada:

200-ok

{
    "id": "7ee1a461-22e1-4fe3-aa88-eeab8a68050c",
    "created": "2025-01-17T15:07:01.971744",
    "lastLogin": "2025-01-17T15:07:17.257561",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lQGV4YW1wbGUuY29tIiwiaWF0IjoxNzM3MTM3MjM3LCJleHAiOjE3MzcxNDA4Mzd9.4uoZA1LlMIWIAmfmUqGQPjHm1FN-IDM5DGqyCd_PKU8",
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