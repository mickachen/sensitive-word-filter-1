package com.oldmee.method1;

import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词树的节点
 * Created by ScienJus on 2015/7/19.
 */
public class SensitiveWordNode {

    // 节点所代表的字符
    private char key;

    // 节点的子节点
    private Map<Character,SensitiveWordNode> nextNodes;

    // 该节点是否为 End 节点
    private boolean end;

    public SensitiveWordNode (char key) {
        this.key = key;
        nextNodes = new HashMap<>();
        end = false;
    }

    public SensitiveWordNode getNextNode (char key) {
        return nextNodes.get(key);
    }

    public void putNextNode (SensitiveWordNode node) {
        nextNodes.put (node.getKey (), node);
    }

    public char getKey () {
        return key;
    }

    public void setKey (char key) {
        this.key = key;
    }

    public Map getNextNodes () {
        return nextNodes;
    }

    public void setNextNodes (Map nextNodes) {
        this.nextNodes = nextNodes;
    }

    public boolean isEnd () {
        return end;
    }

    public void setEnd (boolean end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "SensitiveWordNode{" +
                "key=" + key +
                ", nextNodes=" + nextNodes +
                ", end=" + end +
                '}';
    }
}
