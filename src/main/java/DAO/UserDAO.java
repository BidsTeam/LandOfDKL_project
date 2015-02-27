package DAO;

import java.sql.SQLException;
import java.util.List;

import DAO.logic.User;

public interface UserDAO {
    public void addUser(User user) throws SQLException;   //добавить студента
    public void updateUser(User user) throws SQLException;//обновить студента
    public User getUserById(int id) throws SQLException;    //получить стедента по id
    public User getUserByUsername(String username) throws SQLException;    //получить стедента по id
    public List getAllUsers() throws SQLException;              //получить всех студентов
    public void deleteUser(User user) throws SQLException;//удалить студента
}