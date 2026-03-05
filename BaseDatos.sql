-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         8.0.38 - MySQL Community Server - GPL
-- SO del servidor:              Linux
-- HeidiSQL Versión:             12.7.0.6850
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Volcando estructura para tabla clientes.clientes
DROP TABLE IF EXISTS `clientes`;
CREATE TABLE IF NOT EXISTS `clientes` (
  `contrasena` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `estado` tinyint(1) DEFAULT '1',
  `cliente_id` bigint NOT NULL,
  PRIMARY KEY (`cliente_id`),
  CONSTRAINT `FK3b2u85sny49u8gmk3bkrt3gjy` FOREIGN KEY (`cliente_id`) REFERENCES `personas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla clientes.clientes: ~3 rows (aproximadamente)
INSERT INTO `clientes` (`contrasena`, `estado`, `cliente_id`) VALUES
	('1234', 1, 1),
	('1245', 1, 2),
	('5678', 1, 3);

-- Volcando estructura para tabla clientes.personas
DROP TABLE IF EXISTS `personas`;
CREATE TABLE IF NOT EXISTS `personas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `direccion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `edad` int DEFAULT NULL,
  `genero` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `identificacion` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `telefono` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKdpxdn543sbyt8xkvsqha0l1li` (`identificacion`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla clientes.personas: ~3 rows (aproximadamente)
INSERT INTO `personas` (`id`, `direccion`, `edad`, `genero`, `identificacion`, `nombre`, `telefono`) VALUES
	(1, 'Otavalo sn y principal ', 18, 'Masculino', '0123456789', 'Jose Lema', '098254785'),
	(2, '13 junio y Equinoccial', 30, 'Masculino', '9876543210', 'Juan Osorio', '098874587'),
	(3, ' Amazonas y NNUU ', 21, 'Femenino', '0147852369', 'Marianela Montalvo', '097548965');

-- Volcando estructura para tabla cuentas.cuentas
DROP TABLE IF EXISTS `cuentas`;
CREATE TABLE IF NOT EXISTS `cuentas` (
  `cuenta_id` bigint NOT NULL AUTO_INCREMENT,
  `cliente_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `estado` tinyint(1) DEFAULT '1',
  `numero` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `saldo_inicial` double NOT NULL,
  `tipo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`cuenta_id`),
  UNIQUE KEY `UK7sa6xm9anjkmpftqttuyhko56` (`numero`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla cuentas.cuentas: ~5 rows (aproximadamente)
INSERT INTO `cuentas` (`cuenta_id`, `cliente_id`, `estado`, `numero`, `saldo_inicial`, `tipo`) VALUES
	(1, '1', 1, '478758', 2000, 'Ahorro'),
	(2, '3', 1, '225487', 100, 'Corriente'),
	(3, '2', 1, '495878', 0, 'Ahorros'),
	(4, '3', 1, '496825', 540, 'Ahorros'),
	(5, '1', 1, '585545', 1000, 'Corriente');

-- Volcando estructura para tabla cuentas.movimientos
DROP TABLE IF EXISTS `movimientos`;
CREATE TABLE IF NOT EXISTS `movimientos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fecha` datetime(6) NOT NULL,
  `saldo` double NOT NULL,
  `tipo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `valor` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `cuenta_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4moe88hxuohcysas5h70mdc09` (`cuenta_id`),
  CONSTRAINT `FK4moe88hxuohcysas5h70mdc09` FOREIGN KEY (`cuenta_id`) REFERENCES `cuentas` (`cuenta_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla cuentas.movimientos: ~4 rows (aproximadamente)
INSERT INTO `movimientos` (`id`, `fecha`, `saldo`, `tipo`, `valor`, `cuenta_id`) VALUES
	(1, '2026-03-03 19:22:21.357000', 1425, 'Retiro', '-575', 1),
	(2, '2026-03-03 19:22:50.856000', 700, 'Deposito', '600', 2),
	(3, '2026-03-03 19:23:16.886000', 150, 'Deposito', '150', 3),
	(4, '2026-03-03 19:23:32.114000', 0, 'Retiro', '540', 4);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
