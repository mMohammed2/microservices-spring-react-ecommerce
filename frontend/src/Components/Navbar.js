import React, { useState } from 'react';
import "../assets/css/Navbar.css";
import DehazeOutlinedIcon from '@material-ui/icons/DehazeOutlined';
import CloseIcon from '@material-ui/icons/Close';
import { useHistory } from "react-router-dom";

function Navbar() {

    const [menuOpen, setMenuOpen] = useState(false);
    const history =  useHistory();
    const isSeller = localStorage.getItem("portiire") === "kdhfjdhjghfdk";
    return (
        <>
            <div className="navbar">

                {/* Hamburger Icon */}
                <p
                    className="header__line"
                    onClick={() => setMenuOpen(true)}
                >
                    <DehazeOutlinedIcon />
                </p>

                <div className="navbar__option">
                    <span>Gift Cards</span>
                </div>

                <div className="navbar__option">
                    <span>Best Sellers</span>
                </div>

                <div className="navbar__option">
                    <span>Mobiles</span>
                </div>

                <div className="navbar__option">
                    <span>Today's Deals</span>
                </div>

                <div className="navbar__option">
                    <span>Prime</span>
                </div>

                <div className="navbar__option">
                    <span>New Releases</span>
                </div>
            </div>

            {/* Overlay */}
            {menuOpen && (
                <div
                    className="menu__overlay"
                    onClick={() => setMenuOpen(false)}
                ></div>
            )}

            {/* Side Menu */}
            <div className={`side__menu ${menuOpen ? "active" : ""}`}>
                <div className="side__menuHeader">
                    <h3>Menu</h3>

                    <CloseIcon
                        className="close__icon"
                        onClick={() => setMenuOpen(false)}
                    />
                </div>

                <div className="side__menuItems">
                    {isSeller ? (
                        <p onClick={() => {
                            history.push("/sentral");
                            setMenuOpen(false);
                        }}>
                            Seller Central
                        </p>
                    ) : (
                        <p onClick={() => {
                            history.push("/segister");
                            setMenuOpen(false);
                        }}>
                            Become a Seller
                        </p>
                    )}
                    <p onClick={() => {history.push("/user");setMenuOpen(false);}}>Your Account</p>
                    <p onClick={() => {history.push("/orders");setMenuOpen(false);}}>Orders</p>
                    <p onClick={() => {history.push("/");setMenuOpen(false);}}>Home</p>
                    <p>Prime Membership</p>
                    <p>Customer Service</p>
                </div>

            </div>
        </>
    )
}

export default Navbar;