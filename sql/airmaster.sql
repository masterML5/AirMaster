-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 11, 2022 at 10:59 PM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `airmaster`
--

-- --------------------------------------------------------

--
-- Table structure for table `scores`
--

CREATE TABLE `scores` (
  `id` int(11) NOT NULL,
  `player` varchar(50) NOT NULL,
  `score` int(11) NOT NULL,
  `datum` date NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `scores`
--

INSERT INTO `scores` (`id`, `player`, `score`, `datum`) VALUES
(1, 'miki', 111, '2022-12-11'),
(2, 'asd', 22222, '2022-12-11'),
(3, 'asd', 1, '2022-12-11'),
(4, 'asd', 1, '2022-12-11'),
(5, 'asd', 1, '2022-12-11'),
(6, 'asd', 1, '2022-12-11'),
(7, 'asd', 1, '2022-12-11'),
(8, 'asd', 1, '2022-12-11'),
(9, 'asd', 1, '2022-12-11'),
(10, 'asd', 1, '2022-12-11'),
(11, 'asd', 1, '2022-12-11'),
(12, 'asd', 1, '2022-12-11'),
(13, 'asd', 0, '2022-12-11'),
(14, 'asd', 0, '2022-12-11'),
(15, 'asd', 0, '2022-12-11');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `scores`
--
ALTER TABLE `scores`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `scores`
--
ALTER TABLE `scores`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
