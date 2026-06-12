import React, { useState, useEffect } from "react";
import { useHistory } from "react-router-dom";
import CheckoutProduct from "./CheckoutProduct";
import "../assets/css/Payment.css";
import CurrencyFormat from "react-currency-format";
import instance from "../axios";

function Payment() {
  const history = useHistory();

  const [cartItems, setCartItems] = useState([]);
  const [address, setAddress] = useState([]);
  const [selectedAddressIndex, setSelectedAddressIndex] = useState(0);

  // coupons: percentage based
  const [coupons, setCoupons] = useState({}); 
  const [couponInputs, setCouponInputs] = useState({});

  const [paymentMethod, setPaymentMethod] = useState("COD");
  const [paymentVerified, setPaymentVerified] = useState(false);
  const [transactionId, setTransactionId] = useState("");

  const [loading, setLoading] = useState(false);
  const [pageLoading, setPageLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    loadCheckoutData();
  }, []);

  useEffect(() => {
    const listener = (event) => {
      if (!event.data) return;

      if (event.data.type === "PAYMENT_SUCCESS") {
        setPaymentVerified(true);
        setPaymentMethod(event.data.paymentMethod);
        setTransactionId(event.data.transactionId);
      }

      if (event.data.type === "PAYMENT_FAILED") {
        setError("Payment failed");
      }
    };

    window.addEventListener("message", listener);
    return () => window.removeEventListener("message", listener);
  }, []);

  const loadCheckoutData = async () => {
    try {
      setPageLoading(true);
      const token = localStorage.getItem("token");

      const [addressRes, cartRes] = await Promise.all([
        instance.get("/users/getAddress", {
          headers: { Authorization: `Bearer ${token}` },
        }),
        instance.get("/cart/details", {
          headers: { Authorization: `Bearer ${token}` },
        }),
      ]);

      setAddress(Array.isArray(addressRes.data) ? addressRes.data : []);

      const cartMap = cartRes.data || {};
      const productIds = Object.keys(cartMap);

      if (!productIds.length) {
        setCartItems([]);
        return;
      }

      const productsRes = await instance.post(
        "/products/getCart",
        productIds,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      const products = productsRes.data;

      setCartItems(
        products.map((p) => ({
          ...p,
          qty: cartMap[p.id],
        }))
      );
    } catch {
      setError("Failed to load checkout");
    } finally {
      setPageLoading(false);
    }
  };

  const selectedAddress =
  address.length > 0 ? address[selectedAddressIndex] : null;

  const subtotal = cartItems.reduce(
    (sum, item) => sum + item.price * item.qty,
    0
  );

  const discount = cartItems.reduce((sum, item) => {
    const coupon = coupons[item.id];

    if (!coupon) return sum;

    return (
      sum +
      (item.price *
        item.qty *
        coupon.discountPercent) /
        100
    );
  }, 0);

  const tax = cartItems.reduce((sum, item) => {
    const coupon = coupons[item.id];

    const discountPercent =
      coupon?.discountPercent || 0;

    const discountedPrice =
      item.price * (1 - discountPercent / 100);

    return (
      sum +
      discountedPrice *
        item.qty *
        (item.tax / 100)
    );
  }, 0);

  const grandTotal = subtotal - discount + tax;

  // APPLY COUPON
  const applyCoupon = async (productId) => {
    try {
      setError("");

      const code = couponInputs[productId];

      const res = await instance.post(
        "/seller/validate",
        { productId, code },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      if (res.data.valid) {
        setCoupons((prev) => ({
          ...prev,
          [productId]: {
            code,
            discountPercent: res.data.discount, // % from backend
          },
        }));
      } else {
        setError("Invalid coupon for product");
      }
    } catch {
      setError("Coupon service error");
      alert("Coupon cant be used on the product")
    }
  };

  const openPaymentPopup = (method) => {
    window.open(
      `/payment/secure?method=${method}&amount=${grandTotal}`,
      "payment",
      "width=500,height=650"
    );
  };

  const handlePlaceOrder = async () => {
    if (!selectedAddress) {
      setError("Select address");
      return;
    }

    if (paymentMethod !== "COD" && !paymentVerified) {
      setError("Complete payment first");
      return;
    }

    try {
      setLoading(true);

      const couponPayload = Object.fromEntries(
          Object.entries(coupons).map(([productId, value]) => [
            productId,
            value.code
          ])
        );

      await instance.post(
        "/orders/create",
        {
          address: selectedAddress,
          paymentMethod,
          paymentStatus: paymentMethod === "COD" ? "UNPAID" : "PAID",
          transactionId,
          coupons: couponPayload,
        },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      history.push("/orders");
    } catch {
      setError("Order failed");
    } finally {
      setLoading(false);
    }
  };

  if (pageLoading) {
    return <div className="payment__loading">Loading checkout...</div>;
  }

  return (
    <div className="payment">
      <div className="payment__container">

        <h1 className="payment__title">Checkout</h1>

        {/* ADDRESS */}
        <div className="payment__card">
          <h3>Delivery Address</h3>

          <select
            className="payment__select"
            value={selectedAddressIndex}
            onChange={(e) =>
              setSelectedAddressIndex(Number(e.target.value))
            }
          >
            {address.map((a, i) => (
              <option key={i} value={i}>
                {a}
              </option>
            ))}
          </select>

          {selectedAddress && (
            <div className="payment__selectedBox">
              {selectedAddress}
            </div>
          )}
        </div>

        {/* ITEMS */}
        <div className="payment__card">
          <h3>Items</h3>

          {cartItems.map((item) => {
            const coupon = coupons[item.id];

          const itemTotal = item.price * item.qty;

          const itemDiscount = coupon
            ? (itemTotal * coupon.discountPercent) / 100
            : 0;

          const finalItemAmount =
            item.price *
            (1 - (coupon?.discountPercent || 0) / 100) *
            (1 + item.tax / 100) *
            item.qty;

          return (
              <div key={item.id} className="payment__item">

                <CheckoutProduct
                  id={item.id}
                  image={item.images}
                  title={item.title}
                  price={item.price}
                  rating={
                    item.totalReviews > 0
                      ? Math.round(item.totalRating / item.totalReviews)
                      : 0
                  }
                  tax={item.tax}
                  refreshCart={loadCheckoutData}
                  quantity={item.qty}
                />

                {/* PRICE BREAKDOWN */}
                <div style={{ marginTop: "8px" }}>
                  <p>
                    Price: ₹{item.price} × {item.qty} = ₹{itemTotal}
                  </p>

                  {coupon && (
                    <p style={{ color: "green" }}>
                      Coupon ({coupon.discountPercent}%): -₹
                      {itemDiscount.toFixed(2)}
                    </p>
                  )}

                  <p style={{ fontWeight: "bold" }}>
                    Final: ₹{finalItemAmount.toFixed(2)}
                  </p>
                </div>

                {/* COUPON */}
                <div className="payment__couponBox">
                  <input
                    className="payment__input"
                    placeholder="Enter coupon"
                    value={couponInputs[item.id] || ""}
                    onChange={(e) =>
                      setCouponInputs((prev) => ({
                        ...prev,
                        [item.id]: e.target.value,
                      }))
                    }
                  />

                  <button
                    className="payment__btn"
                    onClick={() => applyCoupon(item.id)}
                  >
                    Apply
                  </button>

                  {coupons[item.id] && (
                    <p style={{ color: "green", marginTop: "5px" }}>
                      Applied: {coupons[item.id].code} (
                      {coupons[item.id].discountPercent}%)
                    </p>
                  )}
                </div>
              </div>
            );
          })}
        </div>

        {/* PAYMENT */}
        <div className="payment__card">
          <h3>Payment Method</h3>

          <button
            className="payment__btn"
            onClick={() => setPaymentMethod("COD")}
          >
            Cash On Delivery
          </button>

          <button
            className="payment__btn"
            onClick={() => openPaymentPopup("UPI")}
          >
            Pay with UPI
          </button>

          <button
            className="payment__btn"
            onClick={() => openPaymentPopup("NETBANKING")}
          >
            Net Banking
          </button>
        </div>

        {/* BILLING */}
        <div className="payment__card payment__billing">

          <div className="payment__line">
            <span>Subtotal</span>
            <span>₹{subtotal.toFixed(2)}</span>
          </div>

          <div className="payment__line">
            <span>Tax</span>
            <span>₹{tax.toFixed(2)}</span>
          </div>

          <div className="payment__line">
            <span>Discount</span>
            <span>-₹{discount.toFixed(2)}</span>
          </div>

          <CurrencyFormat
            value={grandTotal}
            displayType="text"
            thousandSeparator
            prefix="₹"
            renderText={(v) => (
              <h2 className="payment__total">Total: {v}</h2>
            )}
          />

          <button
            className="payment__placeOrder"
            onClick={handlePlaceOrder}
            disabled={loading}
          >
            {loading ? "Placing Order..." : "Place Order"}
          </button>

          {error && <p className="payment__error">{error}</p>}
        </div>

      </div>
    </div>
  );
}

export default Payment;