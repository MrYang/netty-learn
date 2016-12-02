CREATE TABLE `message` (
  `id` varchar(40) NOT NULL,
  `device_id` varchar(50) NOT NULL DEFAULT '',
  `title` varchar(50) NOT NULL DEFAULT '',
  `content` varchar(200) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `push_time` datetime DEFAULT NULL,
  `receipt` int(11) DEFAULT NULL,
  `online` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_device_id` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;