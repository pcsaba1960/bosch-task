CREATE DATABASE IF NOT EXISTS cs_beugro;

use cs_beugro;

CREATE TABLE `production` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pcb_id` int(11) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

