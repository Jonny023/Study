### 无主键批量更新

> sql如： update table set xxx = 1 where a_id = 1 and b_id = 2

```java
@Override
public void change(GoodPriceUpdateVO goodPriceUpdateVO) {
    List<GoodPriceUpdateVO.PriceList> priceList = goodPriceUpdateVO.getPriceList();
    List<GoodsPrice> goodsPrices = priceList.stream().map(item -> {
        GoodsPrice goodsPrice = OrikaMapperUtil.INSTANCE.copyProperties(item, GoodsPrice.class);
        goodsPrice.setGoodsId(goodPriceUpdateVO.getGoodsId());
        return goodsPrice;
    }).collect(Collectors.toList());

    boolean result = this.updateBatchByQueryWrapper(goodsPrices, item -> Wrappers.<GoodsPrice>lambdaUpdate()
            .eq(GoodsPrice::getGoodsId, item.getGoodsId())
            .eq(GoodsPrice::getPricetypeId, item.getPricetypeId())
    );
    if (!result) {
        throw new BizException("更新失败，原因是数据不存在");
    }
}

public boolean updateBatchByQueryWrapper(Collection<GoodsPrice> entityList, Function<GoodsPrice, LambdaUpdateWrapper<GoodsPrice>> wrapperFunction) {
    String sqlStatement = this.getSqlStatement(SqlMethod.UPDATE);
    return this.executeBatch(entityList, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
        Map<String, Object> param = CollectionUtils.newHashMapWithExpectedSize(2);
        param.put(Constants.ENTITY, entity);
        param.put(Constants.WRAPPER, wrapperFunction.apply(entity));
        sqlSession.update(sqlStatement, param);
    });
}
```
