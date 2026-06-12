import React, { useState } from "react";
import instance from "../axios";

const PRODUCT_CATEGORIES = [
  "Mobile Phone",
  "Laptop",
  "Tablet",
  "Desktop Computer",
  "Smart Watch",
  "Headphones",
  "Bluetooth Speaker",
  "Camera",
  "Television",
  "Gaming Console",

  "Men Clothing",
  "Women Clothing",
  "Kids Clothing",
  "Footwear",
  "Fashion Accessories",
  "Jewellery",
  "Watches",

  "Luggage",
  "Backpacks",
  "Handbags",

  "Groceries",
  "Edibles",
  "Beverages",
  "Snacks",
  "Packaged Food",

  "Home Appliances",
  "Kitchen Appliances",
  "Furniture",
  "Home Decor",
  "Lighting",

  "Books",
  "Stationery",
  "Office Supplies",

  "Beauty",
  "Personal Care",
  "Health Care",

  "Sports Equipment",
  "Fitness Equipment",
  "Outdoor Gear",

  "Toys",
  "Baby Products",
  "Pet Supplies",

  "Automotive",
  "Motorcycle Accessories",

  "Garden Supplies",
  "Tools & Hardware",

  "Software",
  "Gift Cards",
  "Other"
];

const AddProduct = () => {
  const [title, setTitle] = useState("");
  const [price, setPrice] = useState("");
  const [images, setImages] = useState("");
  const [description, setDescription] = useState("");
  const [type, setType] = useState(PRODUCT_CATEGORIES[0]);
  const [quantity, setQuantity] = useState(0);
  const [tax, setTax] = useState(0);

  const addProduct = async (e) => {
    e.preventDefault();

    if (!PRODUCT_CATEGORIES.includes(type)) {
      alert("Please select a valid category.");
      return;
    }

    try {
      const response = await instance.post(
        "/product/addProduct",
        {
          title,
          price,
          images,
          description,
          type,
          quantity,
          tax
        },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      alert(response.data.message);
      window.location.reload();
    } catch (error) {
      alert(error.response?.data?.message || error.message);
    }
  };

 return (
  <div className="add-product-container">
    <div className="add-product-card">
      <div className="add-product-header">
        <h2>Add New Product</h2>
        <p>Create a new listing for your store.</p>
      </div>

      <form onSubmit={addProduct}>
        <div className="form-grid">
          <div className="form-group">
            <label>Product Title</label>
            <input
              type="text"
              maxLength={120}
              placeholder="Enter product title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Category</label>
            <select
              value={type}
              onChange={(e) => setType(e.target.value)}
            >
              {PRODUCT_CATEGORIES.map((category) => (
                <option key={category} value={category}>
                  {category}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Price (₹)</label>
            <input
              type="number"
              min="1"
              step="0.01"
              placeholder="Product price"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Quantity</label>
            <input
              type="number"
              min="0"
              placeholder="Available quantity"
              value={quantity}
              onChange={(e) =>
                setQuantity(Number(e.target.value))
              }
              required
            />
          </div>
        </div>
        <div className="form-group">
            <label>Tax (%)</label>
            <input
              type="number"
              min="1"
              step="0.01"
              placeholder="Product tax slab"
              value={tax}
              onChange={(e) => setTax(e.target.value)}
              required
            />
          </div>

        <div className="form-group">
          <label>Description</label>

          <textarea
            rows="5"
            maxLength={1000}
            placeholder="Describe your product..."
            value={description}
            onChange={(e) =>
              setDescription(e.target.value)
            }
          />

          <small>
            {description.length}/1000 characters
          </small>
        </div>

        <div className="form-group">
          <label>Image URL</label>

          <input
            type="url"
            maxLength={255}
            placeholder="https://example.com/image.jpg"
            value={images}
            onChange={(e) => setImages(e.target.value)}
            required
          />
        </div>

        {images && (
          <div className="image-preview">
            <img
              src={images}
              alt="Preview"
              onError={(e) =>
                (e.target.style.display = "none")
              }
            />
          </div>
        )}

        <div className="info-box">
          <strong>Image Hosting Notice</strong>

          <p>
            Image uploads are currently unavailable.
            Please use a publicly accessible image URL.
          </p>

          <p>
            Supported services include Cloudinary,
            Imgur, Google Photos and similar hosting
            providers.
          </p>
        </div>

        <button
          type="submit"
          className="submit-btn"
        >
          Add Product
        </button>
      </form>
    </div>
  </div>
);
};
export default AddProduct;

