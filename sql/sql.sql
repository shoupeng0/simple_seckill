CREATE DATABASE seckill;
USE seckill;

DROP TABLE IF EXISTS `seckill_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seckill_goods` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `goods_id` bigint(20) DEFAULT NULL COMMENT '商品id',
                                 `seckil_price` decimal(10,2) DEFAULT NULL COMMENT '秒杀价',
                                 `stock_count` int(11) DEFAULT NULL COMMENT '秒杀数量',
                                 `start_date` datetime DEFAULT NULL,
                                 `end_date` datetime DEFAULT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `seckill_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seckill_order` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `user_id` bigint(20) DEFAULT NULL,
                                 `order_id` bigint(20) DEFAULT NULL,
                                 `goods_id` bigint(20) DEFAULT NULL,
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `u_userid_goodsid` (`user_id`,`goods_id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `order_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_info` (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT,
                              `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
                              `goods_id` bigint(20) DEFAULT NULL COMMENT '商品id',
                              `addr_id` bigint(20) DEFAULT NULL COMMENT '收货地址id',
                              `goods_name` varchar(16) DEFAULT NULL COMMENT '冗余过来的商品名称',
                              `goods_count` int(11) DEFAULT NULL COMMENT '商品数量',
                              `goods_price` decimal(10,2) DEFAULT NULL COMMENT '商品价格',
                              `order_channel` int(2) DEFAULT '0' COMMENT '支付通道：1 PC、2 Android、3 ios',
                              `status` int(2) DEFAULT NULL COMMENT '订单状态：0 未支付，1已支付，2 已发货，3 已收货，4 已退款，‘5 已完成',
                              `create_date` datetime DEFAULT NULL,
                              `pay_date` datetime DEFAULT NULL COMMENT '支付时间',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

LOCK TABLES `seckill_goods` WRITE;
/*!40000 ALTER TABLE `seckill_goods` DISABLE KEYS */;
INSERT INTO `seckill_goods` VALUES (1,1,6888.00,500,'2024-11-5 10:06:20','2024-11-15 19:06:20');
delete from seckill_order where 1=1;
/*!40000 ALTER TABLE `seckill_goods` ENABLE KEYS */;
UNLOCK TABLES;