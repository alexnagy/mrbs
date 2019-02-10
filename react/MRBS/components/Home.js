import React, { Component } from 'react';
import {
    StyleSheet,
    Text,
    View
} from 'react-native';
import CalendarPicker from 'react-native-calendar-picker';
import FAB from 'react-native-fab';
import NewBooking from "./NewBooking";

export default class Home extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedStartDate: null,
        };
        this.onDateChange = this.onDateChange.bind(this);
    }

    onDateChange(date) {
        this.setState({
            selectedStartDate: date,
        });
    }
    render() {
        const { selectedStartDate } = this.state;
        const startDate = selectedStartDate ? selectedStartDate.toString() : '';
        return (
            <View style={styles.container}>
                <CalendarPicker
                    onDateChange={this.onDateChange}
                />
                <FAB
                    buttonColor="blue"
                    iconTextColor="#FFFFFF"
                    onClickAction={() => {this.props.navigation.navigate('NewBooking')}}
                    visible={true}/>
            </View>

        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#FFFFFF',
        marginTop: 10,
    },
});