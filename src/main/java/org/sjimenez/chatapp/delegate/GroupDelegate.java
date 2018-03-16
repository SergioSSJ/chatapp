package org.sjimenez.chatapp.delegate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.sjimenez.chatapp.dao.ChatDao;
import org.sjimenez.chatapp.dao.GroupDao;
import org.sjimenez.chatapp.model.Group;
import org.sjimenez.chatapp.model.User;
import org.sjimenez.chatapp.model.UserGroupRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import javax.persistence.EntityNotFoundException;

public class GroupDelegate {

    private static final Logger logger = LoggerFactory.getLogger(GroupDelegate.class);

    @Autowired
    GroupDao groupDao;
    @Autowired
    ChatDao chatDao;

    public Group createGroup(String groupName) {
        logger.warn("create group method"+groupName);

        Optional<Group> optionalGroup = groupDao.selectGroupByName(groupName);
        if (optionalGroup.isPresent()) {
            logger.warn("Duplicate group name"+groupName);
            throw new DuplicateKeyException("Duplicate group name");
        }

        Group group = new Group();
        group.setName(groupName);
        group.setCreation(LocalDate.now());
        groupDao.insertGroup(group);
        return group;
    }

    public Group fetchGroupByName(String groupName) {
        Optional<Group> group = groupDao.selectGroupByName(groupName);
        if (!group.isPresent()) {
            logger.warn("Group "+groupName+" not found ");
            throw new EntityNotFoundException();
        }
        return group.get();
    }

    public Group updateGroupByName(String groupName, String newGroupName) {
        Optional<Group> group = groupDao.selectGroupByName(groupName);
        if (!group.isPresent()) {
            logger.warn("Group "+groupName+" not found ");
            throw new EntityNotFoundException();
        }
        groupDao.updateGroup(newGroupName, groupName);
        return group.get();
    }

    public void deleteGroupByName(String groupName) {
        Optional<Group> group = groupDao.selectGroupByName(groupName);
        if (!group.isPresent()) {
            logger.warn("Group "+groupName+" not found ");
            throw new EntityNotFoundException();
        }
        groupDao.deleteGroupById(group.get().getIdgroup());
    }

    public List<User> fetchUsersByGroupName(String groupName) {
        Optional<Group> group = groupDao.selectGroupByName(groupName);
        if (!group.isPresent()) throw new EntityNotFoundException();
        return groupDao.selectUsersById(group.get().getIdgroup());
    }

    public List<User> addUserToGroup(String groupName, List<Integer> userIdList) {
        Optional<Group> group = groupDao.selectGroupByName(groupName);
        if (!group.isPresent()) {
            logger.warn("Group "+groupName+" not found ");
            throw new EntityNotFoundException();
        }
        int idgroup = group.get().getIdgroup();
        userIdList.forEach((iduser) -> {
            Optional<UserGroupRelation> userGroupRelation = groupDao.selectUserGroupRelation(idgroup, iduser);
            if (!userGroupRelation.isPresent())
                groupDao.insertUserGroupRelation(idgroup, iduser);
            else {
                logger.warn("User with id " + iduser + " already exist in group " + idgroup);
            }
        });
        return groupDao.selectUsersById(idgroup);
    }

    public List<User> removeUserFromGroup(String groupName, List<Integer> userIdList) {
        Optional<Group> group = groupDao.selectGroupByName(groupName);
        if (!group.isPresent()) {
            logger.warn("Group "+groupName+" not found ");
            throw new EntityNotFoundException();
        }
        int idgroup = group.get().getIdgroup();
        userIdList.forEach((iduser) -> {
            Optional<User> user = Optional.ofNullable(chatDao.selectUserById(iduser));
            if (!user.isPresent())
                logger.warn("User with id " + iduser + " was not inserted in group because doesnt exist");
            groupDao.insertUserGroupRelation(idgroup, iduser);
        });
        return groupDao.selectUsersById(idgroup);
    }
}
