> 动态添加`input`

```html
<template>
  <div>
    <input type="text" v-for="(item,i) of items" v-model="items[i]" :key="i">
    <button @click="onAdd">添加</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      items: []
    }
  },
  methods: {
    onAdd() {
      this.items.push('')
    }
  }
}
</script>
```
