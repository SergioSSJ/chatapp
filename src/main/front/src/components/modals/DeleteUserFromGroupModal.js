"use strict";

import React, { isValidElement } from "react";
import { connect } from "react-redux";
import { FormGroup, ControlLabel, FormControl, Modal, Button, InputGroup } from "react-bootstrap";
import { isValidUserName, isValidEmail } from "../../util/AppUtils";
import { showCreateGroup, closeCreateGroup, closeDeleteUserFromGroup, showDeleteUserFromGroup } from "../../actions/actions";

let AddUserToGroupModal = (function() {
  let NewGroupTextForm = (function() {
    return class NewGroupTextForm extends React.Component {
      constructor(props) {
        super(props);
        this.state = { groupname: "" };
        this.handlegroupnameChange = this.handlegroupnameChange.bind(this);
      }
      getLogingroupnameValidationState() {
        if (isValidUserName(this.state.groupname)) return "success";
        else return "error";
      }
      handlegroupnameChange(e) {
        this.setState({
          groupname: e.target.value
        });
      }
      render() {
        return (
          <div>
            <FormGroup validationState={this.getLogingroupnameValidationState()}>
              <ControlLabel>User to insert mail</ControlLabel>
              <InputGroup>
                <InputGroup.Addon>@</InputGroup.Addon>
                <FormControl
                  name="name"
                  type="text"
                  value={this.state.groupname}
                  placeholder="Enter user emmail"
                  onChange={this.handlegroupnameChange}
                />
                <FormControl.Feedback />
              </InputGroup>
            </FormGroup>
          </div>
        );
      }
    };
  })();

  return class AppLogInModal extends React.Component {
    constructor(props) {
      super(props);
      this.onClickAddUserToGroupModal = this.onClickAddUserToGroupModal.bind(this);
      this.onClickCloseDeleteUserFromGroupGroupModal = this.onClickCloseDeleteUserFromGroupGroupModal.bind(this);
      this.addUserToGroup = this.addUserToGroup.bind(this);
    }
    validateFieldsToSave(data) {
      if (isValidUserName(data.name)) return true;
      return false;
    }
    retrieveDataToLogInUser() {
      var groupname = this.refs.NewGroupTextForm.state.groupname;
      return { name: groupname };
    }
    onClickAddUserToGroupModal() {
      var data = this.retrieveDataToLogInUser();
      if (!this.validateFieldsToSave(data)) {
        window.alert("Complete los campos de manera correcta");
        return;
      }
      var url = `http://localhost:8080/user/resources/getUserByMail/${data.name}`;
      fetch(url, {
        method: "GET",
        credentials: "include",
        headers: new Headers({
          Accept: "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"
        })
      })
        .then(response => {
          if (!response.ok) {
            this.addUserToGroupErrorManager(response.status);
            throw Error(response.statusText);
          }
          return response;
        })
        .then(res => res.json())
        .then(response => {
          this.addUserToGroup(response.iduser);
        })
        .catch(error => {
          console.error("Error:", error);
        });
    }

    addUserToGroup(iduser) {
      var url = `http://localhost:8080/userToGroup/${this.props.currentChat}?userList=${iduser}`;
      var data=this.retrieveDataToLogInUser();
      fetch(url, {
        method: "DELETE",
        credentials: "include",
        headers: new Headers({
          Accept: "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"
        })
      })
        .then(response => {
          if (!response.ok) {
            this.addUserToGroupErrorManager(response.status);
            throw Error(response.statusText);
          }
          return response;
        })
        .then(res => res.text())
        .then(response => {
          console.log(`user with id ${iduser} was deleted group ${this.props.currentChat}`);
          
          this.props.stompClient.send(`/app/send/delete/${this.props.currentChat}`, {}, data.name);   
        })
        .catch(error => {
          console.error("Error:", error);
        });
      this.props.closeDeleteUserFromGroup();
    }

    addUserToGroupErrorManager(errorCode) {
      switch (errorCode) {
        case 400: {
          window.alert("Bad request please check that you are sending the field in the right way");
          break;
        }
        case 401: {
          window.alert("Not authorized");
          break;
        }
        case 404: {
          window.alert("The user that you are trying to add does not exits");
          break;
        }
        case 409: {
          window.alert("You send and already existing email");
          break;
        }
        case 500: {
          window.alert("Server error please try again or send us a message");
          break;
        }
        default: {
          window.alert("Unespected error occured");
        }
      }
    }

    onClickCloseDeleteUserFromGroupGroupModal() {
      this.props.closeDeleteUserFromGroup();
    }

    render() {
      return (
        <Modal show={this.props.showDeleteUserFromGroupModel.showDeleteUserFromGroupState}>
          <Modal.Header>
            <Modal.Title>{`Delete user from group ${this.props.currentChat}`}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <NewGroupTextForm ref="NewGroupTextForm" />
          </Modal.Body>
          <Modal.Footer>
            <Button onClick={this.onClickCloseDeleteUserFromGroupGroupModal}>Cancel</Button>
            <Button onClick={this.onClickAddUserToGroupModal}>Insert</Button>
          </Modal.Footer>
        </Modal>
      );
    }
  };
})();

const mapStateToProps = (state, ownProps) => ({
    showDeleteUserFromGroupModel: state.showDeleteUserFromGroupModel
});

const mapDispatchToProps = {
  showCreateGroup,
  closeCreateGroup,
  showDeleteUserFromGroup,
  closeDeleteUserFromGroup
};

const AppContainer = connect(mapStateToProps, mapDispatchToProps)(AddUserToGroupModal);

export default AppContainer;