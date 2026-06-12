import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import "../assets/css/Login.css";
import instance from "../axios";
function Login() {
    // programmatically change the url
    const history = useHistory(); 
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const signIn = async (e) => {
        e.preventDefault();
        try {
            const response = await instance.post("/auth/login", {
                email,
                password
            });
            alert("Logged in Successfully");
            localStorage.setItem("token",response.data.token);
            localStorage.setItem("username",email);               
            if(response.data.seller){
                localStorage.setItem("portiire", "kdhfjdhjghfdk");
            }
            history.push("/");
        } catch (error) {
            alert(
                "Check credentials and try again"
            );
        }
    };

    const register = e => {
        e.preventDefault()
        history.push("/register");
    }

    return (
        <div className="login">
            <h1
            style={{
              margin: "50px auto",
              marginLeft:"40px",
              marginRight:"40px",
              fontSize: "1rem",
              color:"white",
              fontFamily: "fantasy"
            }}
          >
            SHOPKart
          </h1>

            <div className="login__container">
                <h1>Login</h1>
                <form>
                    <h5>E-mail</h5>
                    {/* e.target.value = whatever user type in email */}
                    <input type="email" placeholder="enter your email..." value={email} onChange={e => setEmail(e.target.value)}/>

                    <h5>Password</h5>
                    <input type="password" placeholder="enter your password..." value={password} onChange={e => setPassword(e.target.value)}/>

                    <button type="submit" onClick={signIn} className="login__signInButton">Sign In</button>
                </form>

                <p>By signing-in you agree to SHOPKART Conditions of Use & Sale. Please
                see our Privacy Notice, our Cookies Notice and our Interest-Based Ads Notice.</p>

                <button onClick={register} className="login__registerButton">Create your Shopkart Account</button>
            </div>
        </div>
    )
}

export default Login
