-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: JobVerse
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activities`
--

DROP TABLE IF EXISTS `activities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activities` (
  `empEmail` varchar(50) DEFAULT NULL,
  `activity` text,
  `activityTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `user` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fullname` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `announcements`
--

DROP TABLE IF EXISTS `announcements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `announcements` (
  `id` int NOT NULL AUTO_INCREMENT,
  `audience` enum('All','Employers','JobSeekers') NOT NULL,
  `title` varchar(255) NOT NULL,
  `message` text NOT NULL,
  `postedDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `applicantsstatus`
--

DROP TABLE IF EXISTS `applicantsstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `applicantsstatus` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role` varchar(100) DEFAULT NULL,
  `position` varchar(100) DEFAULT NULL,
  `companyName` varchar(100) DEFAULT NULL,
  `jobSeekerEmail` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `appliedjobs`
--

DROP TABLE IF EXISTS `appliedjobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appliedjobs` (
  `JobID` int NOT NULL AUTO_INCREMENT,
  `UserEmail` varchar(100) DEFAULT NULL,
  `CompanyName` varchar(100) NOT NULL,
  `Location` varchar(100) DEFAULT NULL,
  `JobType` varchar(50) DEFAULT NULL,
  `SkillsRequired` text,
  `Salary` decimal(10,2) DEFAULT NULL,
  `PostedDate` date DEFAULT NULL,
  `currentDate` date DEFAULT NULL,
  `Note` text,
  `Reason` text,
  `ResumePDF` longblob,
  `FileName` varchar(255) DEFAULT NULL,
  `id` int NOT NULL,
  `employerEmail` varchar(255) DEFAULT NULL,
  `companyLogo` longblob,
  `logoFileName` varchar(255) DEFAULT NULL,
  `statusUpdate` varchar(20) DEFAULT 'Pending',
  `companyEmail` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`JobID`)
) ENGINE=InnoDB AUTO_INCREMENT=249 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chatlog`
--

DROP TABLE IF EXISTS `chatlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chatlog` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sender` varchar(100) DEFAULT NULL,
  `receiver` varchar(100) DEFAULT NULL,
  `message` text,
  `timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `companies`
--

DROP TABLE IF EXISTS `companies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `companies` (
  `id` int NOT NULL AUTO_INCREMENT,
  `companyName` varchar(100) NOT NULL,
  `companyEmail` varchar(255) DEFAULT NULL,
  `employersEmail` varchar(255) DEFAULT NULL,
  `employersName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `companyprofile`
--

DROP TABLE IF EXISTS `companyprofile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `companyprofile` (
  `companyID` int NOT NULL AUTO_INCREMENT,
  `companyEmail` varchar(255) NOT NULL,
  `companyName` varchar(255) NOT NULL,
  `tagline` varchar(255) DEFAULT NULL,
  `foundedYear` year DEFAULT NULL,
  `headquarters` varchar(255) DEFAULT NULL,
  `industry` varchar(100) DEFAULT NULL,
  `companySize` varchar(50) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  `linkedin` varchar(255) DEFAULT NULL,
  `twitter` varchar(255) DEFAULT NULL,
  `instagram` varchar(255) DEFAULT NULL,
  `aboutCompany` text,
  `logo` longblob,
  `logoFileName` varchar(255) DEFAULT NULL,
  `createdAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `openPositions` int DEFAULT '0',
  `hiringStatus` varchar(50) DEFAULT 'No recent activity',
  PRIMARY KEY (`companyID`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `deletedapplications`
--

DROP TABLE IF EXISTS `deletedapplications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `deletedapplications` (
  `id` int NOT NULL AUTO_INCREMENT,
  `companyName` varchar(100) DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `jobType` varchar(50) DEFAULT NULL,
  `salary` varchar(50) DEFAULT NULL,
  `requirement` text,
  `postedAt` datetime DEFAULT NULL,
  `position` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `efeedback`
--

DROP TABLE IF EXISTS `efeedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `efeedback` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userEmail` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `rating` int NOT NULL,
  `likes` text,
  `dislikes` text,
  `feedbackDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `employers`
--

DROP TABLE IF EXISTS `employers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `EJVID` varchar(10) DEFAULT NULL,
  `fullname` varchar(100) NOT NULL,
  `employerEmail` varchar(255) NOT NULL,
  `jobrole` varchar(100) NOT NULL,
  `companyname` varchar(100) NOT NULL,
  `companyemail` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `website` varchar(100) NOT NULL,
  `industry` varchar(100) NOT NULL,
  `status` varchar(50) DEFAULT 'active',
  `signup_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `EJVID` (`EJVID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_employers` BEFORE INSERT ON `employers` FOR EACH ROW BEGIN
    DECLARE nextId INT;
    SELECT IFNULL(MAX(id), 0) + 1 INTO nextId FROM employers;
    SET NEW.EJVID = CONCAT('EJV', LPAD(nextId, 4, '0'));
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `hirings`
--

DROP TABLE IF EXISTS `hirings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hirings` (
  `id` int NOT NULL AUTO_INCREMENT,
  `companyName` varchar(255) NOT NULL,
  `companyEmail` varchar(100) DEFAULT NULL,
  `companyWebsite` varchar(255) DEFAULT NULL,
  `industry` varchar(100) DEFAULT NULL,
  `openPositions` int DEFAULT '0',
  `hiringStatus` varchar(50) DEFAULT 'No recent activity',
  `createdDate` datetime DEFAULT CURRENT_TIMESTAMP,
  `updatedDate` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `interviewschedule`
--

DROP TABLE IF EXISTS `interviewschedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interviewschedule` (
  `schedule_id` int NOT NULL AUTO_INCREMENT,
  `job_id` int DEFAULT NULL,
  `jobseeker_name` varchar(100) DEFAULT NULL,
  `jobseeker_email` varchar(100) DEFAULT NULL,
  `employer_email` varchar(100) DEFAULT NULL,
  `scheduled_date` date DEFAULT NULL,
  `scheduled_time` time DEFAULT NULL,
  `mode` varchar(50) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'Scheduled',
  `remarks` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`schedule_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jobs`
--

DROP TABLE IF EXISTS `jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jobs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `companyName` varchar(100) NOT NULL,
  `companyEmail` varchar(100) DEFAULT NULL,
  `companyLogo` longblob,
  `logoFileName` varchar(255) DEFAULT NULL,
  `location` varchar(100) NOT NULL,
  `jobType` varchar(100) NOT NULL,
  `salary` varchar(50) NOT NULL,
  `requirement` text NOT NULL,
  `postedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `position` varchar(100) DEFAULT NULL,
  `editUpdate` varchar(100) DEFAULT NULL,
  `employerEmail` varchar(100) DEFAULT NULL,
  `status` enum('Open','Paused','Closed') DEFAULT 'Open',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jobseekers`
--

DROP TABLE IF EXISTS `jobseekers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jobseekers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `JSJVID` varchar(10) DEFAULT NULL,
  `firstname` varchar(100) NOT NULL,
  `lastname` varchar(100) NOT NULL,
  `age` int NOT NULL,
  `gender` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `status` varchar(50) DEFAULT 'active',
  `signup_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `password` (`password`),
  UNIQUE KEY `JSJVID` (`JSJVID`),
  UNIQUE KEY `JSJVID_2` (`JSJVID`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_jobseekers` BEFORE INSERT ON `jobseekers` FOR EACH ROW BEGIN
    DECLARE nextId INT;
    SELECT IFNULL(MAX(id), 0) + 1 INTO nextId FROM jobseekers;
    SET NEW.JSJVID = CONCAT('JSJV', LPAD(nextId, 3, '0'));
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `jsfeedback`
--

DROP TABLE IF EXISTS `jsfeedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jsfeedback` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userEmail` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `rating` int NOT NULL,
  `likes` text,
  `dislikes` text,
  `feedbackDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jsnotifications`
--

DROP TABLE IF EXISTS `jsnotifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jsnotifications` (
  `notificationId` int NOT NULL AUTO_INCREMENT,
  `notifications` text NOT NULL,
  `emailJS` varchar(255) NOT NULL,
  `notificationDate` date DEFAULT (curdate()),
  PRIMARY KEY (`notificationId`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `offerletters`
--

DROP TABLE IF EXISTS `offerletters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `offerletters` (
  `offer_id` int NOT NULL AUTO_INCREMENT,
  `job_id` int DEFAULT NULL,
  `jobseeker_name` varchar(100) DEFAULT NULL,
  `jobseeker_email` varchar(100) DEFAULT NULL,
  `employer_email` varchar(100) DEFAULT NULL,
  `offer_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `joining_date` date DEFAULT NULL,
  `position` varchar(100) DEFAULT NULL,
  `package` varchar(100) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'Offered',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `companyName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`offer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `queries`
--

DROP TABLE IF EXISTS `queries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `queries` (
  `message_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `message` text NOT NULL,
  `submission_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rejectedjobseekers`
--

DROP TABLE IF EXISTS `rejectedjobseekers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rejectedjobseekers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role` varchar(100) NOT NULL,
  `position` varchar(100) NOT NULL,
  `companyName` varchar(100) NOT NULL,
  `jobSeekerEmail` varchar(100) NOT NULL,
  `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reports`
--

DROP TABLE IF EXISTS `reports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reports` (
  `reportID` int NOT NULL AUTO_INCREMENT,
  `jobID` int DEFAULT NULL,
  `reportedBy` varchar(255) NOT NULL,
  `reportedAgainst` varchar(255) NOT NULL,
  `reportedRole` enum('employer','jobseeker') NOT NULL,
  `reason` varchar(255) NOT NULL,
  `description` text,
  `status` varchar(50) DEFAULT 'Pending',
  `reportDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`reportID`),
  KEY `jobID` (`jobID`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `savedjobs`
--

DROP TABLE IF EXISTS `savedjobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `savedjobs` (
  `JobID` int NOT NULL AUTO_INCREMENT,
  `UserEmail` varchar(100) DEFAULT NULL,
  `CompanyName` varchar(100) NOT NULL,
  `Location` varchar(100) DEFAULT NULL,
  `JobType` varchar(50) DEFAULT NULL,
  `SkillsRequired` text,
  `Salary` decimal(10,2) DEFAULT NULL,
  `PostedDate` date DEFAULT NULL,
  `id` int NOT NULL,
  `employerEmail` varchar(255) DEFAULT NULL,
  `savedDate` date DEFAULT NULL,
  `companyLogo` longblob,
  `logoFileName` varchar(255) DEFAULT NULL,
  `status` varchar(100) DEFAULT 'Opened',
  `companyEmail` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`JobID`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shortlistedjobseekers`
--

DROP TABLE IF EXISTS `shortlistedjobseekers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shortlistedjobseekers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role` varchar(100) NOT NULL,
  `position` varchar(100) NOT NULL,
  `companyName` varchar(100) NOT NULL,
  `jobSeekerEmail` varchar(100) NOT NULL,
  `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status` (
  `email` varchar(100) NOT NULL,
  `InterviewsPending` int NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `userprofile`
--

DROP TABLE IF EXISTS `userprofile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userprofile` (
  `JSProfileImage` longblob,
  `JSEmail` varchar(100) NOT NULL,
  `JSFullName` varchar(100) NOT NULL,
  `JSPhone` varchar(15) DEFAULT NULL,
  `JSLocation` varchar(100) DEFAULT NULL,
  `JSHeadline` varchar(150) DEFAULT NULL,
  `JSSummary` text,
  `JSDegree` varchar(100) DEFAULT NULL,
  `JSInstitution` varchar(100) DEFAULT NULL,
  `JSYear` varchar(10) DEFAULT '2025',
  `JSSkills` text,
  PRIMARY KEY (`JSEmail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-29  9:54:47
