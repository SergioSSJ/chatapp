// store.js

import { reducers } from "../reducers/reducer";
import { combineReducers, createStore, compose, applyMiddleware } from "redux";
import createHistory from 'history/createBrowserHistory'
import { ConnectedRouter, routerReducer, routerMiddleware } from 'react-router-redux';

export const history = createHistory()


export function configureStore(initialState = {}) {
  const store = createStore(reducers,undefined,compose(applyMiddleware(routerMiddleware(history))));
  return store;
}

export const store = configureStore();
