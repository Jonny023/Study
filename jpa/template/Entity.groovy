/**
 *  创建实体类和Repository
 */
import com.intellij.database.model.DasTable
import com.intellij.database.model.ObjectKind
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

import java.text.SimpleDateFormat

/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */
packageName = ""
typeMapping = [
        (~/(?i)tinyint|smallint|mediumint/)      : "Integer",
        (~/(?i)bigint/)                          : "Long",
        (~/(?i)int/)                             : "Integer",
        (~/(?i)bool|bit/)                        : "Boolean",
        (~/(?i)float|double|decimal|real/)       : "BigDecimal",
        (~/(?i)datetime|date|time/)              : "Date",
        (~/(?i)timestamp/)                       : "Timestamp",
        (~/(?i)blob|binary|bfile|clob|raw|image/): "InputStream",
        (~/(?i)/)                                : "String"
]


FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated files") { dir ->
    SELECTION.filter { it instanceof DasTable && it.getKind() == ObjectKind.TABLE }.each { generate(it, dir) }
}

def generate(table, dir) {
//    def className = javaClassName(table.getName(), true)
//    def fields = calcFields(table)
//    packageName = getPackageName(dir)
//    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(dir, className + ".java")), "UTF-8"))
//    printWriter.withPrintWriter { out -> genEntity(out,table, className, fields,packageName) }
//
//    new File(dir, className + ".java").withPrintWriter { out -> genEntity(out, className, fields,table) }
    def entity = "entity", repository = "repository"      //包名
    def entityPath = "${dir.toString()}\\${entity}",
        repPath = "${dir.toString()}\\${repository}"
    mkdirs([entityPath, repPath])
    def className = javaName(table.getName(), true)
    def fields = calcFields(table)
    def basePackage = getPackageName(dir)

    new File("${repPath}\\${className}Repository.java").withPrintWriter { out -> genRepository(out, table, className, fields, basePackage, entity, repository) }
    //生成JPA
    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(entityPath, className + ".java")), "UTF-8"))
    //编码格式
    printWriter.withPrintWriter { out -> genEntity(out, table, className, fields, basePackage, entity) }//生成实体类

}

// 获取包所在文件夹路径
def getPackageName(dir) {
    return dir.toString().replaceAll("\\\\", ".").replaceAll("/", ".").replaceAll("^.*src(\\.main\\.java\\.)?", "") + ""
}
// 创建目录
def mkdirs(dirs) {
    dirs.forEach {
        def f = new File(it)
        if (!f.exists()) {
            f.mkdirs();
        }
    }
}

def genRepository(out, table, className, fields, basePackage, entity, repository) {
    out.println "package ${basePackage}.${repository};\n"
    out.println "import ${basePackage}.${entity}.$className;"
    out.println "import cn.com.geely.jpa.BaseRepository;"
    out.println "import org.springframework.stereotype.Repository;\n"
    out.println "@Repository"
    out.println "public interface ${className}Repository extends BaseRepository<$className, ${fields[0].type}> {\n\n}"
}

def genEntity(out, table, className, fields, basePackage, entity) {
    out.println "package ${basePackage}.${entity};"
    out.println ""
    out.println "import javax.persistence.Column;"
    out.println "import javax.persistence.Entity;"
    out.println "import javax.persistence.Table;"
    out.println "import javax.persistence.Id;"
    out.println "import javax.persistence.GeneratedValue;"
    out.println "import javax.persistence.GenerationType;"
    out.println "import java.io.Serializable;"
    out.println "import lombok.Data;"
    Set types = new HashSet()

    fields.each() {
        types.add(it.type)
    }

    if (types.contains("Date")) {
        out.println "import java.util.Date;"
        out.println "import com.fasterxml.jackson.annotation.JsonFormat;"
    }
    if (types.contains("Timestamp")) {
        out.println "import java.sql.Timestamp;"
    }
    if (types.contains("BigDecimal")) {
        out.println "import java.math.BigDecimal;"
    }

    if (types.contains("InputStream")) {
        out.println "import java.io.InputStream;"
    }
    out.println ""
    String comment = table.comment.toString()
    out.println "/**\n" +
            " * @Description " + comment + "\n" +
            " * @Author " + System.getenv("USERNAME") + "\n" + //1. 修改idea为自己名字
            " * @Date " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " \n" +
            " */"
    out.println ""
    out.println "@Data"
    out.println "@Entity"
    //2. schema = \"后面添加自己的表空间名称(mysql可以不添加, 不用这个schema属性也行)
    out.println "@Table(name = \"" + table.getName() + "\")"
    out.println "@org.hibernate.annotations.Table(appliesTo = \"" + table.getName() + "\", comment = \"" + comment + "\")"
    out.println "public class $className implements Serializable {"
    out.println ""
    out.println genSerialID()
    fields.each() {
        // 输出注释
//        if (isNotEmpty(it.comment)) {
//            out.println "\t/**"
//            out.println "\t * ${it.comment.toString()}"
//            out.println "\t */"
//        }
        if (isNotEmpty(it.comment)) {
            out.println """
   /**
    * ${it.comment.toString()}
    */"""
        }
        if ((it.annos+"").indexOf("[@Id]") >= 0) {
            out.println "\t@Id"
            out.println "\t@GeneratedValue(strategy=GenerationType.IDENTITY)"
        }
        if (it.annos != "") out.println "   ${it.annos.replace("[@Id]", "")}"
        if ((it.type + "").equals("Date")) out.println "\t@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\", timezone = \"GMT+8\")"
        // 输出成员变量
//    out.println "\tprivate ${it.type} ${it.name};"
        out.println "\tprivate ${it.type} ${it.property};"
    }

//    // 输出get/set方法
//    fields.each() {
//        out.println ""
//        out.println "\tpublic ${it.type} get${it.name.capitalize()}() {"
//        out.println "\t\treturn this.${it.name};"
//        out.println "\t}"
//        out.println ""
//
//        out.println "\tpublic void set${it.name.capitalize()}(${it.type} ${it.name}) {"
//        out.println "\t\tthis.${it.name} = ${it.name};"
//        out.println "\t}"
//    }
//
//    // 输出toString方法
//    out.println ""
//    out.println "\t@Override"
//    out.println "\tpublic String toString() {"
//    out.println "\t\treturn \"{\" +"
//    fields.each() {
//        out.println "\t\t\t\t\t\"${it.name}='\" + ${it.name} + '\\'' +"
//    }
//    out.println "\t\t\t\t'}';"
//    out.println "\t}"

    out.println ""
    out.println "}"
}

