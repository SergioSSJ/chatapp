package org.sjimenez.chatapp.dao;

import org.sjimenez.chatapp.mappers.GroupMapper;
import org.sjimenez.chatapp.model.Group;
import org.sjimenez.chatapp.model.Message;
import org.sjimenez.chatapp.model.User;
import org.sjimenez.chatapp.model.UserGroupRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GroupDao {

    @Autowired
    GroupMapper groupMapper;

    //insert
    public int insertGroup(Group group) {
        return groupMapper.insertGroup(group);
    }

    public void insertUserGroupRelation(int idgroup, int iduser) {

        groupMapper.insertUserGroupRelation(idgroup, iduser);
    }

    //selects
    public Optional<Group> selectGroupById(int idgroup) {
        return Optional.ofNullable(groupMapper.selectGroupById(idgroup));
    }

    public Optional<Group> selectGroupByName(String name) {
        return Optional.ofNullable(groupMapper.selectGroupByName(name));
    }

    public List<UserGroupRelation> selectAllUserGroup(int idgroup) {
        return groupMapper.selectAllUserGroup();
    }

    public List<Group> selectAllGroups() {
        return groupMapper.selectAll();
    }

    public Optional<UserGroupRelation> selectUserGroupRelation(int idgroup, int iduser) {
        return Optional.ofNullable(groupMapper.selectUserGroupRelation(idgroup, iduser));
    }

    public List<Message> selectMessagesFromGroup(int idgroup) {
        return groupMapper.selectMessagesFromGroup(idgroup);
    }

    public List<User> selectUsersById(int idgroup) {
        return groupMapper.selectUsersById(idgroup);
    }


    //delete
    public void deleteGroupById(int idgroup) {
        groupMapper.deleteGroupById(idgroup);
    }

    public void deleteUserFromGroup(int idgroup, int iduser) {
        groupMapper.deleteUserFromGroup(idgroup, iduser);
    }

    //update
    public void updateGroup(String newname, String oldname) {
        groupMapper.updateGroup(newname, oldname);
    }

}
