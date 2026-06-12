import React, { useEffect, useState } from "react";
import instance from "../axios";
import "../assets/css/SellerAnalytics.css";

function SellerAnalytics() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadOrders();
  }, []);

  const loadOrders = async () => {
    try {
      const res = await instance.get("/orders/analyze", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      console.log(res);
      setOrders(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      console.error(err);
      setOrders([]);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <h2>Loading analytics...</h2>;

  // ----------------------------
  // 📊 CALCULATIONS
  // ----------------------------

  const totalOrders = orders.length;

  const totalRevenue = orders.reduce(
    (sum, o) => sum + (o.total || o.price || 0),
    0
  );

  const totalTax = orders.reduce(
    (sum, o) => sum + (o.tax || 0),
    0
  );

  const totalDiscount = orders.reduce(
    (sum, o) => sum + (o.discount || 0),
    0
  );

  const pendingOrders = orders.filter(
    (o) => o.status !== "DELIVERED"
  ).length;

  const deliveredOrders = orders.filter(
    (o) => o.status === "DELIVERED"
  ).length;

  // ----------------------------
  // UI
  // ----------------------------

  return (
    <div className="analytics">

      <h1>Seller Analytics Dashboard</h1>

      <div className="analytics__grid">

        <div className="card main">
          <h3>Total Orders</h3>
          <p>{totalOrders}</p>
        </div>

        <div className="card revenue">
          <h3>Total Revenue</h3>
          <p>₹{totalRevenue.toFixed(2)}</p>
        </div>

        <div className="card tax">
          <h3>Total Tax</h3>
          <p>₹{totalTax.toFixed(2)}</p>
        </div>

        <div className="card discount">
          <h3>Total Discounts</h3>
          <p>₹{totalDiscount.toFixed(2)}</p>
        </div>

        <div className="card highlight">
          <h3>Pending Orders</h3>
          <p>{pendingOrders}</p>
        </div>

        <div className="card success">
          <h3>Delivered Orders</h3>
          <p>{deliveredOrders}</p>
        </div>

      </div>
    </div>
  );
}

export default SellerAnalytics;