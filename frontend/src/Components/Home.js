import React, { useEffect, useState } from "react";
import "../assets/css/Home.css";
import Product from "./Product";
import instance from "../axios";
import { useLocation, Link } from "react-router-dom";

function Home() {
  const [products, setProducts] = useState([]);
  const location = useLocation();

  const fetchProduct = async () => {
    try {
      const res = await instance.get("/products/getTopProducts");

      const groupedProducts = Object.values(
        res.data.reduce((acc, product) => {
          const key = `${product.name}-${product.category}`;

          if (!acc[key]) {
            acc[key] = {
              ...product,
              variants: [product],
              quantity: product.quantity || 0,
            };
          } else {
            acc[key].variants.push(product);

            acc[key].quantity += product.quantity || 0;

            acc[key].totalSold =
              (acc[key].totalSold || 0) + (product.totalSold || 0);

            acc[key].totalRating =
              (acc[key].totalRating || 0) + (product.totalRating || 0);

            acc[key].totalReviews =
              (acc[key].totalReviews || 0) + (product.totalReviews || 0);

            // Optional: lowest price among sellers
            if (product.price < acc[key].price) {
              acc[key].price = product.price;
            }
          }

          return acc;
        }, {})
      );

      setProducts(groupedProducts);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    fetchProduct();

    if (location.state?.token) {
      console.log(location.state.token);
    }
  }, []);

  return (
    <div className="home">
      <div className="home__container">
        <div className="home__row">
          {products.map((product) => (
            <Link
              key={`${product.name}-${product.category}`}
              to={{
                pathname: `/product/${product.id}`,
                state: {
                  product,
                },
              }}
              className="productLink"
              style={{
                textDecoration: "none",
                color: "inherit",
              }}
            >
              <Product
                id={product.id}
                title={product.name}
                price={product.price}
                rating={
                  product.totalReviews > 0
                    ? Math.round(
                        product.totalRating / product.totalReviews
                      )
                    : 0
                }
                image={product.images}
                items={product.totalSold}
                quantity={product.quantity}
                description={product.description}
                category={product.category}
                tax={product.tax}
              />
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Home;