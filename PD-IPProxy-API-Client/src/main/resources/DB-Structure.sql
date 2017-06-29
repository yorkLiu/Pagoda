DROP TABLE IF EXISTS `proxys`;
CREATE TABLE `proxys` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(16) NOT NULL,
  `port` int(11) NOT NULL,
  `types` int(11) NOT NULL,
  `protocol` int(11) NOT NULL,
  `country` varchar(100) NOT NULL,
  `area` varchar(100) NOT NULL,
  `updatetime` datetime DEFAULT NULL,
  `speed` decimal(5,2) NOT NULL,
  `score` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `proxyaudit`;
CREATE TABLE `proxyaudit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(16) NOT NULL,
  `port` int(11) NOT NULL,
  `types` int(11) NOT NULL,
  `protocol` int(11) NOT NULL,
  `area` varchar(100) NOT NULL,
  `tokenId` varchar(150) DEFAULT NULL,
  `proxyId` bigint(20) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8;