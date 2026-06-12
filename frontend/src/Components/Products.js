import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import instance from "../axios";
import Product from "./Product";
import "../assets/css/Products.css";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

const PRODUCT_CATEGORIES = [
  "Mobile Phone",
  "Laptop",
  "Tablet",
  "Desktop Computer",
  "Smart Watch",
  "Headphones",
  "Television",
  "Gaming Console",
  "Men Clothing",
  "Women Clothing",
  "Kids Clothing",
  "Groceries",
  "Books",
  "Beauty",
  "Sports Equipment",
  "Toys",
  "Automotive",
];

function useQuery() {
  return new URLSearchParams(useLocation().search);
}

const Products = () => {
  const query = useQuery();
  const search = query.get("search");
  const history = useHistory();

  const [allProducts, setAllProducts] = useState([]);
  const [displayProducts, setDisplayProducts] = useState([]);

  const [selectedCategory, setSelectedCategory] = useState("All");

  const [filters, setFilters] = useState({
    minPrice: "",
    maxPrice: "",
    minRating: "",
  });

  // -----------------------------
  // COMBINE PRODUCTS
  // -----------------------------
  const combineProducts = (data) => {
    const grouped = {};

    data.forEach((product) => {
      const key = `${(product.name || product.title)?.toLowerCase()}-${
        product.category || product.type
      }`;

      if (!grouped[key]) {
        grouped[key] = {
          ...product,
          variants: [product],
        };
      } else {
        grouped[key].variants.push(product);

        grouped[key].quantity =
          (grouped[key].quantity || 0) + (product.quantity || 0);

        grouped[key].totalSold =
          (grouped[key].totalSold || 0) + (product.totalSold || 0);

        grouped[key].totalReviews =
          (grouped[key].totalReviews || 0) + (product.totalReviews || 0);

        grouped[key].totalRating =
          (grouped[key].totalRating || 0) + (product.totalRating || 0);
      }
    });

    return Object.values(grouped);
  };

  // -----------------------------
  // FILTER METHODS
  // -----------------------------
  const filterByPrice = (data) => {
    return data.filter((p) => {
      const minOk =
        !filters.minPrice || p.price >= Number(filters.minPrice);
      const maxOk =
        !filters.maxPrice || p.price <= Number(filters.maxPrice);

      return minOk && maxOk;
    });
  };

  const filterByRating = (data) => {
    return data.filter((p) => {
      const rating =
        p.totalReviews > 0
          ? Math.round(p.totalRating / p.totalReviews)
          : 0;

      return (
        !filters.minRating || rating >= Number(filters.minRating)
      );
    });
  };

  const applyAllFilters = (data) => {
    let result = [...data];
    result = filterByPrice(result);
    result = filterByRating(result);
    return result;
  };

  // -----------------------------
  // UPDATE PRODUCTS
  // -----------------------------
  const updateProducts = (data) => {
    const combined = combineProducts(data);

    setAllProducts(combined);
    setDisplayProducts(combined);
  };

  // -----------------------------
  // AUTO FILTER (NO BUTTONS)
  // -----------------------------
  useEffect(() => {
    const filtered = applyAllFilters(allProducts);
    setDisplayProducts(filtered);
  }, [filters, allProducts]);

  // -----------------------------
  // FETCH
  // -----------------------------
  const fetchSearchProducts = async () => {
    try {
      const res = await instance.get(
        `/products/search?query=${encodeURIComponent(search)}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      updateProducts(res.data || []);
    } catch (err) {
      console.log(err);
    }
  };

  const fetchCategoryProducts = async (category) => {
    try {
      const url =
        category === "All"
          ? "/products/all"
          : `/products/category/${category}`;

      const res = await instance.get(url, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });

      updateProducts(res.data || []);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    if (search) {
      fetchSearchProducts();
    } else {
      fetchCategoryProducts("All");
    }
  }, [search]);

  const handleCategoryClick = (cat) => {
    setSelectedCategory(cat);
    fetchCategoryProducts(cat);
  };

  return (
    <div className="productsPage">

      {/* LEFT SIDEBAR */}
      <div className="sidebar">
        <h3>Categories</h3>

        <p
          className={selectedCategory === "All" ? "active" : ""}
          onClick={() => handleCategoryClick("All")}
        >
          All
        </p>

        {PRODUCT_CATEGORIES.map((cat) => (
          <p
            key={cat}
            className={selectedCategory === cat ? "active" : ""}
            onClick={() => handleCategoryClick(cat)}
          >
            {cat}
          </p>
        ))}
      </div>

      {/* CENTER PRODUCTS */}
      <div className="productsGrid">
        {displayProducts.length === 0 ? (
          <h2>No Products Found</h2>
        ) : (
          displayProducts.map((p) => (
            <>
            <Product
              key={p._id || p.id}
              id={p._id || p.id}
              title={p.name || p.title}
              image={p.images}
              price={p.price}
              rating={
                p.totalReviews > 0
                  ? Math.round(p.totalRating / p.totalReviews)
                  : 0
              }
              items={p.totalSold}
              quantity={p.quantity}
              description={p.description}
              category={p.category || p.type}
              tax={p.tax}
              onClick={() =>
                history.push({
                  pathname: `/product/${p.id}`,
                  state: { product: p },
                })
              }
            /><br/></>
          ))
        )}
      </div>

      {/* RIGHT FILTERS */}
      <div className="filtersSidebar">
        <h3>Filters</h3>

        <div className="filterGroup">
          <label>Min Price</label>
          <input
            type="number"
            value={filters.minPrice}
            onChange={(e) =>
              setFilters({ ...filters, minPrice: e.target.value })
            }
          />
        </div>

        <div className="filterGroup">
          <label>Max Price</label>
          <input
            type="number"
            value={filters.maxPrice}
            onChange={(e) =>
              setFilters({ ...filters, maxPrice: e.target.value })
            }
          />
        </div>

        <div className="filterGroup">
          <label>Min Rating</label>
          <select
            value={filters.minRating}
            onChange={(e) =>
              setFilters({ ...filters, minRating: e.target.value })
            }
          >
            <option value="">Any</option>
            <option value="1">1+</option>
            <option value="2">2+</option>
            <option value="3">3+</option>
            <option value="4">4+</option>
          </select>
        </div>
      </div>

    </div>
  );
};

export default Products;