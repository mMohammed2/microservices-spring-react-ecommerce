import React, { useState,useEffect } from "react";
import moment from "moment";
import CurrencyFormat from "react-currency-format";
import OrderTracking from "./OrderTracking";
import "../assets/css/Order.css";
import instance from "../axios";
import OrderProduct from "./OrderProduct";
import { ControlPointSharp } from "@material-ui/icons";

function Order({
  order,
  refreshOrders,
  sellerView = false,
}) {
  const [showTracking, setShowTracking] =
    useState(false);
    const [returnedProducts, setReturnedProducts] = useState({});
  const [loading, setLoading] =
    useState(false);
  const [showReviewModal, setShowReviewModal] =
    useState(false);
  const [reviews, setReviews] = useState({});
  const [selectedProduct, setSelectedProduct] =
    useState(null);
  const [rating, setRating] =
    useState(0);
  const [reviewText, setReviewText] =
    useState("");

  const [reviewLoading, setReviewLoading] =
    useState(false);
  const canCancel =
    order.status === "PLACED" ||
    order.status === "PACKED";

  const cancelOrder = async () => {
    try {
      setLoading(true);

      await instance.post(
        `/orders/${order.id}/cancel`,
        {},
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem(
              "token"
            )}`,
          },
        }
      );

      refreshOrders?.();
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const updatePackageStatus = async (
    status
  ) => {
    try {
      setLoading(true);

      await instance.put(
        `/orders/package/${order.id}/status`,
        {status },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem(
              "token"
            )}`,
          },
        }
      );

      refreshOrders?.();
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const updatePaymentStatus = async (
    paymentStatus
  ) => {
    try {
      setLoading(true);

      await instance.put(
        `/orders/package/${order.id}/payment-status`,
        { "status": paymentStatus },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem(
              "token"
            )}`,
          },
        }
      );

      refreshOrders?.();
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const cancelPackage = async () => {
    try {
      setLoading(true);

      await instance.post(
        `/orders/package/${order.id}/cancel`,
        {},
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem(
              "token"
            )}`,
          },
        }
      );

      refreshOrders?.();
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };
  const submitReview = async () => {
    try {
      setReviewLoading(true);
      await instance.post(
        "/reviews/add",
        {
          productId: selectedProduct.productId,
          rating,
          review: reviewText,
        },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem(
              "token"
            )}`,
          },
        }
      );
      setReviews((prev) => ({
        ...prev,
        [selectedProduct.productId]: {
          productId:
            selectedProduct.productId,
          rating,
          review: reviewText,
        },
      }));
      setSelectedProduct(null);

      alert("Review submitted");

      setShowReviewModal(false);
      setRating(0);
      setReviewText("");

      refreshOrders?.();
    } catch (error) {
      console.error(error);
    } finally {
      setReviewLoading(false);
    }
  };
  const statusFlow = {
  PLACED: [
    "PLACED",
    "PACKED",
    "SHIPPED",
    "OUT_FOR_DELIVERY",
    "DELIVERED",
  ],

  PACKED: [
    "PACKED",
    "SHIPPED",
    "OUT_FOR_DELIVERY",
    "DELIVERED",
  ],

  SHIPPED: [
    "SHIPPED",
    "OUT_FOR_DELIVERY",
    "DELIVERED",
  ],

  OUT_FOR_DELIVERY: [
    "OUT_FOR_DELIVERY",
    "DELIVERED",
  ],

  DELIVERED: [
    "DELIVERED",
  ],

  CANCELLED: [
    "CANCELLED",
  ],
};

const availableStatuses =
  statusFlow[order.status] || [
    order.status,
  ];
const requestReturn = async (productId) => {
  if (returnedProducts[productId]) {
    alert("Already returned");
    return;
  }

  try {
    setLoading(true);

    await instance.post(
      `/orders/return/${order.id}/${productId}`,
      {},
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );

    setReturnedProducts((prev) => ({
      ...prev,
      [productId]: true,
    }));

    alert("Return request submitted");
    refreshOrders?.();
  } catch (error) {
    console.error(error);
  } finally {
    setLoading(false);
  }
};
  const availablePaymentStatuses =
    order.paymentStatus === "PAID"
      ? ["PAID"]
      : ["UNPAID", "PAID"];

    useEffect(() => {
      const fetchReviews = async () => {
        try {
          const reviewMap = {};

          await Promise.all(
            order.item.map(async (item) => {
              try {
                const res = await instance.get(
                  `/reviews/order/${item.productId}`,
                  {
                    headers: {
                      Authorization: `Bearer ${localStorage.getItem("token")}`,
                    },
                  }
                );

                if (res.data) {
                  reviewMap[item.productId] = res.data;
                }
              } catch (err) {
                // ignore 404 (no review yet)
              }
            })
          );

          setReviews(reviewMap);
        } catch (err) {
          console.error(err);
        }
      };

      if (order.status === "DELIVERED") {
        fetchReviews();
      }
    }, [order.id, order.status]);

    useEffect(() => {
  const fetchReturns = async () => {
    try {
      const res = await instance.get("/orders/return/my", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });

      const map = {};
      res.data.forEach((item) => {
        map[item.productId] = true;
      });

      setReturnedProducts(map);
    } catch (err) {
      console.error(err);
    }
  };

  fetchReturns();
}, []);

  return (
    <div className="order">

      <div className="order__header">
        <h2>
          Order #{order.id}
        </h2>

        <span
          className={`status status-${order.status}`}
        >
          {order.status}
        </span>
      </div>

      <p>
        {moment(
          order.createdAt ||
            order.date
        ).format(
          "MMMM Do YYYY, h:mm a"
        )}
      </p>

      <p>
        <strong>Payment:</strong>{" "}
        {order.paymentMethod}
      </p>

      <p>
        <strong>
          Payment Status:
        </strong>{" "}
        {order.paymentStatus}
      </p>

      <p>
        <strong>Address:</strong>{" "}
        {order.address}
      </p>

      {sellerView && (
        <div
          className="seller-package-controls"
          style={{
            margin: "15px 0",
            padding: "15px",
            border: "1px solid #ddd",
            borderRadius: "8px",
          }}
        >
          <h3>
            Package Controls
          </h3>

          <div
            style={{
              marginBottom: "10px",
            }}
          >
            <label>
              Package Status:
            </label>

            <select
              value={order.status}
              onChange={(e) =>
                updatePackageStatus(
                  e.target.value
                )
              }
              disabled={
                loading ||
                order.status === "DELIVERED" ||
                order.status === "CANCELLED"
              }
            >
              {availableStatuses.map(
                (status) => (
                  <option
                    key={status}
                    value={status}
                  >
                    {status}
                  </option>
                )
              )}
            </select>


          </div>

          {order.paymentMethod ===
            "COD" && (
            <div
              style={{
                marginBottom:
                  "10px",
              }}
            >
              <label>
                Payment Status:
              </label>

              <select
                value={order.paymentStatus}
                onChange={(e) =>
                  updatePaymentStatus(
                    e.target.value
                  )
                }
                disabled={
                  loading ||
                  order.paymentStatus === "PAID" ||
                  order.status !== "DELIVERED"
                }
              >
                {availablePaymentStatuses.map(
                  (status) => (
                    <option
                      key={status}
                      value={status}
                    >
                      {status}
                    </option>
                  )
                )}
              </select>


            </div>
          )}

          {order.status !==
            "CANCELLED" && (
            <button
              className="cancel"
              onClick={
                cancelPackage
              }
              disabled={
                loading
              }
            >
              {loading
                ? "Cancelling..."
                : "Cancel Package"}
            </button>
          )}
        </div>
      )}

      <div className="order__items">
        {order.item?.map(
          (item) => {
            const existingReview =
              reviews[item.productId];

              const canReturnItem =
              order.status === "DELIVERED" &&
              order.delivered &&
              moment().diff(
                moment(order.delivered),
                "days"
              ) <= 7;
            const itemTotal =
              item.price *
              item.quantity;

            const discount =
              (itemTotal *
                (item.couponDiscountPercent ||
                  0)) /
              100;

            const final =
              (itemTotal -
                discount) *
              (1 +
                (item.tax ||
                  0) /
                  100);

            return (
              <div
                key={
                  item.productId
                }
                className="order__item"
              >
                <OrderProduct
                  id={
                    item.productId
                  }
                  title={
                    item.title
                  }
                  image={
                    item.image
                  }
                  price={
                    item.price
                  }
                  quantity={
                    item.quantity
                  }
                  tax={
                    item.tax
                  }
                  hideButton
                />

                <div className="order__breakdown">
                  <p>
                    Price: ₹
                    {
                      item.price
                    }
                    {" × "}
                    {
                      item.quantity
                    }
                    {" = ₹"}
                    {
                      itemTotal
                    }
                  </p>

                  {item.couponCode && (
                    <p className="order__discount">
                      Coupon (
                      {
                        item.couponCode
                      }
                      {" - "}
                      {
                        item.couponDiscountPercent
                      }
                      %): -₹
                      {discount.toFixed(
                        2
                      )}
                    </p>
                  )}

                  <p className="order__final">
                    Final: ₹
                    {final.toFixed(
                      2
                    )}
                  </p>
                  <div
                    style={{
                      marginTop: "10px",
                      display: "flex",
                      gap: "10px",
                      flexWrap: "wrap",
                    }}
                  >
                    {!sellerView &&
                      !existingReview &&
                      order.status ===
                        "DELIVERED" && (
                        <button
                          className="review"
                          onClick={() => {
                            setSelectedProduct(item);
                            setShowReviewModal(true);
                          }}
                        >
                          ⭐ Write Review
                        </button>
                      )}
                      {existingReview && (
                            <div style= {{padding:"4px",margin:"2px",background:"#cbcbcb",borderRadius:"5px"}} className="order__review">
                              <p>
                                ⭐ Rating: {" "}
                                {Array(existingReview.stars)
                                  .fill("★")
                                  .join("")}
                              </p>

                              <p>
                                📝 {existingReview.description}
                              </p>
                            </div>
                          )}
                   {!sellerView && canReturnItem && (
                        returnedProducts[item.productId] ? (
                          <div
                            style={{
                              padding: "4px",
                              margin: "2px",
                              background: "#e6ffe6",
                              borderRadius: "5px",
                              fontSize: "12px",
                            }}
                          >
                            ↩ Returned
                          </div>
                        ) : (
                          <button
                            className="return"
                            onClick={() => requestReturn(item.productId)}
                          >
                            ↩ Return Product
                          </button>
                        )
                      )}
                  </div>
                </div>
              </div>
            );
          }
        )}
        
      </div>

      <hr />

      <div className="order__totals">
        <p>
          Subtotal: ₹
          {order.subtotal?.toFixed(
            2
          )}
        </p>

        <p>
          Tax: ₹
          {order.tax?.toFixed(
            2
          )}
        </p>

        <p>
          Discount: ₹
          {order.discount?.toFixed(
            2
          )}
        </p>

        <CurrencyFormat
          value={order.total}
          displayType="text"
          thousandSeparator
          prefix="₹"
          renderText={(
            value
          ) => (
            <h3>
              Order Total:{" "}
              {value}
            </h3>
          )}
        />
      </div>

      <div className="order__actions">

        <button
          onClick={() =>
            setShowTracking(
              (v) => !v
            )
          }
        >
          {showTracking
            ? "Hide Tracking"
            : "Track Order"}
        </button>

        {!sellerView &&
          canCancel && (
            <button
              className="cancel"
              onClick={
                cancelOrder
              }
              disabled={
                loading
              }
            >
              {loading
                ? "Cancelling..."
                : "Cancel Order"}
            </button>
          )}

      </div>

      {showTracking && (
        <OrderTracking
          orderId={order.id}
          status={
            order.status
          }
        />
      )}
      {showReviewModal && (
      <div className="review-modal-overlay">
        <div className="review-modal">

          <h2>
            Review
            {selectedProduct &&
              ` - ${selectedProduct.title}`}
          </h2>
          <div className="star-rating">
            {[1, 2, 3, 4, 5].map(
              (star) => (
                <span
                  key={star}
                  onClick={() =>
                    setRating(star)
                  }
                  className={
                    star <= rating
                      ? "star active"
                      : "star"
                  }
                >
                  ★
                </span>
              )
            )}
          </div>

          <textarea
            placeholder="Write your review..."
            value={reviewText}
            onChange={(e) =>
              setReviewText(
                e.target.value
              )
            }
          />

          <div className="review-actions">
            <button
              onClick={() => {
                setShowReviewModal(false);
                setSelectedProduct(null);
                setRating(0);
                setReviewText("");
              }}
            >
              Cancel
            </button>

            <button
              onClick={submitReview}
              disabled={
                rating === 0 ||
                reviewLoading
              }
            >
              {reviewLoading
                ? "Submitting..."
                : "Submit"}
            </button>
          </div>

        </div>
      </div>
    )}
    </div>
  );
}

export default Order;