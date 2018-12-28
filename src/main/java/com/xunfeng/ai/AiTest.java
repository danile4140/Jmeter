package com.xunfeng.ai;

import com.alibaba.fastjson.JSONObject;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Created by danny.deng on 2018/12/24.
 */
public class AiTest extends AbstractJavaSamplerClient {
    private final static Logger logger = LoggerFactory.getLogger(AiTest.class);
    private final String host = "http://datax.wind.idreamsky.com:8180/silence/check";


    @Override
    public Arguments getDefaultParameters() {
        System.out.println("init parameters");
        Arguments params = new Arguments();
        params.addArgument("gameId", "${gameId}");
        params.addArgument("eventTime", "${eventTime}");
        params.addArgument("originId", "${originId}");
        params.addArgument("serverId", "${serverId}");
        params.addArgument("imei", "${imei}");
        params.addArgument("mid", "${mid}");
        params.addArgument("characterId", "${characterId}");
        params.addArgument("characterName", "${characterName}");
        params.addArgument("characterLevel", "${characterLevel}");
        params.addArgument("ybCharge", "${ybCharge}");
        params.addArgument("channelId", "${channelId}");
        params.addArgument("platform", "${platform}");
        params.addArgument("ip", "${ip}");
        params.addArgument("chatType", "${chatType}");
        params.addArgument("chatContent", "${chatContent}");
        return params;
    }

    public String init(JavaSamplerContext context) {
        String chatContent = "";
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> it = context.getParameterNamesIterator();
        while (it.hasNext()) {
            String key = it.next();
            String value = context.getParameter(key);
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            if (key.equals("chatContent")) {
                chatContent = value;
                continue;
            }
            if (key.equals("TestElement.name")) {
                continue;
            }
            map.put(key, value);
        }
        String wssign = get_sign(map);
        map.put("wssign", wssign);
        map.put("chatContent", chatContent);
        return JSONObject.toJSONString(map);
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        System.out.println("tear down");
        super.teardownTest(context);
    }

    public String get_sign(Map<String, Object> params) {
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
        String str = sb.toString();
//        try {
//            str = new String(str.getBytes("GBK"), "GBK");
//        } catch (Exception e) {
//
//        }
//        String strType = StringUtils.getCharset(str);
//        if (!strType.equals("utf8")) {
//            str = StringUtils.gbk2utf8(str);
//        }
        return MD5Utils.get_md5(str);
    }

    public SampleResult runTest(JavaSamplerContext arg0) {
        final SampleResult sr = new SampleResult();

        try {
            String body;
            body = init(arg0);
            sr.sampleStart();
            // 发送请求
            String rst = HttpUtils.doPostJson(host, body);
            JSONObject jsonobj = JSONObject.parseObject(rst);
            String status = jsonobj.getString("status");
            if (status.equals("0")) {
                sr.setResponseCode("200");
                sr.setResponseMessage("ai check success!");
                sr.setSampleLabel("AiTest");
                sr.setSuccessful(true);
            } else {
                logger.error("----->>> ERROR: " + body);
                sr.setSuccessful(false);
            }
        } catch (Exception ex) {
            sr.setSuccessful(false);
            ex.printStackTrace();
        } finally {
            sr.sampleEnd();
        }
        return sr;
    }

    public static void main(String[] args) {
        String gameid = "90115";
//        long eventTime = System.currentTimeMillis();
        String eventTime = "1545059300";
        String originId = "4001";
        String ServerId = "105";
        String imei = "6e61c186-85e3-4e43-afe6-3e14b32c5c0c";
        String mid = "255e7785c7a2be98600942c939babdcc";
        String characterId = "85929410691385";
        String characterName = "福田收到";
        String characterLevel = "251";
        String ybCharge = "1000";
        String channelId = "TEST0000000";
        String platform = "1";
        String ip = "210.21.221.18";
        String chatContent = "楼梯集合";
        String chatType = "3";
        Arguments params = new Arguments();
        params.addArgument("gameId", gameid);
        params.addArgument("eventTime", eventTime);
        params.addArgument("originId", originId);
        params.addArgument("serverId", ServerId);
        params.addArgument("imei", imei);
        params.addArgument("mid", mid);
        params.addArgument("characterId", characterId);
        params.addArgument("characterName", characterName);
        params.addArgument("characterLevel", characterLevel);
        params.addArgument("ybCharge", ybCharge);
        params.addArgument("channelId", channelId);
        params.addArgument("platform", platform);
        params.addArgument("ip", ip);
        params.addArgument("chatType", chatType);
        params.addArgument("chatContent", chatContent);
        JavaSamplerContext arg0 = new JavaSamplerContext(params);

        //  jMeterContext.setAsyncCallback(asyncIO);
        AiTest aiTest = new AiTest();
        aiTest.setupTest(arg0);
        SampleResult rs = aiTest.runTest(arg0);
        aiTest.teardownTest(arg0);
    }
}
