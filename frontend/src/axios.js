import axios from "axios"

const instance = axios.create({
    // The Api {cloud Function } URL

    baseURL: "http://localhost:8080/api" 
});

// instance.interceptors.response.use(
//   (response) => response,
//   (error) => {
//     if (
//       error.response &&
//       (error.response.status === 401 ||
//         error.response.data?.message === "jwt expired")
//     ) {
//       // Clear storage
//       localStorage.removeItem("token");
//       localStorage.removeItem("username");
//       localStorage.removeItem("portiire");

//       // Redirect to login
//       window.location.href = "/login";
//     }

//     return Promise.reject(error);
//   }
// );


export default instance;