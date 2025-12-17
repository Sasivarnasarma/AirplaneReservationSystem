package com.fms.dao;

import com.fms.model.User;
import com.fms.exception.FMSException;
import java.util.List;

public interface IUserDAO {
    boolean registerUser(User user) throws FMSException;

    User loginUser(String username, String password) throws FMSException;

    List<User> getAllUsers() throws FMSException;

    boolean updateUserRole(int userId, String newRole) throws FMSException;

    User getUserByUsername(String username) throws FMSException;

    boolean updatePassword(String username, String newPassword) throws FMSException;

    boolean updateUser(User user) throws FMSException;

    User getUserByEmail(String email) throws FMSException;
}
