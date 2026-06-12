import React, { useEffect, useState, useRef } from 'react';
import instance from '../axios';
import { useHistory } from 'react-router-dom';

const User = () => {
  const history = useHistory();

  const [user, setUser] = useState(null);
  const [addresses, setAddresses] = useState([""]);
  const [passwordData, setPasswordData] = useState({
    oldPassword: "",
    newPassword: ""
  });

  const token = localStorage.getItem("token");

  // ✅ debounce ref
  const saveTimeout = useRef(null);

  // ================= GET USER =================
  const getUser = async () => {
    try {
      const response = await instance.get('/users/getUser', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      setUser(response.data);

      if (Array.isArray(response.data?.address)) {
        setAddresses(response.data.address.length ? response.data.address : [""]);
      } else {
        setAddresses([""]);
      }

    } catch (error) {
      alert(error.response?.data?.message || "Session timed out, Please login again");

      localStorage.removeItem("token");
      localStorage.removeItem("username");
      localStorage.removeItem("portiire");

      window.location.href = "/";
    }
  };

  // ================= ADDRESS HANDLER =================
  const handleAddressChange = (index, value) => {
    const updated = [...addresses];
    updated[index] = value;
    setAddresses(updated);
  };

  // ================= ADD ADDRESS (MAX 3) =================
  const addAddressField = () => {
    if (addresses.length >= 3) {
      alert("You can only add up to 3 addresses");
      return;
    }
    setAddresses([...addresses, ""]);
  };

  // ================= REMOVE ADDRESS =================
  const removeAddressField = (index) => {
    const updated = addresses.filter((_, i) => i !== index);
    setAddresses(updated.length ? updated : [""]);
  };

  // ================= AUTO SAVE =================
  const autoSaveAddresses = () => {
    if (!user) return;

    if (saveTimeout.current) {
      clearTimeout(saveTimeout.current);
    }

    saveTimeout.current = setTimeout(async () => {
      try {
        await instance.post(
          "/users/addAddress",
          {
            address: addresses.filter(a => a.trim() !== "")
          },
          {
            headers: {
              Authorization: `Bearer ${token}`
            }
          }
        )
      } catch (error) {
      }
    }, 800);
  };

  // ================= PASSWORD HANDLERS =================
  const handlePasswordChange = (e) => {
    setPasswordData({
      ...passwordData,
      [e.target.name]: e.target.value
    });
  };

  const changePassword = async () => {
    try {
      await instance.post(
        "/auth/changePassword",
        {
          oldPassword: passwordData.oldPassword,
          newPassword: passwordData.newPassword
        },
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );

      alert("Password changed successfully");

      setPasswordData({
        oldPassword: "",
        newPassword: ""
      });

    } catch (error) {
      alert(error.response?.data?.message || "Failed to change password");
    }
  };

  // ================= EFFECTS =================
  useEffect(() => {
    getUser();
  }, []);

  // ✅ auto-save on address change
  useEffect(() => {
    if (user) {
      autoSaveAddresses();
    }
  }, [addresses]);

  if (!user) {
    return (
      <div style={{ textAlign: 'center', marginTop: '50px' }}>
        Loading...
      </div>
    );
  }

  return (
    <div style={{
      maxWidth: '700px',
      margin: '40px auto',
      padding: '30px',
      borderRadius: '12px',
      boxShadow: '0 0 10px rgba(0,0,0,0.1)',
      backgroundColor: '#fff'
    }}>

      {/* ================= PROFILE ================= */}
      <div style={{ textAlign: 'center', marginBottom: '30px' }}>
        <img
          src="https://cdn-icons-png.flaticon.com/512/3135/3135715.png"
          alt="profile"
          style={{ width: '120px', height: '120px', borderRadius: '50%' }}
        />

        <h2>{user.name}</h2>
        <p>{user.role}</p>
      </div>

      {/* ================= USER INFO ================= */}
      <div style={{ lineHeight: '2' }}>
        <h3>User Information</h3>

        <p><strong>Email:</strong> {user.email}</p>
        <p><strong>Contact:</strong> {user.contact}</p>

        {/* ================= ADDRESSES ================= */}
        <div style={{ marginTop: "20px" }}>
          <strong>Addresses:</strong>

          {addresses.map((addr, index) => (
            <div
              key={index}
              style={{
                marginTop: "10px",
                display: "flex",
                gap: "10px",
                alignItems: "center"
              }}
            >
              <input
                type="text"
                value={addr}
                onChange={(e) => handleAddressChange(index, e.target.value)}
                placeholder={`Address ${index + 1}`}
                style={{
                  width: "100%",
                  padding: "10px",
                  border: "1px solid #ccc",
                  borderRadius: "8px"
                }}
              />

              <button
                onClick={() => removeAddressField(index)}
                style={{
                  padding: "10px",
                  border: "none",
                  borderRadius: "8px",
                  backgroundColor: "red",
                  color: "#fff",
                  cursor: "pointer"
                }}
              >
                ✕
              </button>
            </div>
          ))}

          <div style={{ marginTop: "15px" }}>
            <button
              onClick={addAddressField}
              disabled={addresses.length >= 3}
              style={{
                padding: "10px 20px",
                border: "none",
                borderRadius: "8px",
                backgroundColor: addresses.length >= 3 ? "gray" : "#007bff",
                color: "#fff",
                cursor: addresses.length >= 3 ? "not-allowed" : "pointer"
              }}
            >
              + Add Address
            </button>
          </div>
        </div>

        {/* ================= CHANGE PASSWORD ================= */}
        <div style={{ marginTop: "40px" }}>
          <h3>Change Password</h3>

          <input
            type="password"
            name="oldPassword"
            value={passwordData.oldPassword}
            onChange={handlePasswordChange}
            placeholder="Old Password"
            style={{
              width: "100%",
              padding: "10px",
              marginTop: "10px",
              border: "1px solid #ccc",
              borderRadius: "8px"
            }}
          />

          <input
            type="password"
            name="newPassword"
            value={passwordData.newPassword}
            onChange={handlePasswordChange}
            placeholder="New Password"
            style={{
              width: "100%",
              padding: "10px",
              marginTop: "10px",
              border: "1px solid #ccc",
              borderRadius: "8px"
            }}
          />

          <button
            onClick={changePassword}
            style={{
              marginTop: "15px",
              padding: "10px 20px",
              border: "none",
              borderRadius: "8px",
              backgroundColor: "#ff9800",
              color: "#fff",
              cursor: "pointer"
            }}
          >
            Change Password
          </button>
        </div>
      </div>

      {/* ================= LOGOUT ================= */}
      <div style={{ marginTop: '30px' }}>
        <button
          onClick={async () => {
            try {
              await instance.get("/logout", {
                headers: { Authorization: `Bearer ${token}` }
              });
            } catch (err) {
              console.log(err);
            }

            localStorage.removeItem("token");
            localStorage.removeItem("username");
            localStorage.removeItem("portiire");

            history.push('/');
          }}
          style={{
            padding: '10px 20px',
            border: 'none',
            borderRadius: '8px',
            backgroundColor: 'red',
            color: '#fff',
            cursor: 'pointer'
          }}
        >
          Logout
        </button>
      </div>

    </div>
  );
};

export default User;