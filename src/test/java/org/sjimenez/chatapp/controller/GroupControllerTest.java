package org.sjimenez.chatapp.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.sjimenez.chatapp.controllers.GroupController;
import org.sjimenez.chatapp.dao.GroupDao;
import org.sjimenez.chatapp.delegate.GroupDelegate;
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
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GroupControllerTest {

	@Mock
	private GroupDelegate groupDelegate;

	@InjectMocks
	private GroupController groupController;

	private Group testGroupBean;

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private GroupDao groupDao;

	@LocalServerPort
	private int port;
	private static final Logger logger = LoggerFactory.getLogger(UserDbMapperTest.class);
	
	@Before
	public void init() {
		testGroupBean = createGroupForTest("GroupTesting1");
	}

	@Test
	public void createGroup() {
		when(groupDelegate.createGroup(testGroupBean.getName(),null)).thenReturn(testGroupBean);
		ResponseEntity<Group> responseEntity = groupController.createGroup(testGroupBean.getName(),null);

		verify(groupDelegate).createGroup(testGroupBean.getName(),null);
		assertThat( responseEntity.getStatusCode(), is(HttpStatus.OK) );
		assertThat( responseEntity.getBody().getName(), is(testGroupBean.getName( )) );
		assertThat( responseEntity.getBody().getIdgroup(), is(testGroupBean.getIdgroup()) );
		assertThat( responseEntity.getBody().getCreation(), is(testGroupBean.getCreation()) );
	}

	@Test
	public void fetchGroupByName() {
		when(groupDelegate.fetchGroupByName(testGroupBean.getName())).thenReturn(testGroupBean);
		ResponseEntity<Group> responseEntity = groupController.fetchGroupByName(testGroupBean.getName());

		verify(groupDelegate).fetchGroupByName(testGroupBean.getName());
		assertThat( responseEntity.getStatusCode(), is(HttpStatus.OK) );
		assertThat( responseEntity.getBody().getName(), is(testGroupBean.getName( )) );
		assertThat( responseEntity.getBody().getIdgroup(), is(testGroupBean.getIdgroup()) );
		assertThat( responseEntity.getBody().getCreation(), is(testGroupBean.getCreation()) );
	}

	@Test
	public void updateGroupByName() {
		when(groupDelegate.fetchGroupByName(testGroupBean.getName())).thenReturn(testGroupBean);
		ResponseEntity<Group> responseEntity = groupController.fetchGroupByName(testGroupBean.getName());

		verify(groupDelegate).fetchGroupByName(testGroupBean.getName());
		assertThat( responseEntity.getStatusCode(), is(HttpStatus.OK) );
		assertThat( responseEntity.getBody().getName(), is(testGroupBean.getName( )) );
		assertThat( responseEntity.getBody().getIdgroup(), is(testGroupBean.getIdgroup()) );
		assertThat( responseEntity.getBody().getCreation(), is(testGroupBean.getCreation()) );
	}

	@Test
	public void deleteGroupByName() {
		
		@SuppressWarnings("rawtypes")
		ResponseEntity responseEntity = groupController.deleteGroupByName(testGroupBean.getName() );
		
		verify(groupDelegate, times(1)).deleteGroupByName(testGroupBean.getName());
		assertThat( responseEntity.getStatusCode(), is(HttpStatus.OK) );
	}

	@Test
	public void fetchGroupByName_NotFoundException(){
		when(groupDao.selectGroupByName("name1")).thenReturn(Optional.ofNullable(null));
		//groupDelegate.removeUserFromGroup("name1", new ArrayList<Integer>(){{ add(1);add(2);add(3); }});
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Void> request = new HttpEntity<Void>(headers);
		ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + String.valueOf(port) + "/group/name1", HttpMethod.GET, request, Void.class);
		assertEquals("Status code must be 404", HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void updateGroupByName_NotFoundException(){
		when(groupDao.selectGroupByName("name1")).thenReturn(Optional.ofNullable(null));
		//groupDelegate.removeUserFromGroup("name1", new ArrayList<Integer>(){{ add(1);add(2);add(3); }});
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Void> request = new HttpEntity<Void>(headers);
		ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + String.valueOf(port) + "/group/name1?newGroupName=newName1", HttpMethod.PUT, request, Void.class);
		assertEquals("Status code must be 404", HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void deleteGroupByName_NotFoundException(){
		when(groupDao.selectGroupByName("name1")).thenReturn(Optional.ofNullable(null));
		//groupDelegate.removeUserFromGroup("name1", new ArrayList<Integer>(){{ add(1);add(2);add(3); }});
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Void> request = new HttpEntity<Void>(headers);
		ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + String.valueOf(port) + "/group/name1", HttpMethod.DELETE, request, Void.class);
		assertEquals("Status code must be 404", HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void createGroup_DuplicateKeyException(){
		when(groupDao.selectGroupByName("name1")).thenReturn(Optional.ofNullable(new Group()));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Void> request = new HttpEntity<Void>(headers);
		ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + String.valueOf(port) + "/group/name1", HttpMethod.POST, request, Void.class);
		assertEquals("Status code must be 404", HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	private Group createGroupForTest(String groupName) {
		Group group = new Group();
		group.setName(groupName);
		group.setCreation(LocalDate.now());
		group.setIdgroup(1);

		return group;
	}
}
