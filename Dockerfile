# --- Etapa 1: Construcción con Maven ---
FROM maven:3.9-eclipse-temurin-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos primero el pom.xml para aprovechar la caché de capas de Docker.
COPY pom.xml .

# Descargamos todas las dependencias del proyecto
RUN mvn dependency:go-offline

# Copiamos el resto del código fuente de tu aplicación
COPY src ./src

# Empaquetamos la aplicación en un archivo .jar. Omitimos los tests para acelerar.
RUN mvn package -DskipTests


# --- Etapa 2: Creación de la imagen final ---
FROM eclipse-temurin:17-jre-jammy

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el .jar que creamos en la etapa anterior ("build") a esta nueva imagen.
COPY --from=build /app/target/api-0.0.1-SNAPSHOT.jar ./app.jar

# Exponemos el puerto 8080, que es el que usa Spring Boot por defecto.
EXPOSE 8080

# Este es el comando que se ejecutará cuando el contenedor se inicie.
ENTRYPOINT ["java", "-jar", "app.jar"]