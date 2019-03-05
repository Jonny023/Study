# 使用table时，对字段为数组的数据再次处理
* 通过`template`来实现
* 属性设置`slot-scope="scope"`
* 下面可通过`scope.row.xxx`获取到其中的值
  如：
 ```vue
 <span v-for="(item,index) in scope.row.xxx">{{item.age}}</span>
 
 <span v-key="index" v-for="(item,index) in scope.row.xxx">{{item.age}}</span>
 ```

```vue
<el-table-column label="操作">
  <template slot-scope="scope">
    <el-button
      size="mini"
      @click="handleEdit(indexMethod(scope.$index), scope.row)">编辑</el-button>
    <el-button
      size="mini"
      type="danger"
      @click="handleDelete(indexMethod(scope.$index), scope.row)">删除</el-button>
  </template>
</el-table-column>

```
