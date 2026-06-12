import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";

function SecurePayment() {
  const location = useLocation();

  const params = new URLSearchParams(location.search);
  const method = params.get("method");
  const amount = params.get("amount");

  const [loading, setLoading] = useState(false);
  const [qrUrl, setQrUrl] = useState("");

  const [cardData, setCardData] = useState({
    cardNumber: "",
    expiryDate: "",
    cvv: "",
  });

  useEffect(() => {
    if (method === "UPI") {
      createUpiPayment();
    }
  }, [method]);

  // ---------------- UPI PAYMENT ----------------
  const createUpiPayment = async () => {
    try {
      setLoading(true);

      const response = await fetch("http://localhost:8080/api/payments/upi", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          amount: Number(amount),
        }),
      });

      // Handle non-200 responses safely
      if (!response.ok) {
        const text = await response.text();
        throw new Error(text);
      }

      const data =await response.json();
      console.log("UPI Response:", data);

      // Flexible backend response handling
      const url = data.qrcode;

      setQrUrl(url);
    } catch (err) {
      console.error("UPI payment creation failed:", err);
      alert("Failed to generate UPI QR");
    } finally {
      setLoading(false);
    }
  };

  // ---------------- CARD PAYMENT ----------------
  const handleCardPayment = async (e) => {
    e.preventDefault();

    try {
      setLoading(true);

      const response = await fetch("http://localhost:8080/api/payments/card", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          amount: Number(amount),
          cardNumber: cardData.cardNumber,
          expiryDate: cardData.expiryDate,
          cvv: cardData.cvv,
        }),
      });

      if (!response.ok) {
        const text = await response.text();
        throw new Error(text);
      }

      const data = await response.json();

      if (data === true) {
        window.opener?.postMessage(
          {
            type: "PAYMENT_SUCCESS",
            paymentMethod: "card",
            transactionId: "TXN_" + Date.now(),
          },
          window.location.origin
        );

        window.close();
      } else {
        alert("Payment failed");
      }
    } catch (err) {
      console.error("Card payment failed:", err);
      alert("Card validation failed");
    } finally {
      setLoading(false);
    }
  };

  // ---------------- UI ----------------
  return (
    <div style={{ padding: 30 }}>
      <h2>Secure Payment</h2>
      <p>Amount: ₹{amount}</p>

      {/* ---------------- UPI ---------------- */}
      {method === "UPI" ? (
        <>
          <h3>UPI Payment</h3>

          {loading && <p>Generating QR...</p>}

          {qrUrl && (
            <div>
              <img
                src={qrUrl}                
              />
              <br></br>
              <button
                style={{ marginTop: 20 }}
                onClick={() => {
                  window.opener?.postMessage(
                    {
                      type: "PAYMENT_SUCCESS",
                      paymentMethod: "upi",
                      transactionId: "TXN_" + Date.now(),
                    },
                    window.location.origin
                  );

                  window.close();
                }}
              >
                Simulate Payment Success
              </button>
            </div>
          )}
        </>
      ) : (
        /* ---------------- CARD ---------------- */
        <>
          <h3>Card Payment</h3>

          <form onSubmit={handleCardPayment}>
            <input
              type="text"
              placeholder="Card Number"
              value={cardData.cardNumber}
              onChange={(e) =>
                setCardData({ ...cardData, cardNumber: e.target.value })
              }
              required
            />

            <div style={{ marginTop: 10 }}>
              <input
                type="text"
                placeholder="MM/YY"
                value={cardData.expiryDate}
                onChange={(e) =>
                  setCardData({ ...cardData, expiryDate: e.target.value })
                }
                required
              />
            </div>

            <div style={{ marginTop: 10 }}>
              <input
                type="password"
                placeholder="CVV"
                maxLength={4}
                value={cardData.cvv}
                onChange={(e) =>
                  setCardData({ ...cardData, cvv: e.target.value })
                }
                required
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              style={{ marginTop: 20 }}
            >
              {loading ? "Processing..." : "Pay Now"}
            </button>
          </form>
        </>
      )}
    </div>
  );
}

export default SecurePayment