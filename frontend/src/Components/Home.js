import React, { useEffect, useState } from "react";
import "../assets/css/Home.css";
import Product from "./Product";
import instance from "../axios";
import { useLocation } from "react-router-dom/cjs/react-router-dom";

function Home() {
  const [products,setProducts] = useState([]);
  const fetchProduct = async () => {
    const data = await instance.get("/products/getTopProducts");
    setProducts(data.data);
  }
  const location = useLocation();
  useEffect(()=>{
    fetchProduct();
    if (location.state?.token) {
      console.log(location.state.token);
    }
  },[]);

  return (
    <div className='home'>
      <div className='home__container'>                
        <div className='home__row'>

          {
            products.map(product => (
                <Product
                  key={product.id}
                  id={product.id}
                  title={product.name}
                  price={product.price}
                  rating={
                    product.total_reviews > 0
                      ? Math.round(product.totalRating / product.totalReviews)
                      : 0
                  }
                  image={product.images}
                  items={product.totalSold}
                  quantity = {product.quantity}
                  description = {product.description}
                  category = {product.category}
                  tax = {product.tax}
                />

              ))
          }

        
        </div>
      </div>
    </div>
  );
}

export default Home;
