package DAO;

import DAO.logic.UserLogic;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    public void addUser(UserLogic user) throws SQLException;   //добавить студента
    public void updateUser(UserLogic user) throws SQLException;//обновить студента
    public UserLogic getUserById(int id) throws SQLException;    //получить стедента по id
    public UserLogic getUserByUsername(String username) throws SQLException;    //получить стедента по username
    public UserLogic getUserByAuth(String username, String password);
    public List getAllUsers() throws SQLException;              //получить всех студентов
    public void deleteUser(UserLogic user) throws SQLException;//удалить студента
    public int getUserCounter() throws SQLException;
    public List<UserLogic> getAllUserRating(int count);
}