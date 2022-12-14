import axios from "axios";
import { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
import "./MergeBoddari.scss";
import Loading from "../component/Loading";

// image
import back from "../../assets/images/icons/back.png";
import after from "../../assets/images/icons/after.png";
import { useNavigate } from "react-router-dom";

const MergeBoddari = () => {
  const navigate = useNavigate();
  const userId = useSelector((state) => state.authToken.userId);
  const accessToken = useSelector((state) => state.authToken.accessToken);

  //<-- state -->
  const [loading, setLoading] = useState(true);
  const [bagList, setBagList] = useState(null);
  const [likeBagList, setLikeBagList] = useState(null);
  const [bagId1name, setBagId1Name] = useState(null);
  const [bagId2name, setBagId2Name] = useState(null);
  const [bagId1, setBagId1] = useState();
  const [bagId2, setBagId2] = useState();
  const [attraction, setAttraction] = useState([]);

  // <-- function -->
  const getBoddariList = async () => {
    try {
      const response = await axios({
        url: `https://j7c103.p.ssafy.io:8080/api/bag/list/${userId}`,
        method: "get",
        headers: {
          "X-AUTH-TOKEN": accessToken,
        },
      });
      console.log(response);
      if (response.data.data.length === 0) {
        setBagList(null);
        setLoading(false);
      } else {
        setBagList(response.data.data);
        setLoading(false);
      }
    } catch (err) {
      console.log(err);
      setLoading(false);
    }
  };
  const getLikeBoddari = async () => {
    try {
      const response = await axios({
        url: `https://j7c103.p.ssafy.io:8080/api/bag/likelist/${userId}`,
        method: "get",
        headers: {
          "X-AUTH-TOKEN": accessToken,
        },
      });
      if (response.data.data.length === 0) {
        setLikeBagList(null);
        setLoading(false);
      } else {
        setLikeBagList(response.data.data);
        setLoading(false);
      }
    } catch (err) {
      console.log(err);
      setLoading(false);
    }
  };
  const merge = () => {
    setLoading(true);
    axios({
      url: `https://j7c103.p.ssafy.io/django/MergeBoddari/Recommend/${bagId1}/${bagId2}/`,
      method: "get",
    })
      .then((res) => {
        const response = res.data;
        const succ = response.map((el: any, index: any) => {
          return el.attraction_id;
        });
        console.log(succ);
        setAttraction(succ);
        setLoading(false);
        alert("????????? ??????! ????????? ???????????????");
      })
      .catch((err) => {
        console.log(err);
        setLoading(false);
        alert("?????? ????????? ?????????");
      });
  };
  // <-- useEffect -->
  useEffect(() => {
    getBoddariList();
    getLikeBoddari();
  }, []);

  // <-- rendering -->
  return (
    <div className="mergeBoddari">
      {/* ??????????????? */}
      {loading ? <Loading /> : null}
      {/* -----------------------------------------------------------------------------------------------------------*/}
      {/* user??? ?????? ????????? ????????? ????????? */}
      <img
        className="backBtn"
        src={back}
        alt="????????????"
        onClick={() => navigate(-1)}
      />
      <Link to="/SaveBoddari" state={{ attraction: attraction }}>
        <img className="afterBtn" src={after} alt="??????????????????" />
      </Link>
      <div className="header">
        <p className="text-center">????????? ?????????</p>
      </div>

      <div className="mergeBoddari_content">
        {bagId1name ? (
          <p className="bagid1">?????? ????????? : {bagId1name}</p>
        ) : (
          <p></p>
        )}
        <div className="my-item">
          <h1>??? ?????????</h1>
          <div className="my-item-content">
            {bagList ? (
              bagList.map((el, index) => {
                return (
                  <div className="my">
                    <p className="myName">{el.name}</p>
                    <img src={el.image} alt="image" className="myimg"></img>
                    <button
                      className="myBtn"
                      onClick={() => {
                        setBagId1(el.id);
                        setBagId1Name(el.name);
                      }}
                    >
                      ??????
                    </button>
                  </div>
                );
              })
            ) : (
              <p>?????? ???????????? ????????????</p>
            )}
          </div>
        </div>
        {/* -----------------------------------------------------------------------------------------------------------*/}
        {/* ???????????? ?????? ????????? ????????? ????????? */}
        {bagId2name ? (
          <p className="bagid2">?????? ????????? : {bagId2name}</p>
        ) : (
          <p></p>
        )}
        <div className="other-item">
          <h1>???????????? ?????????</h1>
          <div className="other-item-content">
            {likeBagList ? (
              likeBagList.map((el, index) => {
                return (
                  <div className="other">
                    <p className="otherName">{el.name}</p>
                    <img src={el.image} alt="image" className="otherimg"></img>
                    <button
                      className="otherBtn"
                      onClick={() => {
                        setBagId2(el.id);
                        setBagId2Name(el.name);
                      }}
                    >
                      ??????
                    </button>
                  </div>
                );
              })
            ) : (
              <p>???????????? ???????????? ????????????.</p>
            )}
          </div>
        </div>
      </div>
      <p></p>
      <div className="mergeTarget">
        {bagId1name || bagId2name ? (
          <div className="mergeTarget">
            <button
              onClick={() => {
                merge();
              }}
            >
              ?????????
            </button>
          </div>
        ) : (
          <div className="mergeTarget">
            <h1>????????? ?????? ???????????? ?????? ?????????!</h1>
          </div>
        )}
      </div>
    </div>
  );
};
export default MergeBoddari;
