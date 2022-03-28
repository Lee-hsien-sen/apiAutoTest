package com.qingzi.testUtil;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class SignatureUtil {
    private static String formatTime(long time) {
        long l = ((time >> 20) & 0xFFFFF) ^ (time & 0xFFFFF);
        return l + "";
    }

    private static String formatMap(TreeMap<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String value = entry.getValue();
            if (StringUtils.isNotBlank(value)) {
                sb.append(entry.getKey()).append(entry.getValue());
            }
        }
        return sb.toString();
    }

    /**
     * 签名校验
     *
     * @param authKey   appKey
     * @param token     appID
     * @param nonce
     * @param time
     * @param args
     * @param postData
     * @param
     * @return
     */
    public static String check(String authKey, String token, String nonce, long time, TreeMap<String, String> args, String postData) {
        String i = formatTime(time);
        if (StringUtils.isBlank(postData)) {
            postData = "";
        }
        String str = authKey + token + nonce + i + formatMap(args) + postData;
        String sha256Hex = DigestUtils.sha256Hex(str);
        return sha256Hex;
//        return StringUtils.equalsIgnoreCase(sha256Hex, signature);
    }

    public static TreeMap<String, String> qzGetSign(String parameter) {
        StringBuffer sortString = new StringBuffer();
        TreeMap<String, String> treeMap = new TreeMap<>();
        String arr[] = parameter.split("&");
        Arrays.sort(arr);
        for (int i = 0; i < arr.length; i++) {
            String split[] = arr[i].split("=");
            treeMap.put(split[0],split[1]);
        }
        System.out.println(treeMap);
        return treeMap;
    }

    public static void main(String[] args) {
        String parameter = "appid=jiaowu&template_id=[473,475]&timestamp=\"+NWN.timestamp";
        qzGetSign(parameter);
    }

    public static void main1(String[] args) {
        String AppID = "1234567";
        String AppKey = "abcdefgh";
        String nonce = "2467832469";
        String postdata = "{\n" +
                "    \"k1\": \"v1\"\n" +
                "}";
        long time = 1647313016066L;
        TreeMap<String, String> map = new TreeMap<>();
        map.put("name", "whb");
        map.put("t", "3");
        map.put("id", "ad4b11d596305494e7e92281968452f2");
        String signature = "8a1b260a877df65dd5ec7b0c810724870397223f8b7e2802fefd2b225e1c3d01";
        String check = SignatureUtil.check(AppKey, AppID, nonce, time, map, postdata);
        System.out.println(check);
    }
}
