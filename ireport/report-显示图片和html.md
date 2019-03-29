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

# 图片打印方式

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" name="Image" language="groovy" pageWidth="595" pageHeight="842" columnWidth="555" leftMargin="20" rightMargin="20" topMargin="20" bottomMargin="20" uuid="0342f951-157c-4709-b85e-daba5d610ba9">
	<property name="ireport.zoom" value="1.0"/>
	<property name="ireport.x" value="0"/>
	<property name="ireport.y" value="0"/>
	<style name="normalStyle" isDefault="true" forecolor="#000000" backcolor="#FFFFFF" hAlign="Left" vAlign="Middle" isBlankWhenNull="true" fontName="宋体" fontSize="12" isBold="false" pdfFontName="STSong-Light" pdfEncoding="UniGB-UCS2-H" isPdfEmbedded="true"/>
	<parameter name="image" class="java.io.InputStream"/>
	<parameter name="image2" class="java.lang.Object"/>
	<parameter name="image3" class="java.lang.String"/>
	<background>
		<band splitType="Stretch"/>
	</background>
	<summary>
		<band height="351" splitType="Stretch">
			<image>
				<reportElement x="0" y="0" width="113" height="75" uuid="6645d932-3fb1-42a3-aee8-d0c3ff82728b"/>
				<imageExpression><![CDATA[$P{image3}]]></imageExpression>
			</image>
			<image>
				<reportElement x="113" y="0" width="115" height="75" uuid="0fe88379-8aeb-4e37-883a-bc3418819793"/>
				<imageExpression><![CDATA[new java.io.File("C:\\Users\\caoyunyun\\Desktop\\下载.jpg")]]></imageExpression>
			</image>
			<image>
				<reportElement x="228" y="0" width="112" height="75" uuid="227fecec-989b-4ff5-a135-40141b097c88"/>
				<imageExpression><![CDATA[new java.net.URL($P{image3})]]></imageExpression>
			</image>
			<image>
				<reportElement x="340" y="0" width="113" height="75" uuid="63321ae6-538c-4265-bb64-f5a94edb7c98"/>
				<imageExpression><![CDATA[$P{image}]]></imageExpression>
			</image>
			<image>
				<reportElement x="0" y="75" width="113" height="76" uuid="7282ea0c-0467-435d-b7c5-26d6ef048011"/>
				<imageExpression><![CDATA[$P{image2}]]></imageExpression>
			</image>
		</band>
	</summary>
</jasperReport>
```

> 默认是选`Error`,如果图片加载不到（比如图片不存、传参错误之类的），就会报错
* `Blank`是如果加载不到图片，就显示空白，不会报错
* `Icon`是如果加载不到图片，就会显示默认的图标
