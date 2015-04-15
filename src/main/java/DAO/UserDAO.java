package DAO;

import DAO.logic.UserLogic;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    public void addUser(UserLogic user);   //добавить студента
    public void updateUser(UserLogic user);//обновить студента
    public UserLogic getUserById(int id);    //получить стедента по id
    public UserLogic getUserByUsername(String username);    //получить стедента по username
    public UserLogic getUserByAuth(String username, String password);
    public List getAllUsers();              //получить всех студентов
    public void deleteUser(UserLogic user);//удалить студента
    public int getUserCounter();
    public List<UserLogic> getAllUserRating(int count);
}