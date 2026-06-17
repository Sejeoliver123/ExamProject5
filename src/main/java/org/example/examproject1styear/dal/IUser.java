package org.example.examproject1styear.dal;

import java.util.List;

public interface IUser {

    List<User> getAllUser() throws Exception;

    User createUser(User user) throws Exception;

    void updateUser(User user) throws Exception;

    void deleteUser(User user) throws Exception;
}
