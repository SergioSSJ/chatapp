"use strict";

import React, { isValidElement } from "react";
import { connect } from "react-redux";
import {
  FormGroup,
  ControlLabel,
  FormControl,
  Modal,
  Button
} from "react-bootstrap";
import {
  isValidUserName,
  isValidEmail
} from "../../util/AppUtils";
import {showCreateGroup, closeCreateGroup} from "../../actions/actions";//change actions to proper create group

let CreateGroupModal = (function() {


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
              <ControlLabel> groupname</ControlLabel>
              <FormControl
                name="name"
                type="text"
                value={this.state.groupname}
                placeholder="Enter groupname"
                onChange={this.handlegroupnameChange}
              />
              <FormControl.Feedback />
            </FormGroup>
          </div>
        );
      }
    };
  })();

  return class AppLogInModal extends React.Component {
    constructor(props) {
      super(props);
      this.onClickCreateGroupModal = this.onClickCreateGroupModal.bind(this);
      this.onClickCloseCreateGroupModal = this.onClickCloseCreateGroupModal.bind(this);
    }
    validateFieldsToSave(data) {
      if (isValidUserName(data.name))
        return true;
      return false;
    }
    retrieveDataToLogInUser() {
      var groupname = this.refs.NewGroupTextForm.state.groupname;
      return {
        name: groupname
      };
    }
    onClickCreateGroupModal() {
        var data = this.retrieveDataToLogInUser();

        if (!this.validateFieldsToSave(data)) {
          window.alert("Complete los campos de manera correcta");
          return;
        }

      var url = `http://localhost:8080/group/${data.name}`;

      fetch(url, {
        method: "POST",
        credentials: "include",
        headers: new Headers({
          Accept:
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"
        })
      })
        .then(response => {
          if (!response.ok) {
            this.logInErrorManager(response.status);
            throw Error(response.statusText);
          }
          return response;
        })
        .then(res => res.text())
        .then(response => {
        
            console.log('new group created')
          console.log(response)

        })
        .catch(error => {
          console.error("Error:", error);
        });
      //this.props.closeCreateGroup();
    }

    logInErrorManager(errorCode) {
      switch (errorCode) {
        case 400: {
          window.alert(
            "Bad request please check that you are sending the field in the right way"
          );
          break;
        }
        case 401: {
          window.alert("Not authorized");
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

    onClickCloseCreateGroupModal() {
      this.props.closeCreateGroup();
    }

    render() {
      return (
        <Modal show={this.props.showCreateGroupModel.showCreateGroupState}>
          <Modal.Header>
            <Modal.Title>LogIn Modal</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <NewGroupTextForm ref="NewGroupTextForm" />
          </Modal.Body>
          <Modal.Footer>
            <Button onClick={this.onClickCloseCreateGroupModal}>Cancel</Button>
            <Button onClick={this.onClickCreateGroupModal}>LogIn</Button>
          </Modal.Footer>
        </Modal>
      );
    }
  };
})();

const mapStateToProps = (state, ownProps) => ({
  showCreateGroupModel: state.showCreateGroupModel
});

const mapDispatchToProps = {
  showCreateGroup,
  closeCreateGroup
};

const AppContainer = connect(mapStateToProps, mapDispatchToProps)(
  CreateGroupModal
);

export default AppContainer;


