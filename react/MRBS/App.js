import React from 'react';
import { View, Text, Button } from 'react-native';
import { createAppContainer, createStackNavigator, StackActions, NavigationActions } from 'react-navigation';
import Home from "./components/Home";
import NewBooking from "./components/NewBooking";

const AppNavigator = createStackNavigator({
    Home: {
        screen: Home,
        navigationOptions: {
            title: 'MRBS',
        },
    },
    NewBooking: {
        screen: NewBooking,
        navigationOptions: {
            title: 'New Booking',
        },
    },
}, {
    initialRouteName: 'Home',
});

export default createAppContainer(AppNavigator);