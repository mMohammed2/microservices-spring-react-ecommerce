import React from 'react';
import "../assets/css/CheckoutProduct.css";
import StarIcon from '@material-ui/icons/Star';
import instance from '../axios';

function CheckoutProduct({
    id,
    image,
    title,
    price,
    rating,
    quantity,
    refreshCart,
    tax
}) {

    const token = localStorage.getItem("token");

    const add = async () => {

        await instance.post(
            `/cart/add/${id}`,
            {},
            {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        );
        refreshCart();
        window.location.reload();
    };

    const remove = async () => {

        await instance.put(
            `/cart/removeFromCart/${id}`,
            {},
            {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        );
        refreshCart();
        window.location.reload();
    };

    return (
        <div className="checkoutProduct">

            <img
                src={image}
                className="checkoutProduct__image"
                alt={title}
            />

            <div className="checkoutProduct__info">

                <p className="checkoutProduct__title">
                    {title}
                </p>

                <p className="checkoutProduct__price">
                    ₹ {price} + {tax} %
                </p>

                <div className="checkoutProduct__rating">
                    {Array(rating).fill().map((_, i) => (
                        <StarIcon key={i} />
                    ))}
                </div>

                {/* QUANTITY CONTROLS */}
                <div className="qtyContainer">

                    <button onClick={remove}>
                        -
                    </button>

                    <span>{quantity}</span>

                    <button onClick={add}>
                        +
                    </button>

                </div>

            </div>

        </div>
    );
}

export default CheckoutProduct;