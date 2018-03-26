package org.sjimenez.chatapp.delegate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sjimenez.chatapp.dao.ChatDao;
import org.sjimenez.chatapp.dao.GroupDao;
import org.sjimenez.chatapp.mappers.UserMapper;
import org.sjimenez.chatapp.model.Group;
import org.sjimenez.chatapp.model.User;
import org.sjimenez.chatapp.test.UserDbMapperTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupDelegateTest {
    @Autowired
    private GroupDelegate groupDelegate;

    @MockBean
    private GroupDao groupDao;

    private static final Logger logger = LoggerFactory.getLogger(UserDbMapperTest.class);

    @Before
    public void initUnitTest() {
    }

    @Test(expected = DuplicateKeyException.class)
    public void createGroupTest() {
        when(groupDao.selectGroupByName("name1")).thenReturn(Optional.ofNullable(new Group()));
        groupDelegate.createGroup("name1",null);

    }

    @Test(expected = EntityNotFoundException.class)
    public void fetchGroupByNameTest() {
        when(groupDao.selectGroupByName("name1")).thenReturn(Optional.ofNullable(null));
        groupDelegate.fetchGroupByName("name1");
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateGroupByNameTest() {
        when(groupDao.selectGroupByName("name1")).thenReturn(Optional.ofNullable(null));
        groupDelegate.fetchGroupByName("name1");
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteGroupByNameTest() {
        when(groupDao.selectGroupByName("name1")).thenReturn(Optional.ofNullable(null));
        groupDelegate.deleteGroupByName("name1");
    }

    @Test(expected = EntityNotFoundException.class)
    public void fetchUsersByGroupNameTest() {
        when(groupDao.selectGroupByName("name1")).thenReturn(Optional.ofNullable(null));
        groupDelegate.fetchUsersByGroupName("name1");
    }

    @Test(expected = EntityNotFoundException.class)
    public void addUsersToGroupTest() {
        when(groupDao.selectGroupByName("name1")).thenReturn(Optional.ofNullable(null));
        groupDelegate.addUserToGroup("name1", new ArrayList<Integer>());
    }

    @Test(expected = EntityNotFoundException.class)
    public void removeUserFromGroupTest() {
        when(groupDao.selectGroupByName("name1")).thenReturn(Optional.ofNullable(null));
        groupDelegate.removeUserFromGroup("name1", new ArrayList<Integer>());
    }

}
