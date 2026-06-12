import React from 'react';
import "../assets/css/CheckoutProduct.css";
function OrderProduct({
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
                {/* QUANTITY CONTROLS */}
                <div className="qtyContainer">
                    Quantity: <span>{quantity}</span>
                </div>
            </div>
        </div>
    );
}

export default OrderProduct;