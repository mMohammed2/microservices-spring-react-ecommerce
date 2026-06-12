import React, { useEffect, useState } from "react";
import { useParams, useLocation } from "react-router-dom";
import instance from "../axios";
import "../assets/css/ProductDetail.css";

function ProductDetail() {
  const { id } = useParams();
  const location = useLocation();

  const storageKey = `product_${id}`;

  const [product, setProduct] = useState(
    location.state?.product ||
      JSON.parse(localStorage.getItem(storageKey)) ||
      null
  );

  const [selectedVariant, setSelectedVariant] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const [reviews, setReviews] = useState([]);

  // Save product from navigation
  useEffect(() => {
    if (location.state?.product) {
      localStorage.setItem(storageKey, JSON.stringify(location.state.product));
      setProduct(location.state.product);
    }
  }, [location.state, storageKey]);

  // Initialize variant when product loads
  useEffect(() => {
    if (product) {
      const defaultVariant = product.variants?.[0] || product;
      setSelectedVariant(defaultVariant);
      setQuantity(1);
    }
  }, [product]);

  // Fetch reviews
  useEffect(() => {
    const fetchReviews = async () => {
      try {
        const res = await instance.get(`/reviews/get/${id}`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });

        setReviews(res.data || []);
      } catch (err) {
        console.log(err);
      }
    };

    fetchReviews();
  }, [id]);

  if (!product) {
    return <div className="loading">Product not available</div>;
  }

  const maxQty = selectedVariant?.quantity ?? product.quantity ?? 0;

  // ADD TO CART
  const addToCart = async (qty = 1) => {
    try {
      await instance.post(
        `/cart/add/${product._id}`,
        {
          variantId: selectedVariant?._id,
          quantity: qty,
        },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );
    } catch (err) {
      console.log(err);
      alert("Failed to update cart");
    }
  };

  return (
    <div className="productPage">
      <div className="productPage__container">

        {/* IMAGE */}
        <div className="productPage__imageBox">
          <img src={product.images} alt={product.name} />
        </div>

        {/* INFO */}
        <div className="productPage__infoBox">

          <h1 className="productTitle">{product.name}</h1>

          <p className="productCategory">
            Category: <span>{product.category}</span>
          </p>

          <div className="productRating">
            ⭐{" "}
            {product.totalReviews > 0
              ? Math.round(product.totalRating / product.totalReviews)
              : 0}
            <span>({product.totalReviews} reviews)</span>
          </div>

          <h2 className="productPrice">
            ₹{selectedVariant?.price || product.price}
          </h2>

          <p className="productDesc">{product.description}</p>

          <div className="productMeta">
            <p>📦 Stock: {maxQty}</p>
            <p>🏷️ Tax: {product.tax}%</p>
          </div>

          {/* VARIANTS */}
          {product.variants?.length > 1 && (
            <div className="variantDropdownBox">
              <h3>Select Seller / Option</h3>

              <select
                className="variantDropdown"
                value={selectedVariant?._id}
                onChange={(e) => {
                  const selected = product.variants.find(
                    (v) => v._id === e.target.value
                  );

                  setSelectedVariant(selected);
                  setQuantity(0);
                }}
              >
                {product.variants.map((v) => (
                  <option key={v._id} value={v._id}>
                    {v.sellerName || "Seller"} - ₹{v.price} - Stock:{" "}
                    {v.quantity}
                  </option>
                ))}
              </select>
            </div>
          )}

          {/* QUANTITY */}
          <div className="qtyBox">

            <button
              className="qtyBtn"
              onClick={async () => {
                if (quantity > 1) {
                  const newQty = quantity - 1;
                  setQuantity(newQty);
                  await addToCart(-1);
                }
              }}
              disabled={quantity <= 1 }
            >
              -
            </button>

            <span className="qtyValue">{quantity}</span>

            <button
              className="qtyBtn"
              onClick={async () => {
                if (quantity < maxQty) {
                  const newQty = quantity + 1;
                  setQuantity(newQty);
                  await addToCart(1);
                }
              }}
              disabled={quantity >= maxQty}
            >
              +
            </button>

          </div>

        </div>
      </div>

      {/* REVIEWS */}
      <div className="reviewsSection">

        <h2>Customer Reviews</h2>

        {reviews.length === 0 ? (
          <p className="noReviews">No reviews yet</p>
        ) : (
          reviews.map((r) => (
            <div key={r._id} className="reviewCard">

              <div className="reviewHeader">
                <span className="reviewRating">⭐ {r.stars}</span>
              </div>

              <p className="reviewComment">{r.description}</p>

            </div>
          ))
        )}

      </div>
    </div>
  );
}

export default ProductDetail;