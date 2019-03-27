## 让模板渲染html和图片

[参考](https://blog.csdn.net/weixin_41301898/article/details/79959433?utm_source=blogxgwz3)

* java
```java
Map paramter = new HashMap();
paramter.put("imgUrl","d:\\file\\test.jpg");

String printFileName = JasperFillManager.fillReportFile(realPath,paramter,dataSource);
if(printFileName!=null) {
  JasperPrintManager.printReport(printFileName, true);
}
```

* 图片

```xml
<parameter name="imageURL" class="java.lang.String"/>

<image isUsingCache="false" isLazy="true">
  <reportElement x="73" y="91" width="198" height="89" uuid="0d57aeba-b807-42be-8dc2-7d138a181cf0"/>
  <imageExpression><![CDATA[$P{imageURL}]]></imageExpression>
</image>

```

* html

```xml
<textField isStretchWithOverflow="true">
  <reportElement x="71" y="20" width="412" height="92" uuid="86143b2e-1bd6-4703-97c8-9d97ba7e165d"/>
    <textElement markup="html">
      <font size="12"/>
    </textElement>
    <textFieldExpression><![CDATA[$F{pathNote}]]></textFieldExpression>
</textField>
```
