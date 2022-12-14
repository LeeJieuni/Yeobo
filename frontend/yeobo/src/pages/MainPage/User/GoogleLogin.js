import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router";

import { useDispatch, useSelector } from "react-redux";
import { SET_TOKEN } from "../../../store/Auth";
import axios from "axios";

import jwt_decode from "jwt-decode";
// import { GoogleLogin } from "@react-oauth/google";
// import { GoogleOAuthProvider } from "@react-oauth/google";

const GoogleLogIn = () => {
  function onSignIn(googleUser) {
    var profile = googleUser.getBasicProfile();
    setUser(...user, profile.getId(), profile.getName(), profile.getImageUrl());
    console.log("ID: " + profile.getId()); // Do not send to your backend! Use an ID token instead.
    console.log("Name: " + profile.getName());
    console.log("Image URL: " + profile.getImageUrl());
    console.log("Email: " + profile.getEmail()); // This is null if the 'email' scope is not present.
  }

  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [user, setUser] = useState({});

  async function handleCallbackResponse(res) {
    try {
      console.log("googleLogin", res);
      console.log("encoded JWT ID Token: " + res.credential);
      var userObject = jwt_decode(res.credential);
      console.log("decode", userObject);
      setUser(userObject);
      await dispatch(
        SET_TOKEN({
          accessToken: res.credential,
          pictureUrl: userObject.picture,
          nickname: userObject.name,
          google: true,
        })
      );
      alert("로그인에 성공했습니다!");
      console.log(user);
      navigate("/");
    } catch (err) {
      alert(err);
    }
  }
  useEffect(() => {
    /* global google */
    google.accounts.id.initialize({
      client_id:
        "468504148024-8es9cee5thhj6r11so8ase485a8vhhh8.apps.googleusercontent.com",
      redirect_uri: `https://i7c103.p.ssafy.io:8080/oauth2/authorization/google?redirect_uri=https://i7c103.p.ssafy.io:443/oauth/redirect`,
      clientSecret: "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",
      callback: handleCallbackResponse,
    });
    google.accounts.id.renderButton(document.getElementById("signInDiv"), {
      theme: "outline",
      size: "large",
    });
    google.accounts.id.prompt();
  }, []);

  const clientId = process.env.REACT_APP_GOOGLE_KEY;
  const clientSecret = process.env.REACT_APP_GOOGLE_SECRET;

  return (
    <React.Fragment>
      <div data-onsuccess="onSignIn" id="signInDiv"></div>
      {/* <div class="g-signin2" data-onsuccess="onSignIn"></div> */}
      {/* {user && (
        <div>
          <img src={user.picture}></img>
          <h3>{user.name}</h3>
        </div>
      )} */}
      {/* <GoogleOAuthProvider clientId={clientId} clientSecret={clientSecret}>
        <GoogleLogin
          buttonText="google login"
          onSuccess={resGoogle}
          onError={() => {
            console.log("Login Failed");
            resGoogle();
          }}
        />
      </GoogleOAuthProvider> */}
    </React.Fragment>
  );
};

export default GoogleLogIn;
