"use strict";
import { combineReducers } from "redux";
import { syncHistoryWithStore, routerReducer } from 'react-router-redux'

export const showModel = (state = { show: false }, action) => {
  switch (action.type) {
    case "CLOSE_SIGN_IN": {
      return { show: false };
    }
    case "SHOW_SIGN_IN": {
      return { show: true };
    }
    default:
      return state;
  }
};

export const showLogInModel = (state = { showLogInState: false }, action) => {

  switch (action.type) {
    case "CLOSE_LOG_IN": {
      return { showLogInState: false };
    }
    case "SHOW_LOG_IN": {
      return { showLogInState: true };
    }
    default:
      return state;
  }
};

export const showCreateGroupModel = (state = { showCreateGroupState: false }, action) => {

  switch (action.type) {
    case "CLOSE_CREATE_GROUP": {
      return { showCreateGroupState: false };
    }
    case "SHOW_CREATE_GROUP": {
      return { showCreateGroupState: true };
    }
    default:
      return state;
  }
};
export const showAddUserToGroupModel = (state = { showAddUserToGroupState: false }, action) => {

  switch (action.type) {
    case "CLOSE_ADD_USER_TO_GROUP": {
      return { showAddUserToGroupState: false };
    }
    case "SHOW_ADD_USER_TO_GROUP": {
      return { showAddUserToGroupState: true };
    }
    default:
      return state;
  }
};


export const reducers = combineReducers({
  showLogInModel,
  showModel,
  showCreateGroupModel,
  showAddUserToGroupModel, 
  routing:routerReducer
});
