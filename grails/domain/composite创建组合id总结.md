> 在`domain`中使用`mapping`映射(使用`composite`联合主键必须实现序列化接口)

static mapping = {
    //使用composite组合id是域类必须实现implements Serializable接口
    id composite:["menu","role"] 
    version false
}

* 必须重写`hashCode`和`equals`方法，否则可能无法保存数据
