package org.sjimenez.chatapp.controllers;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.sjimenez.chatapp.delegate.GroupDelegate;
import org.sjimenez.chatapp.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * This class is focused to create the CRUD actions for Groups that have been
 * consumed on any action related to the groups.
 * 
 * @author developer
 *
 */
@RestController
@RequestMapping(value = "/group")
@Validated
public class GroupController {

	@Autowired
	private GroupDelegate groupDelegate;

	/**
	 * the creation Group method is responsible to create only the group without any
	 * user assigned to it. the group name should to be unique.
	 * 
	 * @param groupName
	 * @return
	 */
	@PostMapping("/{groupName}")
	public ResponseEntity<Group> createGroup(@PathVariable("groupName") @NotEmpty @NotNull String groupName, @RequestParam(value="admin")String admin) {
		System.out.println("Admin name:"+admin);
		return new ResponseEntity<Group>(groupDelegate.createGroup(groupName,admin), HttpStatus.OK);
	}

	/**
	 * This method is responsible to fetch the Group information based on the group
	 * name, and get the users assigned to it.
	 */
	@GetMapping("/{groupName}")
	public ResponseEntity<Group> fetchGroupByName(@PathVariable("groupName") @NotEmpty @NotNull String groupName) {
		return new ResponseEntity<Group>(groupDelegate.fetchGroupByName(groupName), HttpStatus.OK);
	}

	/**
	 * This method is responsible only to change the group name based on the latest
	 * group name.
	 * 
	 * @param groupName
	 * @return
	 */
	@PutMapping("/{groupName}")
	public ResponseEntity<Group> updateGroupByName(@PathVariable("groupName") @NotEmpty @NotNull String groupName,
			@RequestParam("newGroupName") @NotEmpty @NotNull  String newGroupName) {
		return new ResponseEntity<Group>(groupDelegate.updateGroupByName(groupName, newGroupName), HttpStatus.OK);
	}
	/**
	 * Delete the group identified with the group name.
	 */
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{groupName}")
	public ResponseEntity deleteGroupByName(@PathVariable("groupName") @NotEmpty @NotNull String groupName) {
		groupDelegate.deleteGroupByName(groupName);
		return new ResponseEntity(HttpStatus.OK);
	}
}
