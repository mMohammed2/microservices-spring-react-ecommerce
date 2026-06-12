import React, { useEffect, useState } from "react";
import "../assets/css/Orders.css";
import Order from "./Order";
import instance from "../axios";

function Orders({ sellerView }) {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadOrders();
  }, [sellerView]);

  const loadOrders = async () => {
    setLoading(true);

    try {
      const endpoint = sellerView
        ? "/orders/seller"
        : "/orders/user";

      const res = await instance.get(endpoint, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setOrders(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      console.error("Failed to load orders:", err);
      setOrders([]);
    } finally {
      setLoading(false);
    }
  };  
  if (loading) return <h2>Loading Orders...</h2>;

  return (
    <div className="orders">
      <h1>{sellerView ? "Seller Orders" : "Your Orders"}</h1>

      <div className="orders__order">
        {orders.length === 0 ? (
          <p>No Orders Found</p>
        ) : (
          orders.map((order) => (
            <Order
              key={order.id+new Date().getMilliseconds()}
              order={order}
              sellerView={sellerView}
              refreshOrders={loadOrders}
            />
          ))
        )}
      </div>
    </div>
  );
}

export default Orders;