# 批量保存报错

> could not initialize proxy - no Session

* 问题分析

> 循环调用session，而循环使用的是同一个session，当去调用时发现session已被关闭

* 解决方案

> 在循环中使用新的事物

```groovy
package com.test

import com.candidate.*
import com.exam.PersonInfo
import grails.converters.JSON
import grails.transaction.Transactional

/**
 *  批量报名测试
 */
class TestController {

    def sessionFactory

    @Transactional
    def index() {
        def map = [result: false, msg: "批量报名失败"]

        params.remove("action")
        params.remove("format")
        params.remove("controller")
        println "hello"

        PerBaseInfo perBaseInfo
        String distinguish = params.remove("id")
        ExamNameConfig examNameConfig = ExamNameConfig.findByDistinguish(distinguish)
        if (!examNameConfig) {
            map.msg = "数据不存在或参数不正确"
            render map as JSON
            return
        }
        params.distinguish = distinguish

        String subjectName = CandidateSubject.findByDistinguish(distinguish, [sort: 'id', order: 'asc'])?.subjectName
        Volunteer volunteer = Volunteer.findByDistinguish(distinguish, [sort: 'id', order: 'asc'])
        SystemParameterConfig systemParameterConfig = SystemParameterConfig.findByDistinguish(distinguish)

        def personList = PersonInfo.list([max: 3000])
        if (personList) {
            def categoryType = CandidateCategory.findAllByDistinguish(distinguish, [max: 1])
            personList.eachWithIndex { PersonInfo entry, int i ->
              

                PerBaseInfo.withNewTransaction {
                    def session = sessionFactory.currentSession

                    // 判断是否报名
                    if (PerBaseInfo.executeQuery("select count(1) from PerBaseInfo p where p.personInfo.id=:pid", [pid: entry.id])[0] == 0) {
                        perBaseInfo = new PerBaseInfo(params)
                        perBaseInfo.save()
                        if (i.mod(100) == 0) {
                            session.flush()
                            session.clear()
                        }
                        println(i)
                    }
                }
            }
            map.result = true
            map.msg = "批量报名完成"
        }
        render map as JSON
    }
}

```
