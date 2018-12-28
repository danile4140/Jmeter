package com.xunfeng.ai;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * Created by danny.deng on 2018/12/25.
 */
public class StringUtils {
    public static String gbk2utf8(String gbkStr) {
        try {
            return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");
        } catch (Exception e) {
            throw new InternalError();
        }
    }

    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }


    public static String getCharset(String str) {
        String iso8859 = "";
        String gbk = "";
        String utf8 = "";
        try {
            iso8859 = new String(str.getBytes("iso8859-1"));
            gbk = new String(str.getBytes("gbk"));
            utf8 = new String(str.getBytes("utf-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (iso8859.equals(str)) {
            return "iso8859";
        } else if (gbk.equals(str)) {
            return "gbk";
        } else if (utf8.equals(str)) {
            return "utf8";
        }
        return "";
    }

    public static String get_sign(Map<String, Object> params) {
        //签名
        // String key = SignUtils.getKey(gameId);
        String key = "f0d450";
        Set<String> keySet = params.keySet();
        List<String> keys = new ArrayList<String>(keySet);
        Collections.sort(keys);
        StringBuffer sb = new StringBuffer();
        for (String k : keys) {
            String value = JSONObject.toJSONString(params.get(k));

            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            sb.append(k).append('=').append(value).append('&');
        }
        sb.append("key=").append(key);
        
//        String str = sb.toString();
//        str = "channelId=TEST0000000&characterId=85929410691385&characterLevel=251&characterName=战皇之燕愿&chatType=3&eventTime=1545059300&gameId=90115&imei=6e61c186-85e3-4e43-afe6-3e14b32c5c0c&ip=210.21.221.18&mid=255e7785c7a2be98600942c939babdcc&originId=4001&platform=1&serverId=105&ybCharge=1000&key=f0d450";
//        try {
//            str = new String(str.getBytes("GBK"), "GBK");
//        } catch (Exception e) {
//
//        }
//        String strType = StringUtils.getCharset(str);
////        if (!strType.equals("utf8")) {
////            str = StringUtils.gbk2utf8(str);
////        }
        String wssign = MD5Utils.get_md5(sb.toString());
        return wssign;
    }
}
