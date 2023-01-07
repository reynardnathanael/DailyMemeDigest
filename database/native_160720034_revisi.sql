-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 07, 2023 at 06:54 PM
-- Server version: 10.4.20-MariaDB
-- PHP Version: 7.3.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `native_160720034`
--

-- --------------------------------------------------------

--
-- Table structure for table `likes`
--

CREATE TABLE `likes` (
  `meme_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `likes`
--

INSERT INTO `likes` (`meme_id`, `user_id`) VALUES
(1, 1),
(1, 3),
(1, 4),
(2, 1),
(2, 4),
(3, 5);

-- --------------------------------------------------------

--
-- Table structure for table `memes`
--

CREATE TABLE `memes` (
  `meme_id` int(11) NOT NULL,
  `image_url` varchar(150) NOT NULL,
  `top_text` varchar(50) NOT NULL,
  `bottom_text` varchar(50) NOT NULL,
  `num_likes` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `num_views` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `memes`
--

INSERT INTO `memes` (`meme_id`, `image_url`, `top_text`, `bottom_text`, `num_likes`, `user_id`, `num_views`) VALUES
(1, 'https://www.meme-arsenal.com/memes/d57849838dbdab4d17fb394e98f736f7.jpg', 'THIS IS ME', 'AFTER YAWNING', 3, 5, 0),
(2, 'https://i.imgflip.com/4/qiev6.jpg', 'IF A FLY CANT FLY ANYMORE', 'DOES IT BECOME A WALK?', 2, 5, 2),
(3, 'https://www.meme-arsenal.com/memes/072e3bda503faa894d1688ac48554fe1.jpg', 'I AM NOT MAD', 'JUST VERY VERY ANGRY', 1, 4, 1);

-- --------------------------------------------------------

--
-- Table structure for table `meme_comments`
--

CREATE TABLE `meme_comments` (
  `comment_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `meme_id` int(11) NOT NULL,
  `content` longtext NOT NULL,
  `comment_date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `meme_comments`
--

INSERT INTO `meme_comments` (`comment_id`, `user_id`, `meme_id`, `content`, `comment_date`) VALUES
(1, 1, 1, 'very funny!', '2023-01-06 15:34:34');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) DEFAULT NULL,
  `password` varchar(50) NOT NULL,
  `registration_date` datetime NOT NULL,
  `avatar_img` varchar(100) NOT NULL,
  `privacy_setting` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `firstname`, `lastname`, `password`, `registration_date`, `avatar_img`, `privacy_setting`) VALUES
(1, 'jordyn123', 'Jordyn', 'Dokidis', 'password', '2022-12-19 20:29:00', 'https://ubaya.fun/native/160720034/memes_api/img_profile/blankprofile.jpg', 0),
(3, 'rey123', 'Rey', NULL, 'password', '2022-12-19 23:42:40', 'https://ubaya.fun/native/160720034/memes_api/img_profile/blankprofile.jpg', 0),
(4, 'adison123', 'Adison', 'Calzoni', 'password', '2022-12-22 00:27:37', 'https://ubaya.fun/native/160720034/memes_api/img_profile/blankprofile.jpg', 0),
(5, 'makenna123', 'Makenna', 'Vetrovs', 'password', '2022-12-29 12:13:00', 'https://ubaya.fun/native/160720034/memes_api/img_profile/blankprofile.jpg', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `likes`
--
ALTER TABLE `likes`
  ADD PRIMARY KEY (`meme_id`,`user_id`),
  ADD KEY `fk_memes_users_users1_idx` (`user_id`),
  ADD KEY `fk_memes_users_memes1_idx` (`meme_id`);

--
-- Indexes for table `memes`
--
ALTER TABLE `memes`
  ADD PRIMARY KEY (`meme_id`),
  ADD KEY `fk_memes_users_idx` (`user_id`);

--
-- Indexes for table `meme_comments`
--
ALTER TABLE `meme_comments`
  ADD PRIMARY KEY (`comment_id`,`user_id`,`meme_id`),
  ADD KEY `fk_users_memes_memes1_idx` (`meme_id`),
  ADD KEY `fk_users_memes_users1_idx` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `memes`
--
ALTER TABLE `memes`
  MODIFY `meme_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `meme_comments`
--
ALTER TABLE `meme_comments`
  MODIFY `comment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `likes`
--
ALTER TABLE `likes`
  ADD CONSTRAINT `fk_memes_users_memes1` FOREIGN KEY (`meme_id`) REFERENCES `memes` (`meme_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_memes_users_users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `memes`
--
ALTER TABLE `memes`
  ADD CONSTRAINT `fk_memes_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `meme_comments`
--
ALTER TABLE `meme_comments`
  ADD CONSTRAINT `fk_users_memes_memes1` FOREIGN KEY (`meme_id`) REFERENCES `memes` (`meme_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_users_memes_users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
