# JavaShark

自用 Java 反序列化链子集合， 主要参考于 [Eki-marshalexp](https://github.com/EkiXu/marshalexp)，主要用于解决 CTF Java 赛题。

## 结构
- src
  - dev.silente.javashark: 主包
    - gadget: 最小利用单元
    - poc: 一些常见的反序列化完整链
    - memshell: 收集一些内存马
    - solution: 比赛中的 Java 反序列化链
    - utils: 工具类
  - 其他包：一般都是覆写了一些类来达成利用目的
