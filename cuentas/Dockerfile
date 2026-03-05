# Usar la imagen base de Eclipse Temurin JDK 17
FROM eclipse-temurin:17

# Información del autor
LABEL author=darwin

# Copiar el archivo JAR generado por Maven al contenedor
COPY target/cuentas-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto que Spring Boot está utilizando
EXPOSE 8082

# Definir el comando de entrada
ENTRYPOINT ["java", "-jar", "app.jar"]