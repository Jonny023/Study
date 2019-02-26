# vue使用el-tree实现全选、反选

* 下拉树全选、反选
* 根据输入字符过滤

```vue
<el-dialog
        class="dialog"
        :title="upLowTitle"
        :visible.sync="upLowVisble"
        width="40%"
        :before-close="handleClose"
        :close-on-click-modal="false"
      >
        <!-- custom-class="dialog-height" -->
        <div class="tree-layout">
          <el-input placeholder="输入关键字进行过滤" v-model="filterText"></el-input>
          <div class="tree">
            <!-- :load="upLowLoad"
            lazy-->
            <el-tree
              :data="channels"
              ref="channelTree"
              :props="channelProps"
              @check-change="upper_lowerChange"
              :default-expanded-keys="['']"
              default-expand-all
              :filter-node-method="filterNode"
              show-checkbox
              :indent="16"
              v-loading="upLowLoading"
              node-key="id"
            ></el-tree>
          </div>
        </div>
        <span slot="footer" class="dialog-footer dialog-tools">
          <el-button type="success" @click="checkAll">全选</el-button>
          <el-button type="warning" @click="inverse">反选</el-button>
          <el-button type="primary" @click="shelves">上架</el-button>
          <el-button type="danger" @click="obtained">下架</el-button>
          <el-button @click="upLowCancel">取消</el-button>
        </span>
      </el-dialog>
```



```javascript
export default {
  mixins: [listMixins],
  data() {
      return {
          upLowVisble: false, // 上下架显隐开关
          filterText: "", // 过滤字段
          channels: [], // 栏目初始数据
          channelProps: {
            label: "name",
            children: "children",
            isLeaf: "isChild",
            id: "id"
          },
          flag: false, // 属性是否选中标识
          isCheck: false,
          upLowLoading: true, // 遮罩层
          upLowTitle: "" // 上下架面板标题
      }
  },
    methods: {
		// 上下架弹窗
        add() {
          this.upLowVisble = true;
          this.upLowLoad(); // 加载栏目列表
        },
        // 渲染树
        upLowLoad() {
          let that = this;
          this.upLowLoading = true;
          axios
            .post(this.$api.allChannelList)
            .then(res => {
              this.upLowLoading = false;
              that.channels = res.body;
            })
            .catch(err => {
              this.upLowLoading = false;
            });
        },

        // 上下架树加载
        upper_lowerChange(node, checkStatus, childStatus) {
          if (checkStatus) {
            this.currentCheckChannelId = node.id;
          }
        },
        // 上下架节点过滤
        filterNode(value, data) {
          if (!value) return true;
          return data.name.indexOf(value) !== -1;
        },
        // 全选或反选
        checkAll() {
          this.flag = true;
          this.$refs.channelTree.setCheckedNodes(this.channels);
        },
        // 全选处理方法
        batchSelect(nodes, refs, flag, seleteds) {
          if (typeof nodes != "undefined") {
            nodes.forEach(element => {
              refs.setChecked(element, flag, true);
            });
          }

          if (typeof seleteds != "undefined") {
            seleteds.forEach(node => {
              refs.setChecked(node, !flag, true);
            });
          }
        },
        // 反选
        inverse() {
          let res = this.$refs.channelTree;
          let nodes = res.getCheckedNodes(true, true);
          this.batchSelect(this.channels, res, true, nodes);
        },
        // 上架
        shelves() {},
        // 下架
        obtained() {},

        // 取消
        upLowCancel() {
            this.upLowVisble = false;
            this.channels = [];
        }
    },
    created() {
        
    },
    watch: {
        // 根据关键词过滤
        filterText(val) {
          this.$refs["channelTree"].filter(val);
        }
    }
}
```

