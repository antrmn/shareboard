-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: shareboard
-- ------------------------------------------------------
-- Server version	8.0.22

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
-- Dumping data for table `admin`
--

/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;

--
-- Dumping data for table `ban`
--

/*!40000 ALTER TABLE `ban` DISABLE KEYS */;
INSERT INTO `ban` VALUES (1,1,NULL,4,'2021-05-04 10:30:27',NULL,1),(2,1,1,5,'2021-05-04 10:30:27',NULL,0);
/*!40000 ALTER TABLE `ban` ENABLE KEYS */;

--
-- Dumping data for table `comment`
--

/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,1,1,NULL,'Commento 1','2021-05-04 10:28:13'),(2,1,1,1,'Risposta a commento 1','2021-05-04 10:28:13'),(3,1,1,NULL,'Commento2','2021-05-04 10:28:13'),(4,1,1,2,'Risposta a risposta a commento 1','2021-05-04 10:28:13');
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;

--
-- Dumping data for table `comment_vote`
--

/*!40000 ALTER TABLE `comment_vote` DISABLE KEYS */;
INSERT INTO `comment_vote` VALUES (2,1,1),(2,2,1),(2,3,1),(2,4,1),(3,2,1),(3,3,-1),(4,3,-1);
/*!40000 ALTER TABLE `comment_vote` ENABLE KEYS */;

--
-- Dumping data for table `follow`
--

/*!40000 ALTER TABLE `follow` DISABLE KEYS */;
INSERT INTO `follow` VALUES (1,1),(1,2),(3,2);
/*!40000 ALTER TABLE `follow` ENABLE KEYS */;

--
-- Dumping data for table `post`
--

/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,1,1,'Post1','Appartengo a sezion1 e autore1','TEXT','2021-03-30 12:49:31'),(2,1,3,'Post2','Appartengo a sezione1 e autore3','TEXT','2021-03-30 12:49:31'),(3,2,2,'Post3','Appartengo a sezione2 e autore2','TEXT','2021-03-30 12:49:31'),(4,3,4,'Post4','Appartengo a sezione3 e autore4','TEXT','2021-03-30 12:49:31'),(5,5,5,'Post5','Appartengo a sezione5 e autore5','TEXT','2021-03-30 12:49:31');
/*!40000 ALTER TABLE `post` ENABLE KEYS */;

--
-- Dumping data for table `post_vote`
--

/*!40000 ALTER TABLE `post_vote` DISABLE KEYS */;
INSERT INTO `post_vote` VALUES (2,1,1),(2,2,1),(2,3,1),(2,4,1),(3,2,1),(3,3,-1),(4,3,-1);
/*!40000 ALTER TABLE `post_vote` ENABLE KEYS */;

--
-- Dumping data for table `section`
--

/*!40000 ALTER TABLE `section` DISABLE KEYS */;
INSERT INTO `section` VALUES (3,'Descrizione1','Sezione1',NULL),(4,'Descrizione2','Sezione2',NULL),(5,'Descrizione3','Sezione3',NULL);
/*!40000 ALTER TABLE `section` ENABLE KEYS */;

--
-- Dumping data for table `user`
--

/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Utente1','password1','utente1@email.it',NULL,NULL,'2021-03-30 12:41:26'),(2,'Utente2','password2','utente2@email.it',NULL,NULL,'2021-03-30 12:41:26'),(3,'Utente3','password3','utente3@email.it',NULL,NULL,'2021-03-30 12:41:26'),(4,'Utente4','password4','utente4@email.it',NULL,NULL,'2021-03-30 12:41:26'),(5,'Utente5','password5','utente5@email.it',NULL,NULL,'2021-03-30 12:41:26');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-04 12:34:06
