/**
 * Created by danny.deng on 2018/12/27.
 */

import com.alibaba.fastjson.JSONObject

import java.io.FileWriter;
import java.io.IOException
import java.util.regex.Matcher
import java.util.regex.Pattern;

def write2file() {
    String characterName = $ { characterName };
    String chatContent = $ { chatContent };
    String characterLevel = $ { characterLevel };
    String platform = $ { platform };
    String ybCharge = $ { ybCharge };
    String chatType = $ { chatType };
    String binType = $ { binType };
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
    def regExs = [".*微信.*", ".*薇信.*"];
    for (String re : regExs) {
        Pattern pattern = Pattern.compile(re, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        if (matcher.matches()) {
            return true;
        }
    }
    return false;
}

String rd = """{
    "status": 0,
    "data": {
        "gameId": "90115",
        "bantime": "1000",
        "reason": "",
        "originId": "4001",
        "banType": "1",
        "ip": "210.21.221.18",
        "characterName": "威震天'",
        "imei": "6e61c186-85e3-4e43-afe6-3e14b32c5c0c",
        "mid": "61237dfee407889db0e66c64ef0e3b9b",
        "characterId": "300909703725568",
        "serverId": "105"
    }
}
"""

JSONObject json = JSONObject.parseObject(rd);
JSONObject data = json.get("data");
String banType = data.getString("banType");
print(banType)

Boolean flag = regcheck("你大爷薇信的加一次：9035853");
print(flag)
