-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         9.0.0 - MySQL Community Server - GPL
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


-- Volcando estructura de base de datos para cuentas
DROP DATABASE IF EXISTS `cuentas`;
CREATE DATABASE IF NOT EXISTS `cuentas` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `cuentas`;

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
	(1, '2024-07-07 19:22:21.357000', 1425, 'Retiro', '-575', 1),
	(2, '2024-07-07 19:22:50.856000', 700, 'Deposito', '600', 2),
	(3, '2024-07-07 19:23:16.886000', 150, 'Deposito', '150', 3),
	(4, '2024-07-07 19:23:32.114000', 0, 'Retiro', '540', 4);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
