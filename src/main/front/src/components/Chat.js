import React from "react";
import { FormGroup, ControlLabel, FormControl, Modal, Button } from "react-bootstrap";
import { Panel, Table, Grid, Row, Col } from "react-bootstrap";
import SimpleTextFormControl from "./SimpleTextFormControl";
import Stomp from "stompjs";
import SockJS from "sockjs-client";
import moment from "moment";
import { showCreateGroup, closeCreateGroup, showAddUserToGroup } from "../actions/actions";
import { connect } from "react-redux";
import CreateGroupFormModal from "./modals/CreateGroupModal";
import AddUserToGroupModal from "./modals/AddUserToGroupModal";

const LogoOut = () => (
  <div>
    <h1>Chat window</h1>
    <form action="/logout" method="GET">
      <Button type="submit">LogOut</Button>
    </form>
  </div>
);

let AppPanel = (function() {
  let MessageForm = (function() {
    return class NameTextForm extends React.Component {
      constructor(props) {
        super(props);

        this.state = { name: "" };
        this.handleNameChange = this.handleNameChange.bind(this);
      }
      getLoginNameValidationState() {
        return "success";
      }
      handleNameChange(e) {
        this.setState({ name: e.target.value });
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

  return class AppPanel extends React.Component {
    constructor(props) {
      super(props);
      this.serverUrl = "http://localhost:8080/socket";
      this.title = "WebSockets chat";
      this.stompClient;
      this.user_name;
      this.subscription;
      this.currentChat;

      this.initializeWebSocketConnection = this.initializeWebSocketConnection.bind(this);
      this.sendChatMessage = this.sendChatMessage.bind(this);
      this.createGroup = this.createGroup.bind(this);
      this.addUserToGroup = this.addUserToGroup.bind(this);
      this.showlog = this.showlog.bind(this);
      this.onResponse = this.onResponse.bind(this);

      this.initializeWebSocketConnection(this.onConnection, this.onResponse);

      this.state = {
        response: "",
        request: "",
        messages: [],
        groups: [],
        isConnected: false,
        groupname: "Select a groupchat",
        groupselected: true
      };
    }

    createGroup() {
      this.props.showCreateGroup();
    }

    addUserToGroup() {
      this.props.showAddUserToGroup();
    }

    showlog(event, id) {
      console.log(event);
      console.log(id);
      console.log("log");
    }

    loadGroupsFromUser() {
      var thiss = this;
      let groups = this.state.groups;
      fetch(`http://localhost:8080/user/resources/selectgroups/${this.user_name}`, {
        credentials: "same-origin"
      })
        .then(function(response) {

          if (!response.ok) {
            this.chatErrorManager(response.status);
            throw Error(response.statusText);
          }

          return response.json();
        })
        .then(function(myJson) {
          console.log(myJson);
          thiss.setState({
            groups: myJson
          });
        })
        .catch(error => {
          console.log("Error");
        });
    }

    loadGroups() {
      console.log("loadGroupFromUser" + this.user_name);
      var thiss = this;
      let groups = this.state.groups;
      this.loadGroupsFromUser();
    }
    changeWebSocketConnection(onConnectionCallback, onResponseCallback, groupname) {
      if (this.subscription != null) {
        console.log("unsubscribe");
        this.subscription.unsubscribe();
      }
      let ws = new SockJS(this.serverUrl);
      this.stompClient = Stomp.over(ws);
      let that = this;

      this.stompClient.connect({}, function(frame) {
        that.user_name = frame.headers["user-name"];
        that.subscription = that.stompClient.subscribe(`/chat/${groupname}`, message => {
          if (message.body) {
            onResponseCallback(message);
            console.log("MessageReceivedchange" + message.body);
          }
        });
      });
    }

    onGroupClick(idgroup, name, creation) {
      var thiss = this;
      let groups = this.state.groups;
      console.log("user_name" + thiss.user_name);
      fetch(`http://localhost:8080/messageToGroup/${name}`, {
        credentials: "same-origin"
      })
        .then(function(response) {
          if (!response.ok) {
            this.chatErrorManager(response.status);
            throw Error(response.statusText);
          }
          return response.json();
        })
        .then(function(myJson) {
          console.log(myJson);

          thiss.setState({
            messages: myJson,
            groupname: name,
            groupselected: false
          });
        })
        .catch(error => {
          console.log(`Error ${error}`);
        });

      this.changeWebSocketConnection(this.onConnection, this.onResponse, name);
      this.currentChat = name;
    }

    initializeWebSocketConnection(onConnectionCallback, onResponseCallback) {
      let ws = new SockJS(this.serverUrl);
      this.stompClient = Stomp.over(ws);
      let that = this;
      this.stompClient.connect({}, function(frame) {
        that.user_name = frame.headers["user-name"];
        that.loadGroups();
        that.subscription = that.stompClient.subscribe("/chat", message => {
          if (message.body) {
            onResponseCallback(message);
            console.log("MessageReceived" + message.body);
          }
        });
      });
    }

    sendChatMessage() {
      var message = this.refs.MessageForm.state.name;
      var creation = moment().format("YYYY-MM-DD");
      var url = `http://localhost:8080/messageToGroup/${this.currentChat}`;
      var data = {
        content: message,
        creation: creation,
        sender: this.user_name
      };
      fetch(url, {
        method: "POST",
        body: JSON.stringify(data),
        headers: new Headers({
          "Content-Type": "application/json"
        }),
        credentials: "same-origin"
      })
        .then(response => {
          if (!response.ok) {
            this.saveUserErrorManager(response.status);
            throw Error(response.statusText);
          }
          return response;
        })
        .then(res => res.text())
        .then(response => {
          console.log("response:", response);
        })
        .catch(error => {
          console.error("Error:", error);
        });
      this.stompClient.send(`/app/send/message/${this.currentChat}`, {}, message);
      console.log(`Sending message${this.refs.MessageForm.state.name}`);
    }

    onConnection() {}

    onResponse(message) {
      console.log(`New incoming ${message}`);
      var msg = {
        sender: JSON.parse(message.body).name,
        content: JSON.parse(message.body).message,
        id: message.headers["message-id"]
      };
      let messages = this.state.messages;
      this.setState({
        messages: messages.concat(msg)
      });
    }

    chatErrorManager(errorCode) {
      switch (errorCode) {
        case 400: {
          window.alert("Bad request please check that you are sending the field in the right way");
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

    render() {
      let messages = this.state.messages.map(message => {
        return (
          <tr key={message.id}>
            <td name={message.name}>
                {message.sender}-{message.content}
            </td>
          </tr>
        );
      });

      let messageList = (
        <Panel>
          <div style={{ overflowY: "scroll", height: "500px" }}>
            <Table bordered condensed hover responsive striped style={{ wordWrap: "break-word", tableLayout: "fixed" }}>
              <tbody>
                <tr>
                  <th style={{ width: "100%" }}>Messages</th>
                </tr>
                {messages}
              </tbody>
            </Table>
          </div>
        </Panel>
      );

      ////Groups
      let groups = this.state.groups.map(group => {
        return (
          <tr key={group.idgroup}>
            <td>
              <Button onClick={this.onGroupClick.bind(this, group.idgroup, group.name, group.creation)}>
                {group.name}-{group.creation}
              </Button>
            </td>
          </tr>
        );
      });

      ////GroupsList
      let groupList = (
        <Panel>
          <div style={{ overflowY: "scroll", height: "500px" }}>
            <Table bordered condensed hover responsive striped style={{ wordWrap: "break-word", tableLayout: "fixed" }}>
              <tbody>
                <tr>
                  <th style={{ width: "100%" }}>Groups</th>
                </tr>
                {groups}
              </tbody>
            </Table>
          </div>
        </Panel>
      );
      //this.state.groupselected
      return (
        <Grid fluid={true}>
          <LogoOut />
          <Row className="show-grid">
            <Col md={4} xs={9}>
              <Panel>
                <CreateGroupFormModal user_name={this.user_name} history={this.props.history} />
                <AddUserToGroupModal currentChat={this.currentChat} />
                <Button onClick={this.createGroup} disabled={false}>
                  CreateGroup
                </Button>
                <Button onClick={this.addUserToGroup} disabled={this.state.groupselected} >
                  AddUserToGroup
                </Button>
                <SimpleTextFormControl readOnly={false} />
                <MessageForm ref="MessageForm" />
                <Button disabled={this.state.groupselected} onClick={this.sendChatMessage}>
                  Send
                </Button>
              </Panel>
            </Col>
            <h1><ControlLabel>{this.state.groupname}</ControlLabel></h1>
            <Col md={8} xs={9}>
              {messageList}
            </Col>
            <Col md={8} xs={9}>
              {groupList}
            </Col>
          </Row>
        </Grid>
      );
    }
  };
})();

const mapStateToProps = (state, ownProps) => ({
  shoModal: state.showModel,
  showCreateGroupModel: state.showCreateGroupModel,
  showAddUserToGroupModel: state.showAddUserToGroupModel
});

const mapDispatchToProps = {
  showCreateGroup,
  showAddUserToGroup
};

const AppContainer = connect(mapStateToProps, mapDispatchToProps)(AppPanel);
export default AppContainer;