def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) { fields, col ->
        def spec = Case.LOWER.apply(col.getDataType().getSpecification())

        def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value
        def colData = "\t@Column(name = \"" + col.getName() + "\")";
        if ("create_time".equals(Case.LOWER.apply(col.getName()))) colData = "\t@Column(name = \"" + col.getName() + "\", updatable = false)";
        if ("update_time".equals(Case.LOWER.apply(col.getName()))) colData = "\t@Column(name = \"" + col.getName() + "\", updatable = false)";
        def comm = [
                colName : col.getName(),
//                name    : javaName(col.getName(), false),//属性值转驼峰格式
                name    : col.getName(),//属性值格式跟数据库相同
                property: changeStyle(col.getName(), true), //实体类属性，下划线转驼峰式
                type    : typeStr,
                comment: col.getComment(),
//                annos   : "\t@Column(name = \"" + col.getName() + "\" )"]
                annos   : colData]
        if ("id".equals(Case.LOWER.apply(col.getName())))
            comm.annos += ["@Id"]
        if ("uuid".equals(Case.LOWER.apply(col.getName())))
            comm.annos += ["@Id"]
        fields += [comm]
    }
}

// 这里是处理数据库表前缀的方法，这里处理的是test_xxx命名的表
// 已经修改为使用javaName, 如果有需要可以在def className = javaName(table.getName(), true)中修改为javaClassName
// 处理类名（这里是因为我的表都是以test_命名的，所以需要处理去掉生成类名时的开头的T，
// 如果你不需要去掉表的前缀，那么请查找用到了 javaClassName这个方法的地方修改为 javaName 即可）
def javaClassName(str, capitalize) {
    def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
            .collect { Case.LOWER.apply(it).capitalize() }
            .join("")
            .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
    s = s[4..s.size() - 1]// 去除开头的test
    capitalize || s.length() == 1 ? s : Case.LOWER.apply(s[0]) + s[1..-1]
}

//Repository类名，capitalize=true,类名首字母小写
def javaName(str, capitalize) {
//    def s = str.split(/(?<=[^\p{IsLetter}])/).collect { Case.LOWER.apply(it).capitalize() }
//            .join("").replaceAll(/[^\p{javaJavaIdentifierPart}]/, "_")
//    capitalize || s.length() == 1? s : Case.LOWER.apply(s[0]) + s[1..-1]
//  def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
//          .collect { Case.LOWER.apply(it).capitalize() }
//          .join("")
//          .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
//  capitalize || s.length() == 1 ? s : Case.LOWER.apply(s[0]) + s[1..-1]
//}
    def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
            .collect { Case.LOWER.apply(it).capitalize() }
            .join("")
            .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
    capitalize || s.length() == 1 ? s : Case.LOWER.apply(s[0]) + s[1..-1]
}

def isNotEmpty(content) {
    return content != null && content.toString().trim().length() > 0
}

static String changeStyle(String str, boolean toCamel) {
    if (!str || str.size() <= 1)
        return str

    if (toCamel) {
        String r = str.toLowerCase().split('_').collect { cc -> Case.LOWER.apply(cc).capitalize() }.join('')
        return r[0].toLowerCase() + r[1..-1]
    } else {
        str = str[0].toLowerCase() + str[1..-1]
        return str.collect { cc -> ((char) cc).isUpperCase() ? '_' + cc.toLowerCase() : cc }.join('')
    }
}

//生成序列化的serialVersionUID
static String genSerialID() {
    return "\tprivate static final long serialVersionUID =  " + Math.abs(new Random().nextLong()) + "L;"
}