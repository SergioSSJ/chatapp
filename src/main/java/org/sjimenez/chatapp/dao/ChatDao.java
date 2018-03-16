package org.sjimenez.chatapp.dao;



import org.sjimenez.chatapp.mappers.UserMapper;
import org.sjimenez.chatapp.model.Group;
import org.sjimenez.chatapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class ChatDao {

    @Autowired
    UserMapper userMapper;


    public List<User> findAll() {
        return userMapper.findAll();
    }

    public int insertUser(User user) throws org.springframework.dao.DataAccessException {
        return userMapper.insert(user);
    }

    public int deleteUserById(int id) throws org.springframework.dao.DataAccessException {
        return userMapper.deleteUserById(id);
    }

    public int updateUser(User user) throws org.springframework.dao.DataAccessException {
        return userMapper.updateUser(user);
    }

    public User selectUserLastRecord() throws org.springframework.dao.DataAccessException {
        return userMapper.selectUserLastRecord();
    }

    public User selectUserById(int id) throws org.springframework.dao.DataAccessException {
        return userMapper.selectUserById(id);
    }

    public User selectUserByMail(String mail) throws org.springframework.dao.DataAccessException {
        return userMapper.selectUserByMail(mail);
    }

    public List<Group> selectGroupsFromUserById(int iduser) throws org.springframework.dao.DataAccessException {
        return userMapper.selectGroupsFromUserById(iduser);
    }

}
