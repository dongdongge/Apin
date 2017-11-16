import DemoJpush from './demo/DemoJpush.js';
import DemoPay from './demo/DemoPay.js';
import IndexDmeo from './demo/IndexDmeo.js';
import { StackNavigator } from 'react-navigation';





export default  SimpleApp = StackNavigator({
    IndexDmeo: {screen: IndexDmeo},
    jpush: { screen: DemoJpush },
    pay:   { screen: DemoPay },
});
