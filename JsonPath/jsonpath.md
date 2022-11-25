# JsonPath

## jayway json-path

```xml
<dependency>
    <groupId>com.jayway.jsonpath</groupId>
    <artifactId>json-path</artifactId>
    <version>2.7.0</version>
</dependency>
```

### 示例

```java
String jsonStr = "{\"name\": \"zhangsan\", \"data\": \"{}\", \"arr\": [{\"id\":1},{\"id\":2}]}";
//String jsonStr = "{\"name\": \"zhangsan\", \"data\": \"{}\", \"arr\": [{\"date\":\"2022-01-20\"},{\"date\":\"2022-01-20\"}]}";
LinkedHashMap<String, Object> jsonMap = JSONUtil.toBean(jsonStr, LinkedHashMap.class);
for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
    System.out.println(entry.getKey() + ":" + entry.getValue());
}

//Object read = JsonPath.read(jsonStr, "$.arr[*].id");
Object read = JsonPath.read(jsonStr, "$..*");
System.out.println(read);
```



## fastjson JSONPath

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.80</version>
</dependency>
```

### 示例

```java
String jsonStr = "{\"name\": \"zhangsan\", \"data\": \"{}\", \"arr\": [{\"id\":1},{\"id\":2}]}";
Object name = JSONPath.read(jsonStr, "$.name");
System.out.println(name);

List<Object> list = (List<Object>) JSONPath.read(jsonStr, "$.arr[*].id");
System.out.println(list);
```

### 判断是否包含所有key

```java
package com.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;

import java.util.Arrays;
import java.util.List;

public class Demo {

    public static void main(String[] args) {
        List<String> lists = Arrays.asList(
                "ip",
                "rectime",
                "mDeviceId",
                "mAppProfile.mAppPackageName",
                "mAppProfile.mAppVersionName",
                "mAppProfile.mAppVersionCode",
                "mAppProfile.mStartTime",
                "mAppProfile.mSdkVersion",
                "mAppProfile.mPartnerId",
                "mDeviceProfile.mMobileModel",
                "mDeviceProfile.mOsSdkVersion",
                "mDeviceProfile.mGis.lng",
                "mDeviceProfile.mGis.lat",
                "mDeviceProfile.mPixelMetric",
                "mDeviceProfile.mCountry",
                "mDeviceProfile.mCarrier",
                "mDeviceProfile.mLanguage",
                "mDeviceProfile.mTimezone",
                "mDeviceProfile.mOsVersion",
                "mDeviceProfile.mChannel",
                "mDeviceProfile.m2G_3G",
                "mDeviceProfile.isJailBroken",
                "mDeviceProfile.mSimOperator",
                "mDeviceProfile.mNetworkOperator",
                "mDeviceProfile.kernBootTime",
                "mDeviceProfile.advertisingID",
                "mDeviceProfile.mWifiBSSID",
                "mDeviceProfile.mMobileNetType",
                "mDeviceProfile.mCellID",
                "mDeviceProfile.mLac1"
        );

        String json = "{\"mDeviceId\":\"123123\",\"mDeveploperAppkey\":\"123123\",\"mAppProfile\":{\"mAppPackageName\":\"123123\",\"mAppVersionName\":\"2.3.0\",\"mAppVersionCode\":\"6\",\"mStartTime\":1666885637270,\"mSdkVersion\":\"123123\",\"mPartnerId\":\"huawei\",\"isCracked\":false,\"installationTime\":1666882590996,\"purchaseTime\":1666882590996,\"appStoreID\":0},\"mDeviceProfile\":{\"mMobileModel\":\"123123\",\"mOsSdkVersion\":\"29\",\"mGis\":{\"lng\":0.0,\"lat\":0.0},\"mCpuABI\":\"123123\",\"mPixelMetric\":\"1152*2268*480\",\"mCountry\":\"CN\",\"mCarrier\":\"中国移动\",\"mLanguage\":\"zh\",\"mTimezone\":8,\"mOsVersion\":\"Android+10\",\"mChannel\":0,\"m2G_3G\":\"WIFI\",\"isJailBroken\":false,\"mSimOperator\":\"46000\",\"mNetworkOperator\":\"46000\",\"hostName\":\"\",\"deviceName\":\"\",\"kernBootTime\":1666504015003,\"advertisingID\":\"2|null|c74fdf391730c218|null|null|null|123123||null\",\"mWifiBSSID\":\"[{\\\"type\\\":\\\"wifi\\\",\\\"available\\\":true,\\\"connected\\\":true},{\\\"type\\\":\\\"cellular\\\",\\\"available\\\":true,\\\"connected\\\":false}]\",\"mMobileNetType\":\"\",\"mCellID\":0,\"mLac\":0},\"mTMessages\":[{\"mMsgType\":2,\"session\":{\"id\":\"11f781ab-6f89-4266-acb7-123123\",\"start\":1666886313747,\"mStatus\":2,\"duration\":0,\"activities\":[{\"name\":\"arrive_my\",\"start\":1666886313807,\"duration\":8,\"refer\":\"arrive_my\"}],\"appEvents\":[{\"id\":\"click_tickets_list_card\",\"label\":\"\",\"count\":1,\"startTime\":1666886322887,\"parameters\":{\"ciam_uuid\":\"\",\"material_id\":\"123123\",\"type\":\"门票\",\"material_title\":\"123123\"}},{\"id\":\"click_tickets_and_expresspasses_list_card\",\"label\":\"\",\"count\":1,\"startTime\":1666886322894,\"parameters\":{\"ciam_uuid\":\"\",\"id\":\"123312123\",\"title\":\"123123\",\"type\":\"123123\"}}],\"isConnected\":1},\"mInitProfile\":null,\"mAppException\":null}],\"activeApps\":null,\"ip\":\"123123\",\"rectime\":\"1666886323214\"}";
        Object parse = JSON.parse(json);
        for (String item : lists) {
            boolean contains = JSONPath.contains(parse, item);
            System.out.printf("包含%s: %s \n", item, contains);
            if (!contains) {
                throw new RuntimeException("key未完全匹配！");
            }
        }
    }
}

```
