'use strict';

import React from 'react';
import { connect } from 'react-redux';
import {
  FormGroup,
  ControlLabel,
  FormControl,
  Modal,
  Button
} from 'react-bootstrap';
import { showSignIn, closeSignIn } from '../../actions/actions';
import {
  isValidUserName,
  isValidPassword,
  isValidEmail
} from '../../util/AppUtils';
import BirthdatePicker from '../DatePicker';

let SingInFormModal = (function() {
  let NameTextForm = (function() {
    return class NameTextForm extends React.Component {
      constructor(props) {
        super(props);
        this.state = { name: "" };
        this.handleNameChange = this.handleNameChange.bind(this);
      }
      getLoginNameValidationState() {
        if (isValidUserName(this.state.name)) return "success";
        else return "error";
      }
      handleNameChange(e) {
        this.setState({
          name: e.target.value
        });
      }
      render() {
        return (
          <div>
            <FormGroup validationState={this.getLoginNameValidationState()}>
              <ControlLabel>Name</ControlLabel>
              <FormControl
                type="text"
                value={this.state.name}
                placeholder="Enter name"
                onChange={this.handleNameChange}
              />
              <FormControl.Feedback />
            </FormGroup>
          </div>
        );
      }
    };
  })();
  let LastNameTextForm = (function() {
    return class LastNameTextForm extends React.Component {
      constructor(props) {
        super(props);
        this.state = { lastName: "" };
        this.handleLastNameChange = this.handleLastNameChange.bind(this);
      }
      getLoginLastNameValidationState() {
        if (isValidUserName(this.state.lastName)) return "success";
        else return "error";
      }
      handleLastNameChange(e) {
        this.setState({
          lastName: e.target.value
        });
      }
      render() {
        return (
          <div>
            <FormGroup validationState={this.getLoginLastNameValidationState()}>
              <ControlLabel> Last Name</ControlLabel>
              <FormControl
                type="text"
                value={this.state.lastName}
                placeholder="Enter last name"
                onChange={this.handleLastNameChange}
              />
              <FormControl.Feedback />
            </FormGroup>
          </div>
        );
      }
    };
  })();
  let EmailTextForm = (function() {
    return class EmailTextForm extends React.Component {
      constructor(props) {
        super(props);
        this.state = { email: "" };
        this.handleEmailChange = this.handleEmailChange.bind(this);
      }
      getLoginEmailValidationState() {
        if (isValidEmail(this.state.email)) return "success";
        else return "error";
      }
      handleEmailChange(e) {
        this.setState({
          email: e.target.value
        });
      }
      render() {
        return (
          <div>
            <FormGroup validationState={this.getLoginEmailValidationState()}>
              <ControlLabel>Email</ControlLabel>
              <FormControl
                type="email"
                value={this.state.email}
                placeholder="Enter email"
                onChange={this.handleEmailChange}
              />
              <FormControl.Feedback />
            </FormGroup>
          </div>
        );
      }
    };
  })();
  let NicknameTextForm = (function() {
    return class NicknameTextForm extends React.Component {
      constructor(props) {
        super(props);
        this.state = { nickname: "" };
        this.handleNicknameChange = this.handleNicknameChange.bind(this);
      }
      getLoginNicknameValidationState() {
        if (isValidUserName(this.state.nickname)) return "success";
        else return "error";
      }
      handleNicknameChange(e) {
        this.setState({
          nickname: e.target.value
        });
      }
      render() {
        return (
          <div>
            <FormGroup validationState={this.getLoginNicknameValidationState()}>
              <ControlLabel> Nickname</ControlLabel>
              <FormControl
                type="text"
                value={this.state.nickname}
                placeholder="Enter nickname"
                onChange={this.handleNicknameChange}
              />
              <FormControl.Feedback />
            </FormGroup>
          </div>
        );
      }
    };
  })();

  return class AppSaveUserModal extends React.Component {
    constructor(props) {
      super(props);
      this.onClickCloseSaveUserModal = this.onClickCloseSaveUserModal.bind(
        this
      );
    }
    onClickCloseSaveUserModal() {

      var url = "http://localhost:8080/user/resources/insert";
      var name = this.refs.NameTextForm.state.name;
      var lastName = this.refs.LastNameTextForm.state.lastName;
      var mail = this.refs.EmailTextForm.state.email;
      var nickname = this.refs.NicknameTextForm.state.nickname;
      var birthdate = this.refs.BirthdatePicker.state.startDate.format('YYYY-MM-DD');
      var data = {
        name: name,
        lastName: lastName,
        mail: mail,
        birthdate: birthdate,
        nickname: nickname
      };

      fetch(url, {
        method: "POST",
        body: JSON.stringify(data),
        headers: new Headers({
          "Content-Type": "application/json"
        })
      })
        .then(res => res.text())
        .then(response => {
          console.log("response:", response);
        })
        .catch(error =>{
             console.error("Errorrr:", error)
             console.dir(error)
             console.log(error)
            
            });
      this.props.closeSignIn();
    }

    render() {
      return (
        <Modal show={this.props.showModel.show}>
          <Modal.Header>
            <Modal.Title>Save user Modal</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <form>
              <NameTextForm ref="NameTextForm" />
              <LastNameTextForm ref="LastNameTextForm" />
              <EmailTextForm ref="EmailTextForm" />
              <NicknameTextForm ref="NicknameTextForm" />
              <BirthdatePicker ref="BirthdatePicker" />
            </form>
          </Modal.Body>
          <Modal.Footer>
            <Button onClick={this.onClickCloseSaveUserModal}>
              Close save user modal
            </Button>
          </Modal.Footer>
        </Modal>
      );
    }
  };
})();

const mapStateToProps = (state, ownProps) => ({
  showModel: state.showModel
});

const mapDispatchToProps = {
  showSignIn,
  closeSignIn
};

const AppContainer = connect(mapStateToProps, mapDispatchToProps)(
  SingInFormModal
);

export default AppContainer;