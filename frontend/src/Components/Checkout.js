import React, { useEffect, useState } from 'react';
import "../assets/css/Checkout.css";
import CheckoutProduct from './CheckoutProduct';
import Subtotal from "./Subtotal";
import instance from '../axios';

function Checkout() {

    const [cartMap, setCartMap] = useState({});
    const [products, setProducts] = useState([]);
    const [username, setUsername] = useState('');

    useEffect(() => {
        loadCart();
    }, []);

    const loadCart = async () => {

        const token = localStorage.getItem("token");

        if (!token) return;

        try {

            // 1. Get cart map
            const cartResponse = await instance.get(
                "/cart/details",
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            const cartData = cartResponse.data || {};

            setCartMap(cartData);

            const productIds = Object.keys(cartData);

            if (productIds.length === 0) {
                setProducts([]);
                return;
            }

            // 2. Fetch product details
            const productsResponse = await instance.post(
                "/products/getCart",
                productIds,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            setProducts(productsResponse.data);
            setUsername(localStorage.getItem("username"));

        } catch (error) {
            console.error("Cart load failed", error);
        }
    };

    return (
        <div className="checkout">

            <div className="checkout__left">

                <h3>Hello, {username}</h3>

                <h2>Your Shopping Basket</h2>

                {products.map(product => (
                    <CheckoutProduct
                        key={product.id}
                        id={product.id}
                        title={product.title}
                        image={product.images}
                        price={product.price}
                        rating={
                            product.totalReviews > 0
                                ? Math.round(product.totalRating / product.totalReviews)
                                : 0
                        }
                        quantity={cartMap[product.id]}
                        refreshCart={loadCart}
                        tax={product.tax}
                    />
                ))}

            </div>

            <div className="checkout__right">
                <Subtotal
                    products={products}
                    cartMap={cartMap}
                />
            </div>

        </div>
    );
}

export default Checkout;