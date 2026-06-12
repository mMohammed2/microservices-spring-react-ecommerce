import React from "react";
import "../assets/css/OrderTracking.css";

const steps = [
  "PLACED",
  "PACKED",
  "SHIPPED",
  "OUT_FOR_DELIVERY",
  "DELIVERED",
];

function OrderTracking({ orderId, status }) {
  const currentIndex = steps.indexOf(status);

  return (
    <div className="tracking">

      <h3>Tracking Order #{orderId}</h3>

      <div className="tracking__steps">
        {steps.map((step, index) => (
          <div
            key={step}
            className={`tracking__step ${
              index <= currentIndex ? "active" : ""
            }`}
          >
            <div className="circle">{index + 1}</div>
            <div className="label">{step.replaceAll("_", " ")}</div>
          </div>
        ))}
      </div>

      <p className="tracking__status">
        Current Status: <strong>{status}</strong>
      </p>
    </div>
  );
}

export default OrderTracking;