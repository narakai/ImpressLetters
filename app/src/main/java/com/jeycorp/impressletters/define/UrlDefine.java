package com.jeycorp.impressletters.define;

public class UrlDefine {
    public static final boolean DEBUG_MODE = false;

    public static final String LIVE_SERVER = "http://rich.halmecorp.com";
    public static final String LIVE_DATA = "http://good.halmecorp.com";
    public static final String LIVE_PUSH = "Y";

    public static final String DEV_SERVER = "http://dev.jeycorp.com/impressletters";
    public static final String DEV_DATA = "http://goodthings.jeycorp.com";
    public static final String DEV_PUSH = "N";


    private static String getServerUrl() {
        return LIVE_SERVER;
    }
    private static String getPushId(){
        return LIVE_PUSH;
    }
    private static String getData(){
        return LIVE_DATA;
    }

    public static final String PUSH = getPushId();
    public static final String SERVER = getServerUrl();
    public static final String DATA = getData();

    public static final String API = SERVER + "/api";
    public static final String WEB = SERVER;
    public static final String API_MARKETER = SERVER + "/api/marketer";

    //default
    public static final String GET_IMAGE_ICON = "/default_image/icon_rich.png";
    //api

    public static final String API_GET_INTRO = API + "/getIntro.php";
    public static final String API_SET_DEVICE = API + "/setDevice.php";
    public static final String API_GET_USER_KAKAO = API + "/getUserKakao.php";
    public static final String API_GET_USER = API +"/getUser.php";
    public static final String API_SET_USER_BREAK = API + "/setUserBreak.php";
    public static final String API_SET_USER_NICKNAME = API + "/setUserNickname.php";
    public static final String API_SET_USER_PROFILE = API + "/setUserProfile.php";

    public static final String API_GET_NOTICE_LIST = API + "/getNoticeList.php";
    public static final String API_SET_BOARD = API +"/setBoard.php";

    public static final String API_GET_GOODS_DETAIL = API + "/getGoodsDetail.php";
    public static final String API_GET_GOODS_COMMENTLIST = API + "/getCommentList.php";
    public static final String API_SET_GOODS_COMMENT = API + "/setGoodsComment.php";
    public static final String API_SET_GOODS_LIKE = API + "/setGoodsLike.php";
    public static final String API_SET_BOOKMARK = API + "/setBookMark.php";
    public static final String API_SET_SHARE = API + "/setShare.php";
    public static final String API_SET_DELETE_COMMENT = API + "/setDeleteComment.php";
    public static final String API_SET_MODIFY_COMMENT = API + "/setModifyComment.php";

    public static final String API_GET_GOODS_CATEGORY = API + "/getGoodsCategory.php";
    public static final String API_GET_GOODS_SEARCH = API + "/getGoodsBoardSearch.php";

    public static final String API_GET_GOODS_BOARD_LIST = API + "/getGoodsBoardList.php";
    public static final String API_GET_GOODS_BEST_BOARD_LIST = API + "/getGoodsBestBoardList.php";
    public static final String API_GET_MY_GOODS_BOARD_LIST = API + "/getMyGoodsBoardList.php";

    public static final String API_SET_PUSH = API + "/setPush.php";

    public static final String GET_FINISH = "http://dev.jeycorp.com/jeycorppopup/api/getFinish.php";
    public static final String API_POPUP_URL = "http://dev.jeycorp.com/jeycorppopup/";


//    public static final String GUIDE = "http://cds.jeycorp.com/oil/guide_app.html";
//    public static final String FAQ = "http://cds.jeycorp.com/oil/faq.html";

    public static final String GUIDE = WEB + "/guide_app.html";
    public static final String FAQ = WEB + "/faq.html";








}
