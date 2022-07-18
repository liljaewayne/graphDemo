# graphDemo

## 概念:

### KgTriple:

    三元组, 由3个Node组成

### PE:

    被完整修饰的三元组, 由3个NodeWithModifiers组成, 其中一个node只能被1个或0个Modifier修饰

### Modifier:

    修饰三元组, 由3个NodeWithModifiers组成, 用来修饰一个Node

### NodeWithModifiers:

    一个Node和其修饰三元组, 其修饰三元组是列表, 但这里认为其修饰三元组Modifiers如果存在, 则只能有1个

### Node:

    节点, 分为两种类型ConceptNode和RelationNode, 在这里可以忽略其两种子类型, 认为其只有name属性

## 需求:

根据PecategoryCreate.csv中的数据生成多个PE, PE需满足如下条件:

1. PE的2节点是PecategoryCreate的2节点, 且1,3节点都是在KgTriple中定义的可以与2节点可以直接相连的节点
2. PE的1节点 <b>必须包含</b>PecategoryCreate中定义的node1节点作为其Modifier
3. PE的3节点 <b>必须包含</b>PecategoryCreate中定义的node3节点作为其Modifier
4. PE的1,3节点的modifier必须是<b>最短修饰链</b>

## 数据

```
输入数据: 
input/KgTriples.csv ; 初始三元组
input/PecategoryCreate.csv ; 需要生成pe的初始数据

输出目录: output/
```


