package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;

/**
 * AbstractTermTuple的实现
 * @author lxiny
 */
public class TermTuple extends AbstractTermTuple {
    /**
     * 无参构造函数
     */
    public TermTuple() {}
    /**
     * 判断两个三元组内容是否相同
     * @param obj ：要比较的另外一个三元组
     * @return 如果内容相等（三个属性内容都相等）返回true，否则返回false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TermTuple tuple = (TermTuple) obj;
        return curPos == tuple.curPos && term.equals(tuple.term);
    }
    /**
     * 获得三元组的字符串表示
     * @return ： 三元组的字符串表示
     */
    @Override
    public String toString() {
        return "content: " + term.getContent() + "," +
                "freq: " + freq + "," +
                "curPos: " + curPos;
    }
}
