package service.DataBase;


import DAO.logic.UserLogic;
import messageSystem.MessageSystem;

import java.util.List;

public interface DBUserService {
    public boolean addUser(UserLogic user);   //добавить студента

    public void updateUser(UserLogic user);//обновить студента

    public UserLogic getUserById(int id);    //получить стедента по id

    public UserLogic getUserByUsername(String username);    //получить стедента по username

    public UserLogic getUserByAuth(String username, String password, MessageSystem messageSystem);

    public List getAllUsers();              //получить всех студентов

    public void deleteUser(UserLogic user);//удалить студента

    public int getUserCounter();

    public List<UserLogic> getAllUserRating(int count);

    public boolean isDeckFull(int userID);
}
