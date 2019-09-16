# xml解析

* 例子

```groovy
package com.groovy.domain

/**
 * @author Jonny* @description
 * @date 2019/09/16
 */
/**
 * @author Jonny* @description ${description}* @date 2019/9/16 0016
 */
class XmlDemo {

    final static String xml = '''<data>
    <datainfos  uuid="E84B37E46CDF4E25B56E1C361FBD0FF2">
        <datainfo>
            <uuid  REMARK="UUID">52F2C86F1B874AFAB73F7E7DF1D6DAFC</uuid>
            <DESC1  REMARK="员工工号">986196</DESC1>
            <DESC2  REMARK="姓名">张鸿霞</DESC2>
            <DESC3  REMARK="岗位序列编码">A</DESC3>
                    <DESC4  REMARK="性别">女</DESC4>
                    <DESC5  REMARK="所属公司编码">251</DESC5>
                    <DESC6  REMARK="所属公司名称">新疆特变电工集团物流有限公司</DESC6>
                    <DESC7  REMARK="经营单位编码">A17</DESC7>
                    <DESC8  REMARK="所属部门编码">10002401</DESC8>
                    <DESC9  REMARK="所属部门名称">经营班子</DESC9>
                    <DESC10  REMARK="上级部门编码"/>
                    <DESC11  REMARK="证件号码">652327197605022626</DESC11>
                    <DESC12  REMARK="手机">18599331911</DESC12>
                    <DESC13  REMARK="电话号码"/>
                    <DESC14  REMARK="电子邮箱">zhanghongxia@tbea.com</DESC14>
                    <DESC15  REMARK="岗位编码">10020474</DESC15>
                    <DESC16  REMARK="岗位名称">副总经理</DESC16>
                    <DESC17  REMARK="职务级别"/>
                    <DESC18  REMARK="入职日期">1998-05-01</DESC18>
                    <DESC19  REMARK="离职日期"/>
                    <DESC20  REMARK="岗位序列">管理类</DESC20>
                    <DESC21  REMARK="是否在岗">Y</DESC21>
                    <DESC22  REMARK="调离记录状态"/>
                    <DESC23  REMARK="员工类别编码">1</DESC23>
                    <DESC24  REMARK="员工类别">正式员工</DESC24>
                    <DESC25  REMARK="开户银行">中国工商银行</DESC25>
                    <DESC26  REMARK="银行账号">6217233004000788047</DESC26>
                    <DESC27  REMARK="联行号"/>
                    <DESC28  REMARK="所属组织终身码"/>
                    <DESC29  REMARK="所属部门终身码"/>
                    <DESC30  REMARK="国家/地区"/>
                    <DESC31  REMARK="HR职务级别编码"/>
                    <DESC32  REMARK="归属范围"/>
                    <DESC33  REMARK="HR档案主键"/>
                    <DESC34  REMARK="数据来源">JT</DESC34>
                    <DESC35  REMARK="时间戳"/>
                    <DESC36  REMARK="优先级别"/>
                    <DESC37  REMARK="证件类型">身份证</DESC37>
                    <DESC38  REMARK="经营单位名称">新疆特变电工集团有限公司</DESC38>
                    <DESC39  REMARK="上级部门名称"/>
                    <DESC40  REMARK="职务指示">主要职务</DESC40>
                    <DESC41  REMARK="第一次入特变日期">2019-03-01</DESC41>
                    <DESC42  REMARK="汇报者岗位">10014571</DESC42>
                    <DESC43  REMARK="汇报者岗位名称">总经理</DESC43>
                    <DESC44  REMARK="直接上级工号">325516</DESC44>
                    <DESC45  REMARK="直接上级名称">叶青</DESC45>
                    <DESC46  REMARK="数据新增/变更">1</DESC46>
                    <CODE  REMARK="主编码">986196</CODE>
                    <SUBMITCORP  REMARK="提报单位">10000000</SUBMITCORP>
                    <SPECIALITYCODES>
                        <SPECIALITYCODE  SPECIALITYNAME="银行信息" CATEGORYCODE="" SPECIALITYCODE="10002">
                            <PROPERTYCODE  PROPERTYCODE="BANK" STANDARDCODE="" PROPERTYNAME="开户银行">中国工商银行</PROPERTYCODE>
                            <PROPERTYCODE  PROPERTYCODE="BANKACCOUNT" STANDARDCODE="" PROPERTYNAME="银行账号">6217233004000788047</PROPERTYCODE>
                            <PROPERTYCODE  PROPERTYCODE="NBR" STANDARDCODE="" PROPERTYNAME="联行号"/>
                        </SPECIALITYCODE>
                    </SPECIALITYCODES>
                </datainfo>
            </datainfos>
        </data>'''

    static void main(String[] args) {
        def xmlSlurper = new XmlSlurper()
        def result = xmlSlurper.parseText(xml)
        result.datainfos.each {
            println it.datainfo.uuid
            println it.datainfo.DESC1
            println it.datainfo.DESC2
            println it.datainfo.DESC3
        }
    }
}
```
