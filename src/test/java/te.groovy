import com.alibaba.fastjson.JSONObject

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by danny.deng on 2018/12/27.
 */

String rd = new String(ResponseData);
String binType = "";

if (rd != null){
	try{
		JSONObject data = JSONObject.parseObject(rd);
		int code = data.getFloatValue("status");
		if(code==0){
			Failure = false;
			JSONObject dataobj = data.get("data");
			banType = dataobj.getString("banType");
			write2file();
		}else{
			Failure = true;
		}
	}catch(Exception e){
        e.printStackTrace()
		log.error(e.toString());
		log.info(rd);
		Failure = false;
	}
}else{
	Failure = true;
}

def write2file() {
    String characterName = ${characterName};
    String chatContent = ${chatContent};
    String characterLevel = ${characterLevel};
    String platform = ${platform};
    String ybCharge = ${ybCharge};
    String chatType = ${chatType};
    String binType = ${binType};
    String fileName = "E:/TestCenter/02 平台技术业务/压测项目/迅风AI禁言接口/result.csv";

    if (binType.equals("0")) {
        if (regcheck(characterName + "_" + chatContent)) {
            FileWriter fw = new FileWriter(fileName, false);
            fw.write(characterName + "," + chatContent + "," + characterLevel + "," + platform + "," + ybCharge + "," + chatType + "," + binType);
            fw.write("\r\n");
            fw.close();
        }
    } else {
        FileWriter fw = new FileWriter(fileName, false);
        fw.write(characterName + "," + chatContent + "," + characterLevel + "," + platform + "," + ybCharge + "," + chatType + "," + binType);
        fw.write("\r\n");
        fw.close();
    }
}

def regcheck(String content) {
    for (String re : regExs) {
        Pattern pattern = Pattern.compile(re, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        if (matcher.matches()) {
            return true;
        }
    }
    return false;
}
