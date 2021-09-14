//package test;
//
//
//
//import com.puhui.bestbuy.common.domain.wx.Token;
//import com.puhui.bestbuy.common.domain.wx.WeixinOauth2Token;
//import com.puhui.bestbuy.common.domain.wx.WeixinUserInfo;
//import com.puhui.bestbuy.common.domain.wx.WeixinUserList;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONException;
//import net.sf.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.Arrays;
//import java.util.List;
//
//
///**
// * @ClassName: UserUtil.java
// * @Title: 权限控制工具类
// * @Description: 权限控制工具类
// */
//public class UserUtil {
//
//    private static final String errcode = "errcode";
//
//    private static final String errmsg = "errmsg";
//
//    private static Logger log = LoggerFactory.getLogger(UserUtil.class);
//
//    public static Token getToken(String appid, String appsecret) {
//        Token token = null;
//        String requestUrl = WeixinParameter.token_url.replace("APPID", appid)
//                .replace("APPSECRET", appsecret);
//        // 发起GET请求获取凭证
//        JSONObject jsonObject = CommonUtil
//                .httpsRequest(requestUrl, "GET", null);
//
//        if (null != jsonObject) {
//            try {
//                token = new Token();
//                token.setAccessToken(jsonObject.getString("access_token"));
//                token.setExpiresIn(jsonObject.getInt("expires_in"));
//                log.info("[UserUtil][getToken]获取token成功{}", jsonObject.getString("access_token"));
//            } catch (JSONException e) {
//                token = null;
//                // 获取token失败
//                log.error("[UserUtil][getToken]获取token失败 errcode:{} errmsg:{}",
//                        jsonObject.getInt(errcode),
//                        jsonObject.getString(errmsg));
//                log.error("获取token失败", e);
//            }
//        }
//        return token;
//    }
//
//    /**
//     * 获取用户信息
//     *
//     * @param accessToken 接口访问凭证
//     * @param openId      用户标识
//     * @return WeixinUserInfo
//     */
//    public static WeixinUserInfo getUserInfo(String accessToken, String openId) {
//        WeixinUserInfo weixinUserInfo = new WeixinUserInfo();
//        // 拼接请求地址
//        String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";
//        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace(
//                "OPENID", openId);
//        // 获取用户信息
//        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
//
//        if (null != jsonObject) {
//            log.info("[UserUtil][getUserInfo][jsonObject]="
//                    + jsonObject.toString());
//            // 用户的标识
//            weixinUserInfo.setOpenId(openId);
//            // 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
//            weixinUserInfo.setSubscribe(jsonObject.getInt("subscribe"));
//            if (jsonObject.getInt("subscribe") == 1) {
//                // 用户关注时间
//                weixinUserInfo.setSubscribeTime(jsonObject
//                        .getString("subscribe_time"));
//                // 昵称
//                weixinUserInfo.setNickname(jsonObject.getString("nickname"));
//                // 用户的性别（1是男性，2是女性，0是未知）
//                weixinUserInfo.setSex(jsonObject.getInt("sex"));
//                // 用户所在国家
//                weixinUserInfo.setCountry(jsonObject.getString("country"));
//                // 用户所在省份
//                weixinUserInfo.setProvince(jsonObject.getString("province"));
//                // 用户所在城市
//                weixinUserInfo.setCity(jsonObject.getString("city"));
//                // 用户的语言，简体中文为zh_CN
//                weixinUserInfo.setLanguage(jsonObject.getString("language"));
//                // 用户头像
//                weixinUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
//            }
//        }
//        return weixinUserInfo;
//    }
//
//    /**
//     * 校验签名
//     *
//     * @param token     绑定TOKEN
//     * @param signature 微信加密签名
//     * @param timestamp 时间戳
//     * @param nonce     随机数
//     * @return
//     */
//    public static boolean checkSignature(String token, String signature,
//                                         String timestamp, String nonce) {
//
//        // 对token、timestamp和nonce按字典排序
//        String[] paramArr = new String[]{token, timestamp, nonce};
//        Arrays.sort(paramArr);
//
//        // 将排序后的结果拼接成一个字符串
//        String content = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);
//
//        String ciphertext = null;
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-1");
//            // 对接后的字符串进行sha1加密
//            byte[] digest = md.digest(content.getBytes());
//            ciphertext = CommonUtil.byteToStr(digest);
//        } catch (NoSuchAlgorithmException e) {
//            log.error("验签失败", e);
//        }
//
//        // 将sha1加密后的字符串与signature进行对比
//        log.info("ciphertext:" + ciphertext);
//        return ciphertext != null ? ciphertext.equalsIgnoreCase(signature)
//                : false;
//    }
//
//    /**
//     * 获取网页授权凭证
//     *
//     * @param appId     公众账号的唯一标识
//     * @param appSecret 公众账号的密钥
//     * @param code
//     * @return WeixinAouth2Token
//     */
//    public static WeixinOauth2Token getOauth2AccessToken(String appId,
//                                                         String appSecret, String code) {
//        WeixinOauth2Token wat = null;
//        // 拼接请求地址
//        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
//        requestUrl = requestUrl.replace("APPID", appId);
//        requestUrl = requestUrl.replace("SECRET", appSecret);
//        requestUrl = requestUrl.replace("CODE", code);
//        // 获取网页授权凭证
//        JSONObject jsonObject = CommonUtil
//                .httpsRequest(requestUrl, "GET", null);
//        if (null != jsonObject) {
//            try {
//                wat = new WeixinOauth2Token();
//                wat.setAccessToken(jsonObject.getString("access_token"));
//                wat.setExpiresIn(jsonObject.getInt("expires_in"));
//                wat.setRefreshToken(jsonObject.getString("refresh_token"));
//                wat.setOpenId(jsonObject.getString("openid"));
//                wat.setScope(jsonObject.getString("scope"));
//            } catch (Exception e) {
//                wat = null;
//                int errorCode = jsonObject.getInt(errcode);
//                String errorMsg = jsonObject.getString(errmsg);
//                log.error(
//                        "[UserUtil][getOauth2AccessToken]获取网页授权凭证失败 errcode:{} errmsg:{}",
//                        errorCode, errorMsg);
//                log.error("获取网页授权凭证失败", e);
//            }
//        }
//        return wat;
//    }
//
//    /**
//     * 获取关注者列表
//     *
//     * @param accessToken 调用接口凭证
//     * @param nextOpenId  第一个拉取的openId，不填默认从头开始拉取
//     * @return WeixinUserList
//     */
//    @SuppressWarnings({"unchecked", "deprecation"})
//    public static WeixinUserList getUserList(String accessToken,
//                                             String nextOpenId) {
//        WeixinUserList weixinUserList = new WeixinUserList();
//
//        if (null == nextOpenId)
//            nextOpenId = "";
//
//        // 拼接请求地址
//        String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
//        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace(
//                "NEXT_OPENID", nextOpenId);
//        // 获取关注者列表
//        JSONObject jsonObject = CommonUtil
//                .httpsRequest(requestUrl, "GET", null);
//        // 如果请求成功
//        if (null != jsonObject) {
//            weixinUserList.setTotal(jsonObject.getInt("total"));
//            weixinUserList.setCount(jsonObject.getInt("count"));
//            weixinUserList.setNextOpenId(jsonObject
//                    .getString("next_openid"));
//            JSONObject dataObject = (JSONObject) jsonObject.get("data");
//            weixinUserList.setOpenIdList(JSONArray.toList(
//                    dataObject.getJSONArray("openid"), List.class));
//
//        }
//        return weixinUserList;
//    }
//
//}
