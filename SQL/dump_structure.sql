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
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
                         `user_id` int NOT NULL,
                         PRIMARY KEY (`user_id`),
                         CONSTRAINT `fk_admin_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ban`
--

DROP TABLE IF EXISTS `ban`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ban` (
                       `id` int NOT NULL AUTO_INCREMENT,
                       `admin_id` int DEFAULT NULL,
                       `section_id` int DEFAULT NULL,
                       `user_id` int NOT NULL,
                       `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       `end_time` timestamp NULL DEFAULT NULL,
                       `is_global` tinyint(1) NOT NULL DEFAULT '1',
                       PRIMARY KEY (`id`),
                       KEY `section_id` (`section_id`),
                       KEY `fk_ban_admin_id` (`admin_id`),
                       KEY `fk_ban_user_id` (`user_id`),
                       CONSTRAINT `fk_ban_admin_admin_id` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`user_id`) ON DELETE SET NULL,
                       CONSTRAINT `fk_ban_section_section_id` FOREIGN KEY (`section_id`) REFERENCES `section` (`id`) ON DELETE CASCADE,
                       CONSTRAINT `fk_ban_user_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
                       CONSTRAINT `global_or_local` CHECK ((((`is_global` = 1) and (`section_id` is null)) or ((`is_global` = 0) and (`section_id` is not null))))
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `post_id` int NOT NULL,
                           `author_id` int NOT NULL,
                           `parent_comment_id` int DEFAULT NULL,
                           `content` text NOT NULL,
                           `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           PRIMARY KEY (`id`),
                           KEY `author_id` (`author_id`),
                           KEY `post_id` (`post_id`),
                           KEY `parent_comment_id` (`parent_comment_id`),
                           CONSTRAINT `fk_comment_comment_parent_comment_id` FOREIGN KEY (`parent_comment_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE,
                           CONSTRAINT `fk_comment_post_post_id` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE,
                           CONSTRAINT `fk_comment_user_author_id` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comment_vote`
--

DROP TABLE IF EXISTS `comment_vote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment_vote` (
                                `comment_id` int NOT NULL,
                                `user_id` int NOT NULL,
                                `vote` tinyint NOT NULL,
                                PRIMARY KEY (`comment_id`,`user_id`),
                                KEY `user_id` (`user_id`),
                                CONSTRAINT `fk_comment_vote_comment_comment_id` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE,
                                CONSTRAINT `fk_comment_vote_user_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `follow`
--

DROP TABLE IF EXISTS `follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `follow` (
                          `user_id` int NOT NULL,
                          `section_id` int NOT NULL,
                          `follow_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          PRIMARY KEY (`user_id`,`section_id`),
                          KEY `section_id` (`section_id`),
                          CONSTRAINT `fk_follow_section_section_id` FOREIGN KEY (`section_id`) REFERENCES `section` (`id`) ON DELETE CASCADE,
                          CONSTRAINT `fk_follow_user_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `section_id` int NOT NULL,
                        `author_id` int NOT NULL,
                        `title` varchar(255) NOT NULL,
                        `content` text NOT NULL,
                        `type` varchar(8) NOT NULL DEFAULT 'TEXT',
                        `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (`id`),
                        KEY `author_id` (`author_id`),
                        KEY `section_id` (`section_id`),
                        CONSTRAINT `fk_post_section_section_id` FOREIGN KEY (`section_id`) REFERENCES `section` (`id`) ON DELETE CASCADE,
                        CONSTRAINT `fk_post_user_author_id` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `post_vote`
--

DROP TABLE IF EXISTS `post_vote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_vote` (
                             `post_id` int NOT NULL,
                             `user_id` int NOT NULL,
                             `vote` tinyint NOT NULL,
                             PRIMARY KEY (`post_id`,`user_id`),
                             KEY `user_id` (`user_id`),
                             CONSTRAINT `fk_post_vote_post_post_id` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE,
                             CONSTRAINT `fk_post_vote_user_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `section`
--

DROP TABLE IF EXISTS `section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `section` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `description` varchar(255) DEFAULT NULL,
                           `name` varchar(50) NOT NULL,
                           `picture` varchar(4096) DEFAULT NULL,
                           `banner` varchar(4096) DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `username` varchar(30) NOT NULL,
                        `password` binary(16) NOT NULL,
                        `email` varchar(255) NOT NULL,
                        `description` varchar(255) DEFAULT NULL,
                        `picture` varchar(4096) DEFAULT NULL,
                        `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        `salt` binary(16) NOT NULL,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `username` (`username`),
                        UNIQUE KEY `email` (`email`),
                        UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `v_comment`
--

DROP TABLE IF EXISTS `v_comment`;
/*!50001 DROP VIEW IF EXISTS `v_comment`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_comment` AS SELECT
                                        1 AS `id`,
                                        1 AS `content`,
                                        1 AS `creation_date`,
                                        1 AS `author_id`,
                                        1 AS `parent_comment_id`,
                                        1 AS `post_id`,
                                        1 AS `votes`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_comment_complete`
--

DROP TABLE IF EXISTS `v_comment_complete`;
/*!50001 DROP VIEW IF EXISTS `v_comment_complete`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_comment_complete` AS SELECT
                                                 1 AS `id`,
                                                 1 AS `content`,
                                                 1 AS `creation_date`,
                                                 1 AS `author_id`,
                                                 1 AS `parent_comment_id`,
                                                 1 AS `post_id`,
                                                 1 AS `votes`,
                                                 1 AS `post_title`,
                                                 1 AS `author_username`,
                                                 1 AS `is_admin`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_post`
--

DROP TABLE IF EXISTS `v_post`;
/*!50001 DROP VIEW IF EXISTS `v_post`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_post` AS SELECT
                                     1 AS `id`,
                                     1 AS `title`,
                                     1 AS `content`,
                                     1 AS `type`,
                                     1 AS `creation_date`,
                                     1 AS `author_id`,
                                     1 AS `section_id`,
                                     1 AS `votes`,
                                     1 AS `n_comments`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_user`
--

DROP TABLE IF EXISTS `v_user`;
/*!50001 DROP VIEW IF EXISTS `v_user`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_user` AS SELECT
                                     1 AS `id`,
                                     1 AS `username`,
                                     1 AS `password`,
                                     1 AS `salt`,
                                     1 AS `email`,
                                     1 AS `description`,
                                     1 AS `picture`,
                                     1 AS `creation_date`,
                                     1 AS `is_admin`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `v_comment`
--

/*!50001 DROP VIEW IF EXISTS `v_comment`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=MERGE */
    /*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
    /*!50001 VIEW `v_comment` AS select `comment`.`id` AS `id`,`comment`.`content` AS `content`,`comment`.`creation_date` AS `creation_date`,`comment`.`author_id` AS `author_id`,`comment`.`parent_comment_id` AS `parent_comment_id`,`comment`.`post_id` AS `post_id`,ifnull(`v`.`sum_votes`,0) AS `votes` from (`comment` left join (select `comment_vote`.`comment_id` AS `comment_id`,sum(`comment_vote`.`vote`) AS `sum_votes` from `comment_vote` group by `comment_vote`.`comment_id`) `v` on((`v`.`comment_id` = `comment`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_comment_complete`
--

/*!50001 DROP VIEW IF EXISTS `v_comment_complete`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
    /*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
    /*!50001 VIEW `v_comment_complete` AS select `comment`.`id` AS `id`,`comment`.`content` AS `content`,`comment`.`creation_date` AS `creation_date`,`comment`.`author_id` AS `author_id`,`comment`.`parent_comment_id` AS `parent_comment_id`,`comment`.`post_id` AS `post_id`,ifnull(`v`.`sum_votes`,0) AS `votes`,`post`.`title` AS `post_title`,`user`.`username` AS `author_username`,(`a`.`user_id` is not null) AS `is_admin` from ((((`comment` left join (select `comment_vote`.`comment_id` AS `comment_id`,sum(`comment_vote`.`vote`) AS `sum_votes` from `comment_vote` group by `comment_vote`.`comment_id`) `v` on((`v`.`comment_id` = `comment`.`id`))) join `user` on((`comment`.`author_id` = `user`.`id`))) left join `admin` `a` on((`a`.`user_id` = `comment`.`author_id`))) join `post` on((`comment`.`post_id` = `post`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_post`
--

/*!50001 DROP VIEW IF EXISTS `v_post`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=MERGE */
    /*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
    /*!50001 VIEW `v_post` AS select `post`.`id` AS `id`,`post`.`title` AS `title`,`post`.`content` AS `content`,`post`.`type` AS `type`,`post`.`creation_date` AS `creation_date`,`post`.`author_id` AS `author_id`,`post`.`section_id` AS `section_id`,ifnull(`v`.`sum_votes`,0) AS `votes`,ifnull(`c`.`count_comments`,0) AS `n_comments` from ((`post` left join (select `post_vote`.`post_id` AS `post_id`,sum(`post_vote`.`vote`) AS `sum_votes` from `post_vote` group by `post_vote`.`post_id`) `v` on((`v`.`post_id` = `post`.`id`))) left join (select count(`comment`.`id`) AS `count_comments`,`comment`.`post_id` AS `post_id` from `comment` group by `comment`.`post_id`) `c` on((`c`.`post_id` = `post`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_user`
--

/*!50001 DROP VIEW IF EXISTS `v_user`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
    /*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
    /*!50001 VIEW `v_user` AS select `user`.`id` AS `id`,`user`.`username` AS `username`,`user`.`password` AS `password`,`user`.`salt` AS `salt`,`user`.`email` AS `email`,`user`.`description` AS `description`,`user`.`picture` AS `picture`,`user`.`creation_date` AS `creation_date`,(`a`.`user_id` is not null) AS `is_admin` from (`user` left join `admin` `a` on((`a`.`user_id` = `user`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-07-13 15:03:09
