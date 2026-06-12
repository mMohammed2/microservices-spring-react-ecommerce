import React, { useState } from "react";
import { useHistory } from "react-router-dom";
import "../assets/css/Login.css";
import instance from "../axios";

function Register() {
  const history = useHistory();

  const [email, setEmail] = useState("");
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");
  const [contact, setContact] = useState("");

  // Validate Indian mobile number
  const validateContact = (number) => {
    const regex = /^[6-9]\d{9}$/;
    return regex.test(number);
  };

  const signIn = (e) => {
    e.preventDefault();
    history.push("/login");
  };

  const register = async (e) => {
    e.preventDefault();

    // Validation
    if (!validateContact(contact)) {
      alert("Please enter a valid 10 digit contact number");
      return;
    }

    try {
      const response = await instance.post("/auth/signup", {
        email,
        password,
        name,
        contact,
      });

      alert(response.data.message);
      localStorage.setItem("token", response.data.token);
      localStorage.setItem("username", name);
      // Redirect only after success
      history.push("/");
    } catch (error) {
      alert(error.response?.data?.message || "Could not sign up");
    }
  };

  return (
    <div className="login">
      <h1
        style={{
          margin: "50px auto",
          marginLeft: "40px",
          marginRight: "40px",
          fontSize: "1rem",
          color: "white",
          fontFamily: "fantasy",
        }}
      >
        SHOPKart
      </h1>

      <div className="login__container">
        <h1>Register</h1>

        <form onSubmit={register}>
          <h5>E-mail</h5>
          <input
            type="email"
            placeholder="Enter your email..."
            value={email}
            required
            onChange={(e) => setEmail(e.target.value)}
          />

          <h5>Name</h5>
          <input
            type="text"
            placeholder="Enter your name..."
            value={name}
            required
            onChange={(e) => setName(e.target.value)}
          />

          <h5>Contact</h5>
          <input
            type="tel"
            placeholder="Enter your contact..."
            value={contact}
            required
            maxLength={10}
            onChange={(e) => setContact(e.target.value)}
          />

          <h5>Password</h5>
          <input
            type="password"
            placeholder="Set your password..."
            value={password}
            required
            onChange={(e) => setPassword(e.target.value)}
          />

          <button type="submit" className="login__signInButton">
            Create your Shopkart Account
          </button>
        </form>

        <p>
          By signing-up you agree to SHOPKART Conditions of Use & Sale.
          Please see our Privacy Notice, our Cookies Notice and our
          Interest-Based Ads Notice.
        </p>

        <button
          onClick={signIn}
          className="login__registerButton"
        >
          Sign In
        </button>
      </div>
    </div>
  );
}

export default Register;