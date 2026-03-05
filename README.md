# Sistema de Gestión Bancaria - Docker Deployment

Este proyecto contiene dos microservicios Spring Boot para la gestión de clientes y cuentas bancarias, desplegados con Docker y Docker Compose.

## 🏗️ Arquitectura

- **clientes-service**: Microservicio para gestión de clientes (puerto 8081)
- **cuentas-service**: Microservicio para gestión de cuentas (puerto 8082)
- **mysql**: Base de datos MySQL 8.0 (puerto 3306)

## 📋 Prerrequisitos

- Docker Desktop instalado
- Maven 3.6+
- Java 17+
- Git

## 🚀 Despliegue Rápido

### Opción 1: Usar el script de despliegue

**Windows:**
```bash
build-and-deploy.bat
```

**Linux/Mac:**
```bash
chmod +x build-and-deploy.sh
./build-and-deploy.sh
```

### Opción 2: Despliegue manual

1. **Construir las aplicaciones:**
```bash
cd clientes
mvn clean package -DskipTests
cd ../cuentas
mvn clean package -DskipTests
cd ..
```

2. **Levantar los contenedores:**
```bash
docker-compose up --build -d
```

## 🔧 Configuración

### Variables de Entorno

El archivo `.env` contiene la configuración:

```env
MYSQL_ROOT_PASSWORD=password
MYSQL_DATABASE=banco
MYSQL_USERNAME=root
CLIENTES_SERVICE_URL=http://clientes-service:8081
```

### Personalización

Puedes modificar las variables en el archivo `.env` según tus necesidades:

- `MYSQL_ROOT_PASSWORD`: Contraseña del usuario root de MySQL
- `MYSQL_DATABASE`: Nombre de la base de datos
- `MYSQL_USERNAME`: Usuario de la base de datos

## 📊 Acceso a los Servicios

Una vez desplegados, los servicios estarán disponibles en:

- **API Clientes**: http://localhost:8081
- **API Cuentas**: http://localhost:8082
- **MySQL**: localhost:3306

## 📋 Comandos Útiles

### Ver estado de los contenedores
```bash
docker-compose ps
```

### Ver logs
```bash
# Todos los servicios
docker-compose logs -f

# Servicio específico
docker-compose logs -f clientes-service
docker-compose logs -f cuentas-service
docker-compose logs -f mysql
```

### Detener servicios
```bash
docker-compose down
```

### Reiniciar servicios
```bash
docker-compose restart
```

### Reconstruir imágenes
```bash
docker-compose build --no-cache
```

## 🔍 Verificación del Despliegue

1. **Verificar que los servicios están corriendo:**
```bash
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
```

2. **Probar la comunicación entre servicios:**
El servicio `cuentas` debería poder comunicarse con `clientes` a través del Feign Client.

## 🗂️ Estructura del Proyecto

```
Ejercicio_T-cnico/
├── clientes/                 # Microservicio de clientes
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── cuentas/                  # Microservicio de cuentas
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── docker-compose.yml        # Orquestación de contenedores
├── .env                      # Variables de entorno
├── build-and-deploy.sh       # Script de despliegue (Linux/Mac)
├── build-and-deploy.bat      # Script de despliegue (Windows)
└── README.md                 # Este archivo
```

## 🐛 Troubleshooting

### Problemas Comunes

1. **Puertos en uso:** Asegúrate de que los puertos 8081, 8082 y 3306 estén disponibles.

2. **Error de conexión a la base de datos:** Verifica que el contenedor MySQL esté corriendo antes que los servicios de aplicaciones.

3. **Error de construcción:** Asegúrate de tener Maven y Java 17 instalados.

### Limpieza Completa

Para eliminar todos los contenedores, imágenes y volúmenes:
```bash
docker-compose down -v --rmi all
```

## 📝 Notas Importantes

- Los servicios están configurados para comunicarse internamente a través de la red Docker
- La base de datos persiste sus datos en un volumen Docker
- Los archivos SQL en `BaseDatos.sql` se ejecutan automáticamente al iniciar el contenedor MySQL
- Los servicios tienen habilitado el retry mechanism para manejar fallos de conexión

## 🤝 Contribución

Para realizar cambios en los servicios:

1. Modifica el código fuente en las carpetas `clientes/` o `cuentas/`
2. Reconstruye las imágenes: `docker-compose build --no-cache`
3. Reinicia los servicios: `docker-compose up -d`
