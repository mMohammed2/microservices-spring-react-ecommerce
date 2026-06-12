import './App.css';
import Header from './Components/Header';
import Navbar from './Components/Navbar';
import Home from './Components/Home';
import Footer from "./Components/Footer";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Checkout from './Components/Checkout';
import Login from './Components/Login';
import Payment from "./Components/Payment";
import Orders from './Components/Orders';
import Register from './Components/Register';
import User from './Components/User';
import SellerRegister from './Components/SellerRegister';
import SellerCentral from './Components/SellerCentral';
import SecurePayment from './Components/SecurePayments';


function App() {
  return (
    <Router>
      <div className="app">

        {/* Header */}

        <Switch>

          <Route path="/orders">
            <Header />
            <Navbar />
            <Orders />
          </Route>
          
          <Route path="/login"> 
            <Login />
          </Route>

          <Route path="/checkout">
            <Header />
            <Navbar />
            <Checkout />
            <Footer />
          </Route>
          <Route
              path="/payment/secure"
              component={SecurePayment}
            />

          <Route path="/user">
            <Header />
            <Navbar />
            <User />
            <Footer />
          </Route>
          <Route path="/sentral">
            <SellerCentral />
          </Route>

          <Route path="/segister">
            <SellerRegister />
          </Route>

          <Route path="/register">
            <Register />            
          </Route>

          <Route path="/payment">
            <Header />
            <Navbar />
            <Payment />
         
          </Route>
          
          {/* Default Route is at bottom*/}
          <Route path="/">
            {/* Home */}
            <Header />
            <Navbar />
            <Home />
            <Footer />
          </Route>
        </Switch>

      </div>
    </Router>
  );
}

export default App;
