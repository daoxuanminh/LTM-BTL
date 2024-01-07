package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

//import utils.Utils;

public class DB {

//	private static Logger LOGGER = Utils.getLogger(Connection.class.getName());
	private static Connection connect;

	public static Connection getConnection() {
		if (connect != null)
			return connect;
		try {
			String username = "root";
			String password = "";
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/caro";
			connect = DriverManager.getConnection(url, username, password);
//			LOGGER.info("Connect database successfully");
			System.out.println("Thanh cong");
		} catch (Exception e) {
//			LOGGER.info(e.getMessage());
			System.out.println(e);
			e.printStackTrace();
		}
		return connect;
	}

	public static void main(String[] args) {
		DB.getConnection();
	}
}


//CREATE DATABASE IF NOT EXISTS `caro`;
//USE `caro`;
//
//drop table `gamematch`;
//drop table `user`;
//
//-- UPDATE user SET totalScore = ?, numberOfGame = ?, numberOfDraw = ?, numberOfWin = ?, WHERE email = ? AND username = ?
//CREATE TABLE IF NOT EXISTS `user`(
//    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
//    `email` varchar(255) NOT NULL UNIQUE,
//    `password` varchar(255) NOT NULL,
//    `username` varchar(255) NOT NULL UNIQUE,
//    `avatar` varchar(255),
//    `numberOfGame` int DEFAULT 0,
//    `numberOfWin` int DEFAULT 0,
//    `numberOfDraw` int DEFAULT 0,
//    `isOnline` int DEFAULT 0,
//    `isPlaying` int DEFAULT 0, 
//    `totalScore` int NOT NULL DEFAULT 0 
//);
//-- lịch sử thông tin trận đấu 
//CREATE TABLE IF NOT EXISTS `gameMatch` (
//  `idMatch` int(11) NOT NULL AUTO_INCREMENT,
//  `playerId1` int(11) NOT NULL,
//  `playerId2` int(11) NOT NULL,
//  `winnerId` int(11) DEFAULT NULL,
//  `playTime` int(11) NOT NULL,
//  `totalMove` int(11) NOT NULL,
//  `startedTime` varchar(50) NOT NULL,
//  FOREIGN KEY (`playerId1`) REFERENCES `user`(`id`),
//  FOREIGN KEY (`playerId2`) REFERENCES `user`(`id`), 
//  FOREIGN KEY (`winnerId`) REFERENCES `user`(`id`), 
//  PRIMARY KEY (`idMatch`)
//); 




