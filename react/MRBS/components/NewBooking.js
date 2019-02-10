import React, { Component } from 'react';
import {
    StyleSheet,
    Text,
    View,
    TouchableHighlight,
    Button
} from 'react-native';
import DatePicker from 'react-native-datepicker';
import ListPopover from 'react-native-list-popover';

export default class NewBooking extends Component {
    constructor(props) {
        super(props);

        this.state = {
            date: '',
            time1: '20:00',
            time2: '20:00',
            isVisible: false,
            item: ''
        };
    }

    render() {
        var LIST = ["Room A", "Room B", "Room C"];
        return (
            <View style={styles.container}>
                <Text style={styles.welcome}>Date</Text>
                <DatePicker
                    style={{width: 200}}
                    date={this.state.date}
                    mode="date"
                    format="YYYY-MM-DD"
                    confirmBtnText="Confirm"
                    cancelBtnText="Cancel"
                    showIcon={false}
                    minuteInterval={10}
                    onDateChange={(date) => {this.setState({date: date});}}
                />
                <Text style={styles.welcome}>From</Text>
                <DatePicker
                    style={{width: 200}}
                    date={this.state.time1}
                    mode="time"
                    format="HH:mm"
                    confirmBtnText="Confirm"
                    cancelBtnText="Cancel"
                    showIcon={false}
                    minuteInterval={10}
                    onDateChange={(time) => {this.setState({time1: time});}}
                />
                <Text style={styles.welcome}>To</Text>
                <DatePicker
                    style={{width: 200}}
                    date={this.state.time2}
                    mode="time"
                    format="HH:mm"
                    confirmBtnText="Confirm"
                    cancelBtnText="Cancel"
                    showIcon={false}
                    minuteInterval={10}
                    onDateChange={(time) => {this.setState({time2: time});}}
                />
                <Text style={styles.welcome}>Room</Text>
                <TouchableHighlight
                    style={styles.button}
                    onPress={() => this.setState({isVisible: true})}>
                    <Text>{this.state.item || 'Select'}</Text>
                </TouchableHighlight>
                <ListPopover style={styles.container}
                    list={LIST}
                    isVisible={this.state.isVisible}
                    onClick={(item) => this.setState({item: item})}
                    onClose={() => this.setState({isVisible: false})}/>

                <Button style={{width: 200}}
                    title="Book"
                    color="#841584"
                />
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'space-evenly',
        alignItems: 'center',
        backgroundColor: '#F5FCFF'
    },
    welcome: {
        fontSize: 12,
        textAlign: 'center',
        margin: 10
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5
    },
    button: {
        backgroundColor: '#F5FCFF',
        marginLeft: 10,
        marginRight: 10,
        width: 200,
        padding: 10,
        borderWidth: 1,
        borderColor: '#aaa',
        alignItems: 'center',
        justifyContent: 'center'
    },
});