import React, { useEffect, useMemo, useState } from "react";
import { useLocation } from "react-router-dom";
import instance from "../axios";
import Product from "./Product";
import "../assets/css/Products.css";
import Fuse from "fuse.js";
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

const Products = () => {
  const location = useLocation();
  const history = useHistory();

  const [allProducts, setAllProducts] = useState([]);
  const [displayProducts, setDisplayProducts] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState("All");

  const [filters, setFilters] = useState({
    minPrice: "",
    maxPrice: "",
    minRating: "",
  });

  const [sortBy, setSortBy] = useState("");

  // ✅ SAFE SEARCH PARAM
  const searchParam = useMemo(() => {
    const params = new URLSearchParams(location.search);
    return params.get("search") || "";
  }, [location.search]);

  // -----------------------------
  // COMBINE PRODUCTS
  // -----------------------------
  const combineProducts = (data) => {
    const grouped = {};

    data.forEach((product) => {
      const key = `${(product.name || "").toLowerCase()}-${product.category}`;

      if (!grouped[key]) {
        grouped[key] = {
          ...product,
          variants: [product],
        };
      } else {
        grouped[key].variants.push(product);
      }
    });

    return Object.values(grouped);
  };

  // -----------------------------
  // FETCH PRODUCTS
  // -----------------------------
  const fetchProducts = async () => {
    try {
      const res = await instance.get("/products/all", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });

      const combined = combineProducts(res.data || []);
      setAllProducts(combined);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  // -----------------------------
  // FUSE INDEX
  // -----------------------------
  const fuse = useMemo(() => {
    if (!allProducts.length) return null;

    return new Fuse(allProducts, {
    keys: [
      { name: "name", weight: 1 },
      { name: "category", weight: 0.2 },
    ],
      threshold: 0.6,
      ignoreLocation: true,
      findAllMatches: true,
      useExtendedSearch: true,
    });
  }, [allProducts]);

  // -----------------------------
  // FILTERS
  // -----------------------------
  const applyCategory = (data) => {
    if (selectedCategory === "All") return data;
    return data.filter((p) => p.category === selectedCategory);
  };

  const applySearch = (data) => {
    if (!searchParam.trim() || !fuse) return data;

    const query = String(searchParam).trim();

    const results = fuse.search(query);

    return results
      .sort((a, b) => a.score - b.score)
      .slice(0, 20)
      .map(r => r.item);
  };

  const applyFilters = (data) => {
    return data.filter((p) => {
      const minOk = !filters.minPrice || p.price >= Number(filters.minPrice);
      const maxOk = !filters.maxPrice || p.price <= Number(filters.maxPrice);

      const rating =
        p.totalReviews > 0
          ? Math.round(p.totalRating / p.totalReviews)
          : 0;

      const ratingOk =
        !filters.minRating || rating >= Number(filters.minRating);

      return minOk && maxOk && ratingOk;
    });
  };

  const applySort = (data) => {
    let sorted = [...data];

    switch (sortBy) {
      case "price_low":
        sorted.sort((a, b) => a.price - b.price);
        break;

      case "price_high":
        sorted.sort((a, b) => b.price - a.price);
        break;

      case "rating_high":
        sorted.sort(
          (a, b) =>
            (b.totalRating || 0) / (b.totalReviews || 1) -
            (a.totalRating || 0) / (a.totalReviews || 1)
        );
        break;

      case "popular":
        sorted.sort((a, b) => (b.totalSold || 0) - (a.totalSold || 0));
        break;

      default:
        break;
    }

    return sorted;
  };

  // -----------------------------
  // PIPELINE
  // -----------------------------
  useEffect(() => {
    let result = [...allProducts];

    result = applyCategory(result);
    result = applySearch(result);
    result = applyFilters(result);
    result = applySort(result);

    setDisplayProducts(result);
  }, [allProducts, selectedCategory, filters, sortBy, searchParam]);

  // -----------------------------
  // CATEGORY CLICK
  // -----------------------------
  const handleCategoryClick = (cat) => {
    history.push(`/products?category=${encodeURIComponent(cat)}`);
  };

  // -----------------------------
  // UI
  // -----------------------------
  return (
    <div className="productsPage">

      {/* LEFT */}
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

      {/* PRODUCTS */}
      <div className="productsGrid">
        {displayProducts.length === 0 ? (
          <h2>No Products Found</h2>
        ) : (
          displayProducts.map((p) => (
            <Product
              key={p.id}
              id={p.id}
              title={p.name}
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
              category={p.category}
              tax={p.tax}
              onClick={() =>
                history.push({
                  pathname: `/product/${p.id}`,
                  state: { product: p },
                })
              }
            />
          ))
        )}
      </div>

      {/* RIGHT FILTERS */}
      <div className="filtersSidebar">
        <h3>Sort By</h3>

        <select value={sortBy} onChange={(e) => setSortBy(e.target.value)}>
          <option value="">Default</option>
          <option value="price_low">Price: Low → High</option>
          <option value="price_high">Price: High → Low</option>
          <option value="rating_high">Rating: High → Low</option>
          <option value="popular">Popularity</option>
        </select>

        <h3>Filters</h3>

        <input
          type="number"
          placeholder="Min Price"
          onChange={(e) =>
            setFilters({ ...filters, minPrice: e.target.value })
          }
        />

        <input
          type="number"
          placeholder="Max Price"
          onChange={(e) =>
            setFilters({ ...filters, maxPrice: e.target.value })
          }
        />

        <select
          onChange={(e) =>
            setFilters({ ...filters, minRating: e.target.value })
          }
        >
          <option value="">Min Rating</option>
          <option value="1">1+</option>
          <option value="2">2+</option>
          <option value="3">3+</option>
          <option value="4">4+</option>
        </select>
      </div>
    </div>
  );
};

export default Products;