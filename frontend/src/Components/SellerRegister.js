import React, { useState } from "react";
import { useHistory } from "react-router-dom";
import "../assets/css/Login.css";
import instance from "../axios";

function SellerRegister() {
  const history = useHistory();

  const token = localStorage.getItem("token");

  const [shopname, setShopname] = useState("");
  const [gstnumber, setGstnumber] = useState("");

  const register = async (e) => {
    e.preventDefault();

    try {
      const response = await instance.post(
        "/auth/signupseller",
        {
          shopname,
          gstnumber,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      alert(response.data.message);
      localStorage.setItem("portiire", "kdhfjdhjghfdk");
      history.push("/");
    } catch (error) {
      alert(error.response?.data?.message || "Could not sign up as seller");
    }
  };

  return (
    <div className="login">
      <h1
        style={{
          margin: "50px auto",
          fontSize: "1rem",
          color: "white",
          fontFamily: "fantasy",
        }}
      >
        SHOPKart
      </h1>

      <div className="login__container">
        <h1>Seller Register</h1>

        {/* If logged in → show seller form */}
        {token ? (
          <form onSubmit={register}>
            <h5>Shop Name</h5>
            <input
              type="text"
              placeholder="Enter your shop name..."
              value={shopname}
              required
              onChange={(e) => setShopname(e.target.value)}
            />

            <h5>GST Number</h5>
            <input
              type="text"
              placeholder="Enter your GST number..."
              value={gstnumber}
              required
              onChange={(e) => setGstnumber(e.target.value)}
            />

            <button type="submit" className="login__signInButton">
              Register as Seller
            </button>
          </form>
        ) : (
          <div>
            <p style={{ color: "white" }}>
              You must be logged in to become a seller.
            </p>

            <button
              onClick={() => history.push("/login")}
              className="login__signInButton"
            >
              Go to Login
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

export default SellerRegister;