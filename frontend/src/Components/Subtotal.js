import React from 'react';
import "../assets/css/Subtotal.css";
import CurrencyFormat from "react-currency-format";
import { useHistory } from 'react-router-dom';

function Subtotal({products,cartMap}) {
    // Gives us browser history
    const history = useHistory();
     const total = products.reduce((sum, item) => {
        const qty = cartMap[item.id] || 1;
        return sum + item.price * qty;
    }, 0);
    
    return (
        <div className="subtotal">
            <CurrencyFormat
                renderText={(value) => (
                <>
                    <p>Subtotal ({products?.length} items): <strong>{value}</strong></p>
                </>
                )}
                decimalScale={2}
                value ={total}
                displayType={"text"}
                thousandSeparator={true}
                prefix={"Rs. "}
            />

            <button onClick={e => history.push('/payment')}>Proceed to checkout</button>
        </div>
    )
}

export default Subtotal
