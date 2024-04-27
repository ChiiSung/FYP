-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主机： 127.0.0.1
-- 生成日期： 2024-04-27 20:32:32
-- 服务器版本： 10.4.28-MariaDB
-- PHP 版本： 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `calendar_system`
--

-- --------------------------------------------------------

--
-- 表的结构 `assignment`
--

CREATE TABLE `assignment` (
  `assignmentID` int(255) NOT NULL,
  `userID` int(255) NOT NULL,
  `courseID` int(255) NOT NULL,
  `taskTitle` varchar(2000) NOT NULL,
  `dueDate` datetime NOT NULL,
  `description` varchar(10000) NOT NULL,
  `completed` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 表的结构 `course`
--

CREATE TABLE `course` (
  `courseID` int(255) NOT NULL,
  `courseName` varchar(255) NOT NULL,
  `courseSection` int(3) NOT NULL,
  `courseType` enum('K','T') NOT NULL,
  `time` time NOT NULL,
  `day` enum('Sunday','Monday','Tuesday','Wednesday','Thursday') NOT NULL,
  `durationTime` enum('2','3') NOT NULL,
  `code` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 表的结构 `repeattask`
--

CREATE TABLE `repeattask` (
  `repeatID` int(255) NOT NULL,
  `userID` int(255) NOT NULL,
  `taskTitle` varchar(2000) NOT NULL,
  `dueDate` datetime NOT NULL,
  `description` varchar(10000) NOT NULL,
  `completed` tinyint(1) NOT NULL,
  `repeatType` enum('Daily','Weekly','Monthly') NOT NULL,
  `frequancyRepeat` int(255) NOT NULL,
  `endDate` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `repeattask`
--

INSERT INTO `repeattask` (`repeatID`, `userID`, `taskTitle`, `dueDate`, `description`, `completed`, `repeatType`, `frequancyRepeat`, `endDate`) VALUES
(13, 1, 'asdf', '2024-04-19 00:00:00', 'asdf', 0, 'Daily', 2, '2024-04-21 00:00:00'),
(14, 1, 'asdf', '2024-04-25 00:00:00', 'asdf', 0, 'Weekly', 2, '2024-05-09 00:00:00'),
(15, 1, 'asd', '2024-04-04 00:00:00', '', 0, 'Weekly', 2, '2024-04-18 00:00:00'),
(16, 1, 'asd', '2024-04-16 00:00:00', 'asd', 1, 'Daily', 2, '2024-04-18 00:00:00'),
(21, 1, 'asdfadsfa', '2024-03-10 00:00:00', '', 0, 'Daily', 1, '2024-03-11 00:00:00'),
(22, 1, 'ads', '2024-04-08 00:00:00', 'asd', 0, 'Monthly', 2, '2024-06-08 00:00:00');

-- --------------------------------------------------------

--
-- 表的结构 `task`
--

CREATE TABLE `task` (
  `taskID` int(11) NOT NULL,
  `userID` int(11) NOT NULL,
  `taskTitle` varchar(2000) NOT NULL,
  `dueDate` datetime NOT NULL,
  `description` varchar(10000) NOT NULL,
  `completed` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `task`
--

INSERT INTO `task` (`taskID`, `userID`, `taskTitle`, `dueDate`, `description`, `completed`) VALUES
(2, 1, 'gg', '2024-03-06 00:00:00', 'hailat\n', 0),
(13, 1, '1234', '2024-03-06 00:00:00', '1234', 0),
(20, 1, 'asd', '2024-04-07 00:00:00', 'asd', 0),
(27, 1, 'june 的 时间', '2024-06-04 00:00:00', '为什么世界这么大\n', 0),
(28, 1, 'testing', '2024-03-06 00:00:00', '', 0),
(29, 1, 'hello world', '2024-03-06 00:00:00', '', 0),
(30, 1, 'asdfasdfa', '2024-03-06 00:00:00', '', 0);

-- --------------------------------------------------------

--
-- 表的结构 `timetable`
--

CREATE TABLE `timetable` (
  `timetableID` int(255) NOT NULL,
  `userID` int(255) NOT NULL,
  `semStart` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 表的结构 `timetablecourse`
--

CREATE TABLE `timetablecourse` (
  `timetableID` int(255) NOT NULL,
  `courseID` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE `user` (
  `userID` int(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `userType` enum('student','lecturer','admin') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`userID`, `username`, `email`, `password`, `userType`) VALUES
(1, 'LCS', 'lingchiisung@gmail.com', '1234', 'student'),
(2, 'ZX', 'zxkhoo141132@gmail.com', '123', 'student');

-- --------------------------------------------------------

--
-- 表的结构 `userassignmentcompleted`
--

CREATE TABLE `userassignmentcompleted` (
  `userID` int(11) NOT NULL,
  `assignmentID` int(11) NOT NULL,
  `completed` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转储表的索引
--

--
-- 表的索引 `assignment`
--
ALTER TABLE `assignment`
  ADD PRIMARY KEY (`assignmentID`);

--
-- 表的索引 `course`
--
ALTER TABLE `course`
  ADD PRIMARY KEY (`courseID`);

--
-- 表的索引 `repeattask`
--
ALTER TABLE `repeattask`
  ADD PRIMARY KEY (`repeatID`);

--
-- 表的索引 `task`
--
ALTER TABLE `task`
  ADD PRIMARY KEY (`taskID`);

--
-- 表的索引 `timetable`
--
ALTER TABLE `timetable`
  ADD PRIMARY KEY (`timetableID`),
  ADD KEY `userID` (`userID`);

--
-- 表的索引 `timetablecourse`
--
ALTER TABLE `timetablecourse`
  ADD KEY `timetableID` (`timetableID`),
  ADD KEY `courseID` (`courseID`);

--
-- 表的索引 `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userID`);

--
-- 表的索引 `userassignmentcompleted`
--
ALTER TABLE `userassignmentcompleted`
  ADD KEY `userID` (`userID`),
  ADD KEY `assignmentID` (`assignmentID`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `assignment`
--
ALTER TABLE `assignment`
  MODIFY `assignmentID` int(255) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `course`
--
ALTER TABLE `course`
  MODIFY `courseID` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- 使用表AUTO_INCREMENT `repeattask`
--
ALTER TABLE `repeattask`
  MODIFY `repeatID` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- 使用表AUTO_INCREMENT `task`
--
ALTER TABLE `task`
  MODIFY `taskID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- 使用表AUTO_INCREMENT `timetable`
--
ALTER TABLE `timetable`
  MODIFY `timetableID` int(255) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `user`
--
ALTER TABLE `user`
  MODIFY `userID` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- 限制导出的表
--

--
-- 限制表 `timetable`
--
ALTER TABLE `timetable`
  ADD CONSTRAINT `timetable_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`);

--
-- 限制表 `timetablecourse`
--
ALTER TABLE `timetablecourse`
  ADD CONSTRAINT `timetablecourse_ibfk_1` FOREIGN KEY (`timetableID`) REFERENCES `timetable` (`timetableID`);

--
-- 限制表 `userassignmentcompleted`
--
ALTER TABLE `userassignmentcompleted`
  ADD CONSTRAINT `userassignmentcompleted_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`),
  ADD CONSTRAINT `userassignmentcompleted_ibfk_2` FOREIGN KEY (`assignmentID`) REFERENCES `assignment` (`assignmentID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
