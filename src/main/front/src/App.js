"use strict";
import React, { Component } from "react";
import { Link } from "react-router-dom";
import { connect } from "react-redux";

import { showSignIn,showLogIn, showCreateGroup } from "./actions/actions";
import SingInFormModal from "./components/modals/SingInFormModal";
import LogInFormModal from "./components/modals/LogInFormModal";
import { Panel, Button, ListGroup, ListGroupItem } from "react-bootstrap";
import Chat from "./components/Chat"
import { Routes,Route} from 'react-router'

import "./App.css";

export class App extends Component {
  constructor(props) {
    super(props);
    this.changeToChat = this.changeToChat.bind(this);
  }
  changeToChat() {

    this.props.history.push('/chat')
    console.log(this)
  }
  render() {


console.log(this)
console.dir(this)

    return (
      <div>
        <header>
          <Panel>
          <Button onClick={this.props.showLogIn}>LogIn</Button>
          <Button onClick={this.props.showSignIn}>SingIn</Button>     
          </Panel>
        </header>
        <SingInFormModal />
        <LogInFormModal history={this.props.history}/>
      </div>
    );
  }
}

const mapStateToProps = (state, ownProps) => ({
  shoModal: state.showModel,
  showLogInModel:state.showLogInModel
});

const mapDispatchToProps = {
  showSignIn,
  showLogIn,
  showCreateGroup,
  
};

const AppContainer = connect(mapStateToProps, mapDispatchToProps)(App);
export default AppContainer;
