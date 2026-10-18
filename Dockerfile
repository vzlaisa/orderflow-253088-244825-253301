
# ---- etapa 1: build ----

# Imagen con Maven y Java 21 para compilar el proyecto.    
FROM maven:3.9-eclipse-temurin-21 AS build
# Define /app como directorio de trabajo.
WORKDIR /app
# Copia el proyecto Maven completo al contenedor.
COPY . .
# Compila orders-api y los módulos requeridos, sin ejecutar pruebas.
RUN mvn -B -pl orders-api -am package -DskipTests

# ---- etapa 2: runtime ----

# Imagen ligera con Java 21 para ejecutar la aplicación.
FROM eclipse-temurin:21-jre-alpine
# Define /app como directorio de trabajo.
WORKDIR /app
# Copia el JAR generado desde la etapa de compilación.
COPY --from=build /app/orders-api/target/*.jar app.jar
# Inicia la aplicación cuando se ejecuta el contenedor.
ENTRYPOINT ["java", "-jar", "app.jar"]