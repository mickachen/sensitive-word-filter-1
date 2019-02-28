package com.oldmee.method1;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * 敏感词树
 * Created by ScienJus on 2015/7/19.
 */
public class SensitiveWordTree {

    // 日志
    private static final Logger LOG = Logger.getLogger("SensitiveWordTree");
    // 根节点
    private static SensitiveWordNode root = null;
    // 敏感词库编码
//    private static final String ENCODING = "utf-8";
    private static final String ENCODING = "gbk";
    // 敏感词库位置 （这个词库的位置自己要换掉）
//    private static final String filePath = "/Users/lirenren/intellijProjects/effective-java-3e-source-code/resource/1.txt";
    private static final String filePath = "D:/1.txt";

    /**
     * 读取敏感词库
     *
     * @return
     */
    private static Set readSensitiveWords() {
        Set keyWords = new HashSet();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), ENCODING));
            String line;
            while ((line = reader.readLine()) != null) {
                keyWords.add(line.trim());
            }
        } catch (UnsupportedEncodingException e) {
            LOG.info("敏感词库编码错误!");
        } catch (FileNotFoundException e) {
            LOG.info("敏感词库不存在!");
        } catch (IOException e) {
            LOG.info("读取敏感词库失败!");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOG.info("读取敏感词库失败!");
                }
            }
        }
        return keyWords;
    }

    /**
     * 初始化敏感词库
     */
    private static void init() {
        // 读取敏感词库
        Set<String> keyWords = readSensitiveWords();
        // 初始化根节点
        root = new SensitiveWordNode(' ');
        // 创建敏感词
        for (String keyWord : keyWords) {
            createSensitiveWordNode(keyWord);
        }
    }

    /**
     * 构建敏感词
     *
     * @param keyWord
     */
    private static void createSensitiveWordNode(String keyWord) {
        if (root == null) {
            LOG.info("根节点不存在!");
            return;
        }
        SensitiveWordNode nowNode = root;
        for (Character c : keyWord.toCharArray()) {
            SensitiveWordNode nextNode = nowNode.getNextNode(c);
            if (nextNode == null) {
                nextNode = new SensitiveWordNode(c);
                nowNode.putNextNode(nextNode);
            }
            nowNode = nextNode;
        }
        nowNode.setEnd(true);
    }

    /**
     * 检查敏感词
     *
     * @return 所有查出的敏感词
     */
    private static String censorWords(String text) {
        if (root == null) {
            init();
        }
        StringBuilder sensitiveWords = new StringBuilder();
        StringBuilder temp_sensitiveWords = new StringBuilder();
        char[] text_to_char = text.toCharArray();
        SensitiveWordNode sensitiveWordNode = root;
        SensitiveWordNode this_sensitiveWordNode = null;
        boolean flag;
        for (int start = 0; start < text_to_char.length; start++) {
            SensitiveWordNode temp_sensitiveWordNode = sensitiveWordNode.getNextNode(text_to_char[start]);

            if (temp_sensitiveWordNode != null || this_sensitiveWordNode != null
                    && this_sensitiveWordNode.getNextNode(text_to_char[start]) != null
                    && this_sensitiveWordNode.getNextNode(text_to_char[start]).getKey() == text_to_char[start]) {
                flag = true;

            } else {
                flag = false;
                temp_sensitiveWords = new StringBuilder();
                this_sensitiveWordNode = null;
            }

            if (flag) {

                if (this_sensitiveWordNode != null
                        && this_sensitiveWordNode.getNextNode(text_to_char[start]) == null) {
                    if (this_sensitiveWordNode.isEnd()) {
                        sensitiveWords.append(temp_sensitiveWords + ",");
                        this_sensitiveWordNode = null;
                    }
                    temp_sensitiveWords = new StringBuilder();
                }

                if (temp_sensitiveWordNode != null && temp_sensitiveWordNode.isEnd() || this_sensitiveWordNode != null
                        && this_sensitiveWordNode.getNextNode(text_to_char[start]) != null
                        && this_sensitiveWordNode.getNextNode(text_to_char[start]).getNextNodes() != null
                        && this_sensitiveWordNode.getNextNode(text_to_char[start]).isEnd()
                        && (start == text_to_char.length - 1
                            || this_sensitiveWordNode.getNextNode(text_to_char[start]).getNextNodes().size() == 0)) {
                    temp_sensitiveWords.append(text_to_char[start] + ",");
                    sensitiveWords.append(temp_sensitiveWords);
                    temp_sensitiveWords = new StringBuilder();
                    this_sensitiveWordNode = null;
                } else {
                    temp_sensitiveWords.append(text_to_char[start]);
                    if (this_sensitiveWordNode == null) {
                        this_sensitiveWordNode = temp_sensitiveWordNode;
                    }
                    if (this_sensitiveWordNode != null
                            && this_sensitiveWordNode.getNextNode(text_to_char[start]) != null
                            && this_sensitiveWordNode.getNextNode(text_to_char[start]).getNextNodes().size() != 0) {
                        this_sensitiveWordNode = this_sensitiveWordNode.getNextNode(text_to_char[start]);
                    }
                }

            }

        }
        return sensitiveWords.length() > 0 ?
                sensitiveWords.subSequence(0, sensitiveWords.length() - 1).toString()
                : "未匹配到敏感词";
    }

    public static void main(String[] args) {
        long start_time = System.currentTimeMillis();
        init();
        System.out.println(System.currentTimeMillis() - start_time);
        System.out.println(root);
        start_time = System.currentTimeMillis();
        String list = censorWords("语文数，学数学，学英英语语文文");
        System.out.println(System.currentTimeMillis() - start_time);
        System.out.println(list);
    }

}
