import React, { useEffect, useMemo, useState } from "react";
import instance from "../axios";

const Coupons = () => {
  
  const [coupons, setCoupons] = useState([]);
  const [products, setProducts] = useState([]);

  const [code, setCode] = useState("");
  const [discount, setDiscount] = useState("");
  const [allowed, setAllowed] = useState(0);
  const [expiry, setExpiry] = useState("");
  const [allowedProducts, setAllowedProducts] = useState([]);

  const [editingId, setEditingId] = useState(null);
  const [editData, setEditData] = useState({
    code: "",
    discount: "",
    allowed: 0,
    expiry: "",
    allowedProducts: [],
  });

  const [sortConfig, setSortConfig] = useState({
    key: "code",
    direction: "asc",
  });

  const token = localStorage.getItem("token");

  const today = new Date().toISOString().split("T")[0];
  const isExpired = expiry && expiry < today;

  const fetchCoupons = async () => {
    const res = await instance.get("/seller/coupons", {
      headers: { Authorization: `Bearer ${token}` },
    });
    setCoupons(res.data);
  };

  const fetchProducts = async () => {
    const res = await instance.get("/products/seller", {
      headers: { Authorization: `Bearer ${token}` },
    });
    setProducts(res.data);
  };

  useEffect(() => {
    fetchCoupons();
    fetchProducts();
  }, []);

  const createCoupon = async () => {
    if (
      !code ||
      discount < 1 ||
      discount > 100 ||
      allowed < 0 ||
      !expiry ||
      isExpired
    ) {
      alert("Invalid coupon data");
      return;
    }

    await instance.post(
      "/seller/addCoupon",
      {
        code: code.trim().toUpperCase(),
        discount: Number(discount),
        allowed: Number(allowed),
        expiry,
        allowedProducts,
      },
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );

    setCode("");
    setDiscount("");
    setAllowed(0);
    setExpiry("");
    setAllowedProducts([]);

    fetchCoupons();
  };
  const deleteCoupon = async (id) => {
    if (!window.confirm("Delete this coupon?")) return;

    await instance.delete(`/seller/deleteCoupon/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    fetchCoupons();
  };

  const startEdit = (c) => {
    setEditingId(c.id);
    setEditData({
      code: c.code || "",
      discount: c.discount || "",
      allowed: c.allowed || 0,
      expiry: c.expiry
        ? new Date(c.expiry).toISOString().split("T")[0]
        : "",
      allowedProducts: c.allowedProducts?.map((p) => p.id) || [],
    });
  };

  const updateCoupon = async () => {
    await instance.put(
      `/seller/updateCoupon/${editingId}`,
      {
        code: editData.code.trim().toUpperCase(),
        discount: Number(editData.discount),
        allowed: Number(editData.allowed),
        expiry: editData.expiry,
        allowedProducts: editData.allowedProducts,
      },
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );

    setEditingId(null);
    fetchCoupons();
  };

  const handleSort = (key) => {
    setSortConfig((prev) => ({
      key,
      direction:
        prev.key === key && prev.direction === "asc"
          ? "desc"
          : "asc",
    }));
  };

  const sortedCoupons = useMemo(() => {
    const sorted = [...coupons];

    sorted.sort((a, b) => {
      const key = sortConfig.key;

      let valA = a[key];
      let valB = b[key];

      if (key === "expiry") {
        valA = valA ? new Date(valA).getTime() : 0;
        valB = valB ? new Date(valB).getTime() : 0;
      }

      if (typeof valA === "string") {
        valA = valA.toLowerCase();
        valB = (valB || "").toLowerCase();
      }

      if (valA < valB)
        return sortConfig.direction === "asc" ? -1 : 1;
      if (valA > valB)
        return sortConfig.direction === "asc" ? 1 : -1;

      return 0;
    });

    return sorted;
  }, [coupons, sortConfig]);

  return (
    <div style={{ padding: "20px" }}>
      <h2>Coupons</h2>

      {/* CREATE */}
      <div style={{ marginBottom: "20px" }}>
        <h3>Create Coupon</h3>

        <input
          placeholder="Code"
          value={code}
          onChange={(e) =>
            setCode(
              e.target.value
                .toUpperCase()
                .replace(/[^A-Z0-9]/g, "")
            )
          }
        />

        <input
          type="number"
          placeholder="Discount"
          value={discount}
          onChange={(e) => setDiscount(e.target.value)}
        />

        <input
          type="number"
          placeholder="Allowed"
          value={allowed}
          onChange={(e) => setAllowed(e.target.value)}
        />

        <input
          type="date"
          min={today}
          value={expiry}
          onChange={(e) => setExpiry(e.target.value)}
        />

        <div
          style={{
            maxHeight: "150px",
            overflowY: "auto",
            border: "1px solid #ddd",
            padding: "10px",
            marginTop: "10px",
          }}
        >
          {products.map((p) => (
            <label key={p.id} style={{ display: "block" }}>
              <input
                type="checkbox"
                checked={allowedProducts.includes(p.id)}
                onChange={(e) => {
                  if (e.target.checked) {
                    setAllowedProducts([...allowedProducts, p.id]);
                  } else {
                    setAllowedProducts(
                      allowedProducts.filter((id) => id !== p.id)
                    );
                  }
                }}
              />
              {" " + p.name}
            </label>
          ))}
        </div>

        <button onClick={createCoupon} disabled={isExpired}>
          Create
        </button>
      </div>

      {/* TABLE */}
      <table
        border="1"
        cellPadding="10"
        style={{ width: "100%", borderCollapse: "collapse" }}
      >
        <thead>
          <tr>
            <th onClick={() => handleSort("code")}>Code</th>
            <th onClick={() => handleSort("discount")}>Discount</th>
            <th onClick={() => handleSort("allowed")}>Allowed</th>
            <th onClick={() => handleSort("used")}>Used</th>
            <th onClick={() => handleSort("expiry")}>Expiry</th>
            <th>Products</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {sortedCoupons.map((c) => (
            <tr key={c.id}>
              {editingId === c.id ? (
                <>
                  <td>
                    <input
                      value={editData.code}
                      onChange={(e) =>
                        setEditData({
                          ...editData,
                          code: e.target.value,
                        })
                      }
                    />
                  </td>

                  <td>
                    <input
                        type="number"
                        min={1}
                        max={100}
                        value={editData.discount}
                        onChange={(e) => {
                          let val = e.target.value;

                          if (val === "") {
                            setEditData({ ...editData, discount: val });
                            return;
                          }

                          val = Math.max(1, Math.min(100, Number(val)));

                          setEditData({ ...editData, discount: val });
                        }}
                      />
                  </td>

                  <td>
                    <input
                      type="number"
                      value={editData.allowed}
                      onChange={(e) =>
                        setEditData({
                          ...editData,
                          allowed: e.target.value,
                        })
                      }
                    />
                  </td>

                  <td>{c.used}</td>

                  <td>
                    <input
                      type="date"
                      min={today}
                      value={editData.expiry}
                      onChange={(e) =>
                        setEditData({
                          ...editData,
                          expiry: e.target.value,
                        })
                      }
                    />
                  </td>

                  <td>
                    <button onClick={updateCoupon}>Save</button>
                    <button onClick={() => setEditingId(null)}>
                      Cancel
                    </button>
                  </td>
                </>
              ) : (
                <>
                  <td>{c.code}</td>
                  <td>{c.discount}%</td>
                  <td>{c.allowed}</td>
                  <td>{c.used}</td>
                  <td>
                    {c.expiry
                      ? new Date(c.expiry).toLocaleDateString()
                      : "-"}
                  </td>
                  <td>
                    {c.allowedProducts?.length
                      ? c.allowedProducts
                          .join(", ")
                      : "All"}
                  </td>

                  <td>
                    <button onClick={() => startEdit(c)}>
                      Edit
                    </button>
                    <button onClick={() => deleteCoupon(c.id)}>
                      Delete
                    </button>
                  </td>
                </>
              )}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Coupons;