package service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import caro.player.Player;
import javafx.application.Platform;
import service.*;

public class PlayerService {

    public int createPlayer(Player user) {
    	Connection connection = DB.getConnection();
        String query = "INSERT INTO user (email, password, username, numberOfGame, numberOfWin, " +
                "numberOfDraw, totalScore) VALUES (?, ?, ?, 0, 0, 0, 0)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.executeUpdate();

            // Lấy ID được sinh tự động
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }
            return 0; // thành công
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            if (e.getMessage().contains("email")) {
				return 1;
			}
            else {
				return 2;
			}
            
        }
    }

    public Player getPlayerById(int userId) {
    	Connection connection = DB.getConnection();
        String query = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateMatch(Player winner, Player losser) {
    	Connection connection = DB.getConnection();

        String query = "UPDATE user SET totalScore = ?, numberOfGame = ?, numberOfDraw = ?, numberOfWin = ? WHERE email = ? AND username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, winner.getTotalScore());
            preparedStatement.setInt(2, winner.getNumberOfGame());
            preparedStatement.setInt(3, winner.getNumberOfDraw());
            preparedStatement.setInt(4, winner.getNumberOfWin());
            preparedStatement.setString(5, winner.getEmail());
            preparedStatement.setString(6, winner.getUsername());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public List<Player> getTopPlayers() {
    	Connection connection = DB.getConnection();
        List<Player> users = new ArrayList<>();
        String query = "SELECT * FROM user "
        		+ "ORDER BY totalScore DESC "
        		+ "LIMIT 100;";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                users.add(extractUserFromResultSet(resultSet));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    private Player extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        return new Player(
                resultSet.getString("email"),
                resultSet.getString("username"),
                resultSet.getInt("numberOfGame"),
                resultSet.getInt("numberOfWin"),
                resultSet.getInt("numberOfDraw"),
                resultSet.getInt("totalScore")
        );
    }

	public Player getPlayerByLoginInf(String email, String password) {
		// TODO Auto-generated method stub
		Connection connection = DB.getConnection();
		String query = "SELECT * FROM user WHERE email = '"+ email+"' AND password = '"+password+"'";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
//            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
	}
}
