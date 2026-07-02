-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: com_shop
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `delivery`
--

DROP TABLE IF EXISTS `delivery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '配送ID',
  `ord_id` int NOT NULL COMMENT '订单ID',
  `emp_id` int NOT NULL COMMENT '配送员ID',
  `stage` tinyint DEFAULT '0' COMMENT '状态: 0未派送 1正在派送 2已送达',
  `arr_time` date DEFAULT NULL COMMENT '送达时间',
  `note` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ord_id` (`ord_id`),
  KEY `idx_emp_id` (`emp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='配送表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery`
--

LOCK TABLES `delivery` WRITE;
/*!40000 ALTER TABLE `delivery` DISABLE KEYS */;
INSERT INTO `delivery` VALUES (1,11,2,2,'2026-07-02',''),(5,12,2,2,'2026-07-02','原石'),(6,13,2,1,NULL,'厉害了'),(7,14,2,1,NULL,'');
/*!40000 ALTER TABLE `delivery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '员工ID',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `stage` int unsigned DEFAULT '0' COMMENT '状态: 0空闲 1配送中',
  `entry_time` date DEFAULT NULL COMMENT '入职日期',
  `username` varchar(30) NOT NULL COMMENT '登录账号',
  `password` varchar(30) NOT NULL COMMENT '登录密码',
  `job` int unsigned DEFAULT '0' COMMENT '岗位: 0管理员1 配送员',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'方糕挚爱',0,'2026-06-07','fanggaozhiai','123456',0),(2,'狗',1,'2026-07-01','dog','123456',1),(3,'李四',0,'2024-06-20','lisi01','123456',1),(4,'王五',0,'2025-01-10','wangwu88','123456',1),(5,'赵六',0,'2025-05-08','zhaoliu','123456',1),(6,'孙七',0,'2025-08-22','sunqi99','123456',1),(7,'周八',0,'2025-11-11','zhouba','123456',1),(8,'吴九',0,'2026-01-05','wujiu77','123456',1),(9,'郑十',0,'2026-03-18','zhengshi','123456',1),(10,'鸡',0,'2026-07-01','ji','123456',1),(11,'猪',0,'2026-07-01','pig','123456',1),(13,'林鹏',0,'2023-05-12','lin686','LmRZ7L07',1),(14,'黄丽',0,'2024-06-14','huang928','kvFnt0SS',1),(15,'刘艳',0,'2021-06-28','liu153','siybQJQB79',1),(16,'何昊',0,'2020-10-26','he655','GnBrRjXAPyd',1),(17,'刘静',0,'2024-05-19','liu356','Y3K947c1K2b',1),(18,'杨雨',0,'2021-10-19','yang846','bn9rGpYyxYP',1),(19,'杨明',0,'2021-07-08','yang610','nyxD4gwGZCUi',1),(20,'周秀英',0,'2025-10-01','zhou982','Ba7uM23tR3',1),(21,'杨鹏',0,'2021-08-06','yang872','szhfJX0e5',1),(22,'马浩',0,'2023-06-04','ma274','ikbT0fXJ',1);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `merchant`
--

DROP TABLE IF EXISTS `merchant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `merchant` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '名称',
  `person` varchar(255) NOT NULL DEFAULT '' COMMENT '联系人',
  `phone` varchar(50) NOT NULL DEFAULT '' COMMENT '电话',
  `us_id` int unsigned NOT NULL COMMENT '对应用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `merchant`
--

LOCK TABLES `merchant` WRITE;
/*!40000 ALTER TABLE `merchant` DISABLE KEYS */;
INSERT INTO `merchant` VALUES (1,'无敌大商店','fanggao','114514',1),(2,'原神之商店','fanggao','114514',1);
/*!40000 ALTER TABLE `merchant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `us_id` int NOT NULL COMMENT '下单用户ID',
  `money` double DEFAULT NULL COMMENT '订单金额（分）',
  `stage` tinyint DEFAULT '0' COMMENT '状态: 0未送达 1已送达',
  `re_address` varchar(200) DEFAULT NULL COMMENT '收货地址',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `pay_time` date DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`),
  KEY `idx_us_id` (`us_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (11,2,1900,1,'提瓦特大陆','2026-07-01',NULL),(12,1,1900,2,'提瓦特','2026-07-02',NULL),(13,1,5700,1,'333','2026-07-02',NULL),(14,1,1,1,'提瓦特','2026-07-02',NULL);
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `ord_id` int NOT NULL COMMENT '订单ID',
  `pro_id` int NOT NULL COMMENT '商品ID',
  `num` int NOT NULL COMMENT '商品数量',
  `total` double DEFAULT NULL,
  `per_money` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_ord_id` (`ord_id`),
  KEY `idx_pro_id` (`pro_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (2,11,3,1,1900,1900),(3,12,3,1,1900,1900),(4,13,3,3,5700,1900),(5,14,4,10,1,0.1);
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `describe` varchar(500) DEFAULT NULL COMMENT '商品描述',
  `img` varchar(300) DEFAULT NULL COMMENT '商品图片URL',
  `stage` tinyint DEFAULT '1' COMMENT '状态: 0有货 1无货',
  `mer_id` int NOT NULL COMMENT '所属商铺ID',
  `price` double NOT NULL COMMENT '商品价格',
  PRIMARY KEY (`id`),
  KEY `idx_mer_id` (`mer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (2,'原石','ys的原石','https://community--shop.oss-cn-beijing.aliyuncs.com/2026/07/01/7b147791-7d2c-4f2d-9ff9-fbda4b190a54.jpg',1,1,0),(3,'小原石','原石','https://community--shop.oss-cn-beijing.aliyuncs.com/2026/07/01/d53d2d56-51cd-4c14-abf7-68981ad998b6.gif',0,1,1900),(4,'源石','粥游之ys','https://community--shop.oss-cn-beijing.aliyuncs.com/2026/07/02/3091a2fa-cf26-419a-b70a-409c11292bf9.jpg',0,2,0.1);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `address` varchar(500) DEFAULT NULL COMMENT '地址',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'fanggao','11223344','123456','有顶天','2026-06-09'),(2,'yuanshen','114514','123456','家','2026-07-01');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-02 20:36:16
