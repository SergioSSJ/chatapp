package org.sjimenez.chatapp.controllers;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.sjimenez.chatapp.beans.ApiError;
import org.sjimenez.chatapp.delegate.GroupDelegate;
import org.sjimenez.chatapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The main Purpose of this Controller is handle all actions related with Groups
 * and Users, with the objective to assign, unassign, updte, ect the customers
 * for the groups.
 * 
 * @author developer
 *
 */
@RestController
@RequestMapping(value = "/userToGroup")
@Validated
public class UserToGroupController {

	private static final Logger log = LoggerFactory.getLogger(UserToGroupController.class);

	@Autowired
	private GroupDelegate groupDelegate;

	/**
	 * This method is responsible to fetch the user list of one specific group,
	 * which one is identified by its group name
	 * 
	 * @param groupName
	 * @return
	 */
	@GetMapping("/{groupName}")
	public ResponseEntity<List<User>> fetchUsersByGroupName(
			@PathVariable("groupName") @NotEmpty @NotNull String groupName) {
		return new ResponseEntity<List<User>>(groupDelegate.fetchUsersByGroupName(groupName), HttpStatus.OK);
	}

	/**
	 * This method is responsible to add new user to a group based on its group
	 * name. The users have been sent by a user list on the boy of the request.
	 * 
	 * @param groupName
	 * @return
	 */
	@PostMapping("/{groupName}")
	public ResponseEntity<List<User>> addUserToGroup(
			@PathVariable("groupName") @NotEmpty @NotNull String groupName,
			@RequestParam("userList") @NotEmpty @NotNull List<Integer> userList) {
		if (userList.isEmpty()) {
			log.warn("userList variable is empty");
			final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "userList can not be an empty list",
					"userList variable is empty");
			return new ResponseEntity(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<List<User>>(groupDelegate.addUserToGroup(groupName, userList), HttpStatus.OK);
	}

	/**
	 * Remove a list of user from one specific group. The user list is attached like
	 * a parameter on the request body
	 * 
	 * @param groupName
	 * @return
	 */
	//TODO:  create a regex to send a validation for the input useList that should to follow "1,2,3" 
	@DeleteMapping("/{groupName}")
	public ResponseEntity<List<User>> removeUserFromGroup(
			@PathVariable("groupName") @NotEmpty @NotNull String groupName,
			@RequestParam("userList") @NotEmpty @NotNull List<Integer> userList) {
		if (userList.isEmpty()) {
			log.warn("userList variable is empty");
			final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "userList can not be an empty list",
					"userList variable is empty");
			return new ResponseEntity(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<List<User>>(groupDelegate.removeUserFromGroup(groupName, userList), HttpStatus.OK);
	}
}
