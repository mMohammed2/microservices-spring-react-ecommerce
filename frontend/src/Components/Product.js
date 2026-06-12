import React, { useEffect, useState } from 'react';
import "../assets/css/Product.css";
import StarIcon from '@material-ui/icons/Star';
import instance from '../axios';

function Product({ id, title, image, price, rating, items, quantity, description, category,tax,onClick }) {


    const [cartQty, setCartQty] = useState(0);

    const token = localStorage.getItem("token");

    useEffect(() => {
        fetchQty();
    }, []);

    const fetchQty = async () => {

        if (!token) return;

        try {
            const res = await instance.get("/cart/details", {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            const map = res.data || {};
            setCartQty(map[id] || 0);

        } catch (err) {
            console.error(err);
        }
    };

    const addToCart = async () => {

        await instance.post(
            `/cart/add/${id}`,
            {},
            { headers: { Authorization: `Bearer ${token}` } }
        );

        setCartQty(prev => prev + 1);
    };

    const removeFromCart = async () => {

        await instance.put(
            `/cart/removeFromCart/${id}`,
            {},
            { headers: { Authorization: `Bearer ${token}` } }
        );

        setCartQty(prev => Math.max(prev - 1, 0));
        window.location.reload();
    };

    return (
        <div className="product" onClick={onClick}>

            <div className="product__info">

                <h3>{title}</h3>
                <p>{description}</p>
                <p>{category}</p>

                <p>₹ {price}</p>
                <p>Tax:  {tax} %</p>
                <div>
                    {Array(rating).fill().map((_, i) => (
                        <StarIcon key={i} />
                    ))}
                </div>

                <p>Sold: {items}</p>
                <p>Available: {quantity}</p>
            
            </div>
            <div style={{height:"4rem"}}></div>
            <img src={image} alt={title} />
            <div className="qtyBox">

                    <button onClick={removeFromCart} disabled={cartQty === 0}>
                        -
                    </button>

                    <span>{cartQty}</span>

                    <button onClick={addToCart}>
                        +
                    </button>

                </div>
        </div>
    );
}

export default Product;