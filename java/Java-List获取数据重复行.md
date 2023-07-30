# 根据组合key获取重复行号

```java
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Main {
    public static void main(String[] args) {
        List<Goods> list = Lists.newArrayList(
                new Goods(1, "可乐", "001", 1),
                new Goods(2, "可乐", "001", 2),
                new Goods(3, "可乐", "001", 3),
                new Goods(4, "柠檬", "002", 4),
                new Goods(5, "柠檬", "002", 5)
        );

        Map<String, List<Integer>> duplicateMap = list.stream()
                .collect(Collectors.groupingBy(
                        g -> g.getGoodsName() + g.getCode(), // 按goodsName+code分组
                        Collectors.mapping(Goods::getRowNo, Collectors.toList()) // 记录行号
                ));

        list.forEach(g -> {
            String key = g.getGoodsName() + g.getCode();
            List<Integer> rowNos = duplicateMap.get(key);
            if (rowNos != null && rowNos.size() > 1) {
                int currentRowNo = g.getRowNo();
                List<Integer> duplicateRows = rowNos.stream()
                        .filter(no -> no != currentRowNo)
                        .collect(Collectors.toList());
                if (!duplicateRows.isEmpty()) {
                    System.out.println("【" + key + "】与" + duplicateRows + "行重复");
                }
            }
        });
        //打印结果：
        //【可乐001】与[2, 3]行重复
        //【可乐001】与[1, 3]行重复
        //【可乐001】与[1, 2]行重复
        //【柠檬002】与[5]行重复
        //【柠檬002】与[4]行重复
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Goods {
        private Integer id;
        private String goodsName;
        private String code;
        private Integer rowNo;
    }
}

//获取元素下表（无序） 集合元素可以重复，重复的下标放到一个集合中{80=[5, 6], 3=[2], 4=[3], 20=[1], 5=[4], 10=[0]}
Map<Integer, List<Integer>> unOrder = IntStream.range(0, ints.size())
        .boxed().collect(Collectors.groupingBy(ints::get, Collectors.toList()));

//获取元素下表（有序） 集合元素可以重复，重复的下标放到一个集合中{10=[0], 20=[1], 3=[2], 4=[3], 5=[4], 80=[5]}
Map<Integer, List<Integer>> orderResult = IntStream.range(0, ints.size())
        .boxed().collect(Collectors.groupingBy(ints::get, LinkedHashMap::new, Collectors.toList()));

//返回map 集合元素不能重复，重复则报错Duplicate key xxx {80=5, 3=2, 4=3, 20=1, 5=4, 10=0}
Map<Integer, Integer> result = IntStream.range(0, ints.size())
        .boxed()
        .collect(Collectors.toMap(ints::get, Function.identity()));

```
