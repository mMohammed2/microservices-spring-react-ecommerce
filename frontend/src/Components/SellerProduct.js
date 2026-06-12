
import React, { useState } from "react";
import instance from "../axios";
import "../assets/css/Product.css";

function SellerProduct({ product, refreshProducts }) {
    const [editing, setEditing] = useState(false);
    const [formData, setFormData] = useState({
        ...product
    });

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const saveProduct = async () => {
        try {
            await instance.put(
                `/products/${product.id}`,
                formData,
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("token")}`
                    }
                }
            );

            alert("Product updated");
            setEditing(false);

            if (refreshProducts) {
                refreshProducts();
            }

        } catch (err) {
            console.error(err);
            alert("Failed to update product");
        }
    };

    return (
        <div className="product">

            {editing ? (
                <>
                    <input
                        name="title"
                        value={formData.title}
                        onChange={handleChange}
                        placeholder="Product Name"
                    />

                    <textarea
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        placeholder="Description"
                    />

                    <input
                        name="images"
                        value={formData.images}
                        onChange={handleChange}
                        placeholder="Image URL"
                    />

                    <input
                        type="number"
                        name="price"
                        value={formData.price}
                        onChange={handleChange}
                    />

                    <input
                        type="number"
                        name="quantity"
                        value={formData.quantity}
                        onChange={handleChange}
                    />

                    <div style={{ marginTop: "10px" }}>
                        <button onClick={saveProduct}>
                            Save
                        </button>

                        <button
                            onClick={() => {
                                setEditing(false);
                                setFormData({ ...product });
                            }}
                        >
                            Cancel
                        </button>
                    </div>
                </>
            ) : (
                <>
                    <div className="product__info">
                        <h3>{product.name}</h3>

                        <p>{product.description}</p>

                        <p>
                            <strong>₹ {product.price}</strong>
                        </p>

                        <p>
                            Sold: {product.totalSold}
                        </p>

                        <p>
                            Available: {product.quantity}
                        </p>
                    </div>

                    <img
                        src={product.images}
                        alt={product.name}
                        style={{ margin: "10px" }}
                    />

                    <button onClick={() => setEditing(true)}>
                        Edit Listing
                    </button>
                </>
            )}

        </div>
    );
}

export default SellerProduct;