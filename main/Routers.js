
import { StackNavigator } from 'react-navigation';
/*
*
* demo
* */
import DemoJpush from './demo/DemoJpush.js';
import DemoPay from './demo/DemoPay.js';
import IndexDmeo from './demo/IndexDmeo.js';







export default  SimpleApp = StackNavigator({
    IndexDmeo: {screen: IndexDmeo},
    jpush: { screen: DemoJpush },
    pay:   { screen: DemoPay },
});
