package org.sjimenez.chatapp.controllers;

import org.sjimenez.chatapp.dao.ChatDao;
import org.sjimenez.chatapp.mappers.UserMapper;
import org.sjimenez.chatapp.model.Group;
import org.sjimenez.chatapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/user/resources")
public class UserResourceController {
    private static final Logger logger = LoggerFactory.getLogger(UserResourceController.class);
    @Autowired
    private PasswordEncoder PASSWORD_ENCODER;

    @Autowired
    private ChatDao chatDao;

    private UserMapper userMapper;

    private Optional<User> userOptional;

    @GetMapping("/all")
    public List<User> getAll() {
        return chatDao.findAll();
    }



    @GetMapping("/selectgroups/{mail:.+}")//this {mail:.+} is for dont truncate the domain name
    public ResponseEntity<List<Group>> getGroups(@PathVariable("mail") String mail) {
        logger.error("mail"+mail);
        User user1=chatDao.selectUserByMail(mail);
        System.out.println("obtained"+user1);
        Optional<User> user=Optional.ofNullable(chatDao.selectUserByMail(mail));
        if(!user.isPresent()){
            logger.error("Duplicated keys");
            return new ResponseEntity<List<Group>>(HttpStatus.NO_CONTENT);
        }
        int iduser=user.get().getIduser();
        return new ResponseEntity<List<Group>>(chatDao.selectGroupsFromUserById(iduser),HttpStatus.OK   );
    }

    @PostMapping("/login")
    public ResponseEntity<User> login() {
        return new ResponseEntity<User>(HttpStatus.OK);
    }

    @PostMapping("/insert")
    public ResponseEntity<User> insert(@Valid @RequestBody User user, BindingResult bindingResult) {
        logger.info("Insert service");
        if (bindingResult.hasErrors()) {
            logger.warn("Ocurred an error while validating request data");
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
        userOptional = Optional.ofNullable(chatDao.selectUserByMail(user.getMail()));

        if (userOptional.isPresent()) {
            logger.error("Duplicated keys");
            return new ResponseEntity<User>(HttpStatus.CONFLICT);
        }

        try {
            user.setNickname(PASSWORD_ENCODER.encode(user.getNickname()));
            chatDao.insertUser(user);
        } catch (org.springframework.dao.DataAccessException ex) {
            logger.error("Error when inserting in database" + ex);
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("/getUserById/{iduser}")
    public ResponseEntity<User> getUserById(@PathVariable("iduser") Integer iduser) {
        userOptional = Optional.ofNullable(chatDao.selectUserById(iduser));
        if (!userOptional.isPresent()) {
            logger.info("user with id to retrieve not found");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(userOptional.get(), HttpStatus.OK);
    }
    @GetMapping("/getUserByMail/{mail:.+}")
    public ResponseEntity<User> getUserById(@PathVariable("mail") String mail) {
        userOptional = Optional.ofNullable(chatDao.selectUserByMail(mail));
        if (!userOptional.isPresent()) {
            logger.info("user with mail "+mail+" to retrieve not found");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(userOptional.get(), HttpStatus.OK);
    }

    @PutMapping("/updateUser")
    private ResponseEntity<User> updateUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.warn("ocurred an error while validating request data");
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
        userOptional = Optional.ofNullable(chatDao.selectUserById(user.getIduser()));
        if (!userOptional.isPresent()) {
            logger.info("user to update not found");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        chatDao.updateUser(user);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{iduser}")
    private ResponseEntity<Void> delete(@PathVariable("iduser") Integer iduser) {
        userOptional = Optional.ofNullable(chatDao.selectUserById(iduser));
        if (!userOptional.isPresent()) {
            logger.info("user to delete not found");
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        chatDao.deleteUserById(iduser);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
