@echo off
echo 🔨 Construyendo aplicaciones Spring Boot...

REM Construir el servicio de clientes
echo 📦 Construyendo servicio de clientes...
cd clientes
call mvn clean package -DskipTests
cd ..

REM Construir el servicio de cuentas
echo 📦 Construyendo servicio de cuentas...
cd cuentas
call mvn clean package -DskipTests
cd ..

echo 🐳 Iniciando contenedores Docker...

REM Detener y eliminar contenedores existentes
docker-compose down

REM Construir y levantar los contenedores
docker-compose up --build -d

echo ⏳ Esperando a que los servicios estén listos...
timeout /t 30 /nobreak

echo 🔍 Verificando estado de los servicios...
docker-compose ps

echo ✅ Despliegue completado!
echo 📊 Servicio de clientes: http://localhost:8081
echo 🏦 Servicio de cuentas: http://localhost:8082
echo 🗄️  Base de datos MySQL: localhost:3306

echo 📋 Para ver los logs:
echo    docker-compose logs -f clientes-service
echo    docker-compose logs -f cuentas-service
echo    docker-compose logs -f mysql

pause
