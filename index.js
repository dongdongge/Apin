import { AppRegistry } from 'react-native';
import App from './App';
import DemoJpush from './main/demo/DemoJpush.js';
import DemoPay from './main/demo/DemoPay.js';
import { StackNavigator } from 'react-navigation';
const SimpleApp = StackNavigator({
    App:   { screen: App },
    jpush: { screen: DemoJpush },
    pay:   { screen: DemoPay },
});
AppRegistry.registerComponent('Apin', () => SimpleApp);
