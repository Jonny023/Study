## 解析字符xml

> 首先需要引入依赖

```xml
// gradle
compile group: 'dom4j', name: 'dom4j', version: '1.6.1'


// maven
<dependency>
    <groupId>dom4j</groupId>
    <artifactId>dom4j</artifactId>
    <version>1.6.1</version>
</dependency>

```

```java

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

public class XmlTest {

    public static void main(String[] args) {
        // 解析BillNo
        String xml ="<Response><ROWCOUNT>5</ROWCOUNT><Rows><Row><BillNo>T0140412</BillNo><MedicareType>0</MedicareType><BillingDepartmentId>154</BillingDepartmentId><BillingDepartment>门诊中医科</BillingDepartment><BillingDoctor>管理员</BillingDoctor><PayTime>2019-03-05 16:17:28</PayTime><PayStatus>3</PayStatus><BillCost>0.06</BillCost><Executed>0</Executed><PayNumber>myy01wechat20190305161713932</PayNumber><PayExtend>1|20190305161727||myy01wechat20190305161713932|rxrmyy01|||||T0140412</PayExtend><BillInvNo/></Row><Row><BillNo>T0140412</BillNo><MedicareType>0</MedicareType><BillingDepartmentId>154</BillingDepartmentId><BillingDepartment>门诊中医科</BillingDepartment><BillingDoctor>管理员</BillingDoctor><PayTime>2019-03-05 16:19:13</PayTime><PayStatus>2</PayStatus><BillCost>-0.06</BillCost><Executed>-1</Executed><PayNumber>1234234234</PayNumber><PayExtend>退款交易说明</PayExtend><BillInvNo/></Row></Rows></Response>";

        SAXReader reader = new SAXReader();
        Document document = null;
        try {
//            document = DocumentHelper.parseText(xml);
            try {
                document = reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(document.asXML());
            // 获取根节点
            Element root = document.getRootElement();
            System.out.println("获取BillNo为:"+root.element("Rows").element("Row").element("BillNo").getText());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}

```
