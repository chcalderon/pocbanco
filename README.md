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
