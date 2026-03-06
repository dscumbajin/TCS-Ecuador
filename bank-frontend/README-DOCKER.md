# Bank Frontend Docker Deployment

Este directorio contiene los archivos necesarios para desplegar la aplicación Angular en un contenedor Docker.

## Archivos creados:

### Dockerfile
- Multi-stage build que primero compila la aplicación Angular y luego la sirve con nginx
- Usa Node.js 18 Alpine para el build
- Usa nginx Alpine para producción
- Construye la aplicación en modo producción

### nginx.conf
- Configuración optimizada de nginx para aplicaciones Angular
- Soporta routing de Angular (SPA)
- Incluye compresión gzip
- Headers de seguridad
- Cacheo de assets estáticos

## Despliegue automático

El servicio `bank-frontend` ha sido añadido al `docker-compose.yml` principal:

- **Puerto**: 80 (accesible en http://localhost)
- **Dependencias**: Espera a que los servicios de clientes y cuentas estén listos
- **Red**: Conectado a la red `app-network` para comunicación con otros servicios
- **Restart**: Se reinicia automáticamente si falla

## Comandos útiles

```bash
# Construir y levantar todos los servicios
docker-compose up --build -d

# Ver logs del frontend
docker-compose logs -f bank-frontend

# Detener todos los servicios
docker-compose down

# Reconstruir solo el frontend
docker-compose up --build bank-frontend
```

## Acceso a la aplicación

Una vez desplegado, la aplicación estará disponible en:
- Frontend: http://localhost
- Clientes API: http://localhost:8081
- Cuentas API: http://localhost:8082
