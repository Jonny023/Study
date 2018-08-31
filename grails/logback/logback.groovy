import ch.qos.logback.classic.Level
import ch.qos.logback.classic.filter.LevelFilter
import ch.qos.logback.core.spi.FilterReply

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        //pattern = "%-4(%d{HH:mm:ss.SSS} [%thread]) %-5level %logger{32} \\(%file:%line\\) - %msg%n"
        pattern = "%-4(%d{HH:mm:ss.SSS} | [%thread]) %-5level | %logger | \\(%class:%line\\) - %msg%n"
    }
}
logger("console",ERROR,['STDOUT'],false)

root(ERROR, ['STDOUT'])


//日志级别从高到地低 OFF 、 FATAL 、 ERROR 、 WARN 、 INFO 、 DEBUG 、 TRACE 、 ALL

//定义当前目录
def HOME_DIR = "."

//ERROR级别的日志
appender("ERROR", RollingFileAppender) {
    //过滤器，只记录ERROR级别的日志
    filter(LevelFilter) {
        level = Level.ERROR
        onMatch = FilterReply.ACCEPT
        onMismatch = FilterReply.DENY
    }
    //PatternLayoutEncoder对输出日志信息进行格式化
    encoder(PatternLayoutEncoder) {
        //默认为pattern = "%level %logger - %msg%n"
        //%d表示日期，%thread表示线程名，%level日志级别，%file具体的文件，%line记录日志位置，%msg日志消息，%n换行符
//        pattern = "%-4(%d{HH:mm:ss.SSS} [%thread]) %-5level %logger{32} \\(%file:%line\\) - %msg%n"
        //pattern = "%-4(%d{HH:mm:ss.SSS} [%thread]) %-5level %logger{32} \\(%F:%L\\) - %msg%n"
        pattern = "%-4(%d{HH:mm:ss.SSS} | [%thread]) %-5level | %logger | \\(%class:%line\\) - %msg%n"
    }
    //指定日志生成格式
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${HOME_DIR}/logs/%d{yyyy-MM-dd}_ERROR_%i.log"
        maxHistory = 30 //日志最长保留30天
        timeBasedFileNamingAndTriggeringPolicy(SizeAndTimeBasedFNATP) {
            maxFileSize = "10MB"
        }
    }

    //约束生成单个日志文件大小为10M，超过后新建一个文件保存
//    triggeringPolicy(SizeBasedTriggeringPolicy) {
//        maxFileSize = "10MB"
//    }
    append = true
}
logger("console",ERROR,['ERROR'],false)


//将指定级别的日志输出到日志文件中
root(ERROR, ['ERROR'])

//指定要在控制台打印的位置
logger('grails.app.controllers', INFO, ['STDOUT'], false)
logger('grails.app.services', INFO, ['STDOUT'], false)
logger('grails.app.jobs', INFO, ['STDOUT'], false)
logger('com.listener', INFO, ['STDOUT'], false)
