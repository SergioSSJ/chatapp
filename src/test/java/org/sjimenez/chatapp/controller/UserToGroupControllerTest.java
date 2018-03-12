package org.sjimenez.chatapp.controller;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.sjimenez.chatapp.controllers.UserToGroupController;
import org.sjimenez.chatapp.delegate.GroupDelegate;
import org.sjimenez.chatapp.mappers.UserMapper;
import org.sjimenez.chatapp.model.Group;
import org.sjimenez.chatapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserToGroupControllerTest {
	@Mock
	private GroupDelegate groupDelegate;

	@InjectMocks
	private UserToGroupController userToGroupController;
	
	private Group testGroupBean;
	
	private List<User> userList;
	
	private User user;
	
	@Before
	public void init() {
		testGroupBean = createGroupForTest("GroupTesting1");
		
		userList = new ArrayList<User>();
		
		userList.add(createUserForTest());
		userList.add(createUserForTest());
		userList.add(createUserForTest());		
	}
	
	@Test
	public void fetchUsersByGroupName()
	{
		when(groupDelegate.fetchUsersByGroupName(testGroupBean.getName())).thenReturn(userList);
		ResponseEntity<List<User>> responseEntity =  userToGroupController.fetchUsersByGroupName(testGroupBean.getName());
		
		verify(groupDelegate, times(1)).fetchUsersByGroupName(testGroupBean.getName());
		assertArrayEquals(userList.toArray(), responseEntity.getBody().toArray());
	}
	
	@Test
	public void addUserToGroup()
	{
		List<Integer> newUsers = new ArrayList<Integer>();
		user = createUserForTest();
		newUsers.add(user.getIduser() );
		userList.add(user);
		
		when(groupDelegate.addUserToGroup(testGroupBean.getName(), newUsers)).thenReturn(userList);
		ResponseEntity<List<User>> responseEntity =  userToGroupController.addUserToGroup(testGroupBean.getName(), newUsers);
		
		verify(groupDelegate).addUserToGroup(testGroupBean.getName(), newUsers);
		assertArrayEquals(userList.toArray(), responseEntity.getBody().toArray());
	}
	
	@Test
	public void removerUserFromGroup()
	{
		List<Integer> removeUsers = new ArrayList<Integer>();
		user = userList.get(userList.size()-1 );
		removeUsers.add(user.getIduser() );
		userList.remove(user);
		
		when(groupDelegate.removeUserFromGroup(testGroupBean.getName(), removeUsers)).thenReturn(userList);
		ResponseEntity<List<User>> responseEntity =  userToGroupController.removeUserFromGroup(testGroupBean.getName(), removeUsers);
		
		verify(groupDelegate).removeUserFromGroup(testGroupBean.getName(), removeUsers);
		assertArrayEquals(userList.toArray(), responseEntity.getBody().toArray());
	}
	
	
	private Group createGroupForTest(String groupName) {
		Group group = new Group();
		group.setName(groupName);
		group.setCreation(LocalDate.now());
		group.setIdgroup(1);

		return group;
	}
	
	private User createUserForTest()
	{
		LocalTime localtime = LocalTime.now();
		int randomNum = ThreadLocalRandom.current().nextInt(1, 15000 + 1);
			
		User user = new User();
		user.setBirthdate(LocalDate.now());
		user.setIduser(randomNum);
		user.setLastName("lastName");
		user.setMail(localtime.toString() + "@hotmail.com");
		user.setName("name");
		user.setNickname(localtime.toString());
		
		return user;
	}
}