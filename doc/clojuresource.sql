-- MySQL dump 10.13  Distrib 5.5.32, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: clojuresources
-- ------------------------------------------------------
-- Server version	5.5.32-0ubuntu0.13.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Category`
--

DROP TABLE IF EXISTS `Category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Category`
--

LOCK TABLES `Category` WRITE;
/*!40000 ALTER TABLE `Category` DISABLE KEYS */;
INSERT INTO `Category` VALUES (1,'IDES','IDES',1),(2,'LOGGING TOOLS','LOGGING TOOLS',1),(3,'Mail Client','Mail Client',1),(4,'SQL Database library','SQL Database library',1),(5,'NoSQL Database library','NoSQL Database library',1),(6,'Web Development','Web Development',1),(7,'Web Templating','Web Templating',1),(8,'Web Security','Web Security',1),(9,'REST','REST',1),(10,'Algorithm','Algorithm',1),(11,'Performance','Performance',1),(12,'Rule engine','Rule engine',1),(13,'Validation','Validation',1);
/*!40000 ALTER TABLE `Category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Licence`
--

DROP TABLE IF EXISTS `Licence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Licence` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Licence`
--

LOCK TABLES `Licence` WRITE;
/*!40000 ALTER TABLE `Licence` DISABLE KEYS */;
INSERT INTO `Licence` VALUES (1,'Eclipse Public License (EPL)',NULL),(2,'Academic Free License (AFL)',NULL),(3,'Apache Software License',NULL),(4,'BSD License',NULL),(5,'Common Development and Distribution License (CDDL)',NULL),(6,'Common Public License (CPL)',NULL),(7,'GNU General Public License (GPL)',NULL),(8,'GNU Library or Lesser General Public License (LGPL',NULL),(9,'MIT License',NULL),(10,'Mozilla Public License',NULL),(11,'Public Domain',NULL),(12,'Sun Public License (SPL)',NULL),(13,'Other',NULL);
/*!40000 ALTER TABLE `Licence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Project`
--

DROP TABLE IF EXISTS `Project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` TEXT DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `license_id` int(11) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `homepage` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Project`
--

LOCK TABLES `Project` WRITE;
/*!40000 ALTER TABLE `Project` DISABLE KEYS */;
/*!40000 ALTER TABLE `Project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fullname` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `salt` varchar(100) DEFAULT NULL,
  `verification_token` varchar(100) DEFAULT NULL,
  `forgotpassword_token` varchar(100) DEFAULT NULL,
  `token` varchar(100) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `created_ts` date DEFAULT NULL,
  `updated_ts` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
INSERT INTO `Users` VALUES (9,'Sougata Bhattacharya','sougata@neevtech.com','$2a$10$qCmQmDXsTrBoT2gGDHAux.OkPfTiEX70/ARA.2ORqgLIUn.x0sAei','$2a$10$qCmQmDXsTrBoT2gGDHAux.','28bbd6637f1f511eb8663a3c1314c80b','28bbd6637f1f511eb8663a3c1314c80b',NULL,'ACTIVE','2013-10-22',NULL),(10,'Sougata Bhattacharya','sougata@gmail.com','$2a$10$i86h3LOncVRE1MSbrMplAODk1kkL6bYu1MoG3yoF0z3bpFkguWcsW','$2a$10$i86h3LOncVRE1MSbrMplAO','35247fc008b693966bf1c410dc82f70','35247fc008b693966bf1c410dc82f70',NULL,'ACTIVE','2013-10-24',NULL);
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-10-25 23:41:56
