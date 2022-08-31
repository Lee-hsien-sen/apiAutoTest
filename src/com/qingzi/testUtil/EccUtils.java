package com.qingzi.testUtil;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECKeyPair;
import org.whispersystems.libsignal.ecc.ECPrivateKey;
import org.whispersystems.libsignal.ecc.ECPublicKey;
import org.whispersystems.libsignal.kdf.HKDFv3;
import org.whispersystems.libsignal.util.ByteUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class EccUtils {


    public static ECPublicKey getECPublicKey(String pub) {
        try {
            //补位 已适配signal
            byte[] type = new byte[]{5};
            byte[] bytes = ByteUtil.combine(new byte[][]{type, Base64.decodeBase64(pub)});
            return Curve.decodePoint(bytes, 0);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            Log.logInfo("key invalid");
        }
        return null;
    }


    public static ECPrivateKey decodePrivatePoint(String pri) throws DecoderException {
        return decodePrivatePoint(Hex.decodeHex(pri.toCharArray()));
    }

    public static ECPrivateKey decodePrivatePoint(byte[] bytes) {
        return Curve.decodePrivatePoint(bytes);
    }

    public static KeyInfo makeIKey(ECPublicKey publicKey, ECPrivateKey privateKey, String salt) {
        byte[] ikey;
        try {
            ikey = Curve.calculateAgreement(publicKey, privateKey);
            Log.logInfo("eccUtils pubKey: {}, priKey:{}  ikey: {},ikey64:{}, salt {},size:{} " +
                    Hex.encodeHexString(publicKey.serialize()) + " " +
                    Hex.encodeHexString(privateKey.serialize()) + " " +
                    Hex.encodeHexString(ikey) + " " +
                    org.apache.commons.net.util.Base64.encodeBase64String(ikey) + " " +
                    salt + " " +
                    ikey.length
            );
        } catch (InvalidKeyException e) {
            Log.logInfo("key invalid");
            throw new IllegalStateException("key invalid");
        }
        KeyInfo logInfo = new KeyInfo();
        logInfo.ikey = ikey;
        byte[] derivativeKey = new byte[0];
        try {
            derivativeKey = new HKDFv3().deriveSecrets(ikey, salt.getBytes("UTF-8"), "crystalIKey".getBytes("UTF-8"), 128);
        } catch (UnsupportedEncodingException e) {
            Log.logInfo("salt or logInfo not supported encode");
        }
        Log.logInfo("eccUtils derivativeKey: {},\n expend64:{}" + " " + Hex.encodeHexString(derivativeKey) + " " +
                org.apache.commons.net.util.Base64.encodeBase64String(derivativeKey)
        );

        byte[] apikey = new byte[32];
        System.arraycopy(derivativeKey, 0, apikey, 0, apikey.length);
        logInfo.apiKey = apikey;
        Log.logInfo("eccUtils apikey: {}" + Hex.encodeHexString(apikey));

        byte[] channelKey = new byte[32];
        System.arraycopy(derivativeKey, 32, channelKey, 0, channelKey.length);
        logInfo.channelKey = channelKey;
        Log.logInfo("eccUtils channelKey: {}" + Hex.encodeHexString(channelKey));

        byte[] reservedKey = new byte[64];
        System.arraycopy(derivativeKey, 64, reservedKey, 0, reservedKey.length);
        logInfo.reservedKey = reservedKey;
        Log.logInfo("eccUtils reservedKey: {}" + Hex.encodeHexString(reservedKey));

        return logInfo;
    }

    public static Map<String, String> getPair() {
        ECKeyPair pair = Curve.generateKeyPair();
        byte[] pubKey = pair.getPublicKey().serialize();
        byte[] bytes = new byte[32];
        System.arraycopy(pubKey, 1, bytes, 0, bytes.length);
        String serverPublicKey = new String(Base64.encodeBase64Chunked(bytes)).trim();

        Map<String, String> map = new HashMap<>();
        byte[] privateKey = pair.getPrivateKey().serialize();
//        String pri = new String(Base64.encodeBase64Chunked(privateKey));
        String pri = Hex.encodeHexString(privateKey);
        map.put("pub", serverPublicKey);
        map.put("pri", pri);
        return map;
    }

    public static String doExchange(String uid, String dev, String serverPublicKey, String cliPri) throws DecoderException {
//        ECKeyPair pair = Curve.generateKeyPair();
//        byte[] pubKey = pair.getPublicKey().serialize();
//        byte[] bytes = new byte[32];
//        System.arraycopy(pubKey, 1, bytes, 0, bytes.length);
//        String serverPublicKey = new String(Base64.encodeBase64Chunked(bytes));
        ECPrivateKey privateKey = decodePrivatePoint(cliPri);
        KeyInfo ikeyInfo = EccUtils.makeIKey(EccUtils.getECPublicKey(serverPublicKey), privateKey, String.valueOf(uid));
        String apiKeyId = ikeyInfo.apiKeyId;
        apiKeyId = DigestUtils.sha256Hex(ikeyInfo.apiKey).substring(0, 16);
        ikeyInfo.channelKeyId = DigestUtils.sha256Hex(ikeyInfo.channelKey).substring(0, 16);
        ikeyInfo.uid = uid;
        ikeyInfo.dev = dev;
        ikeyInfo.ctime = System.currentTimeMillis();
        Map<String, String> map = new HashMap<>();
        map.put("pub", serverPublicKey);
//        map.put("apiKeyId", ikeyInfo.apiKeyId);  // 返回会有安全问题
//        map.put("channelKeyId", ikeyInfo.channelKeyId);
        Log.logInfo("apiKeyId:{},channelKeyId:{}" + apiKeyId + ikeyInfo.channelKeyId);
        return apiKeyId;
    }

}
