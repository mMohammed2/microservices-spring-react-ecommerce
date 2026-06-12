import React, { useEffect, useState } from 'react';
import '../assets/css/Header.css';
import SearchIcon from "@material-ui/icons/Search";
import ShoppingCartOutlinedIcon from '@material-ui/icons/ShoppingCartOutlined';
import { Link } from 'react-router-dom';
import instance from '../axios';

function Header() {

    const [cartCount, setCartCount] = useState(0);
    useEffect(() => {
        const getCartCount = async () => {

            const token = localStorage.getItem("token");

            if (!token) {
                setCartCount(0);
                return;
            }

            try {

                const response = await instance.get('/cart/count', {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });

                setCartCount(response.data);
            } catch (error) {

                console.error("Failed to fetch cart count", error);
                setCartCount(0);

            }
        };

        getCartCount();
    }, []);

    return (
        <div className="header">
            <div className='home__row'>
                <h1
                    style={{
                        margin: "50px auto",
                        marginLeft: "40px",
                        marginRight: "40px",
                        fontSize: "1rem",
                    }}
                >
                    SHOPKart
                </h1>
            </div>

            <div className="header__search">
                <input className="header__searchInput" type="text" />
                <SearchIcon
                    className="header__searchIcon"
                    style={{ color: '#2874f0' }}
                />
            </div>

            <div className="header__nav">

                <Link
                    to={localStorage.getItem("token") ? "/user" : "/login"}
                    style={{ textDecoration: 'none' }}
                >
                    <div className="header__option">
                        <span className="header__optionLineOne">
                            Hello {localStorage.getItem("username") || 'Guest'}
                        </span>
                        <span
                            className="header__optionLineTwo"
                            style={{
                                backgroundColor: '#fff',
                                color: '#000',
                                textAlign: 'center',
                                padding: '2px 5px'
                            }}
                        >
                            {localStorage.getItem("token")
                                ? 'Sign Out'
                                : 'Sign In'}
                        </span>
                    </div>
                </Link>

                <Link
                    to="/orders"
                    style={{ textDecoration: 'none' }}
                >
                    <div className="header__option">
                        <span className="header__optionLineOne">
                            Return
                        </span>
                        <span className="header__optionLineTwo">
                            & Orders
                        </span>
                    </div>
                </Link>

                <Link
                    to="/checkout"
                    style={{ textDecoration: 'none' }}
                >
                    <div className="header__optionBasket">
                        <span className="header__optionLineOne">
                            <ShoppingCartOutlinedIcon />
                        </span>
                        <span className="header__optionLineTwo">
                            Cart {cartCount}
                        </span>
                    </div>
                </Link>

            </div>
        </div>
    );
}

export default Header;