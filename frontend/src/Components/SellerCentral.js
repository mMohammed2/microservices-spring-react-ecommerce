import React, { useEffect, useState } from "react";
import "../assets/css/SellerCentral.css";
import instance from "../axios";
import SellerProduct from "./SellerProduct";
import Orders from "./Orders";
import Coupons from "./Coupons";
import AddProduct from "./AddProduct";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import SellerAnalytics from "./SellerAnalytics";

const SellerCentral = () => {
  const [tab, setTab] = useState("products");
  const [products, setProducts] = useState([]);

  const history = useHistory();

  const fetchProducts = async () => {
    const res = await instance.get("/products/seller", {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });

    setProducts(res.data);
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  return (
    <div className="seller-dashboard">

      {/* Sidebar */}
      <aside className="seller-sidebar">
        <div className="seller-logo">
          <h2>Seller Central</h2>
        </div>

        <button
          className={tab === "products" ? "active" : ""}
          onClick={() => setTab("products")}
        >
          📦 Products
        </button>

        <button
          className={tab === "add" ? "active" : ""}
          onClick={() => setTab("add")}
        >
          ➕ Add Product
        </button>

        <button
          className={tab === "coupons" ? "active" : ""}
          onClick={() => setTab("coupons")}
        >
          🎟 Coupons
        </button>

        <button
          className={tab === "orders" ? "active" : ""}
          onClick={() => setTab("orders")}
        >
          📋 Orders
        </button>

        <button
          className={tab === "analytics" ? "active" : ""}
          onClick={() => setTab("analytics")}
        >
          📊 Analytics
        </button>
        <button onClick={() => history.push("/")}>
          🏠 Home
        </button>
      </aside>

      {/* Content */}
      <main className="seller-main">

        <div className="seller-header">
          <h1>Seller Dashboard</h1>

          <div className="seller-stats">
            <div className="stat-card">
              <h3>{products.length}</h3>
              <p>Total Products</p>
            </div>
          </div>
        </div>

        <div className="seller-body">
          {tab === "products" && (
            <>
              <h2>My Products</h2>

              <div className="seller-products-grid">
                {products.map((product) => (
                  <SellerProduct
                    key={product.id}
                    product={product}
                    refreshProducts={fetchProducts}
                  />
                ))}
              </div>
            </>
          )}

          {tab === "add" && <AddProduct />}

          {tab === "coupons" && <Coupons />}

          {tab === "orders" && (
            <Orders sellerView={true} />
          )}
          {tab === "analytics" && <SellerAnalytics />}
        </div>

      </main>
    </div>
  );
};

export default SellerCentral;