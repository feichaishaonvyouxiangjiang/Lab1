package test.tttest;

import java.util.Random;
import java.util.Scanner;
import java.io.*;

public class MyList{ //int链表
    class MyNode{ //节点
      int perVer;
      MyNode nextNode;
    }
    class HeadNode{
      int shortestpath;
      boolean dirty;
      int perVer;
      MyNode fristNode; 
    }
    public static final int INFINITE = 2147483647;

    public HeadNode[] headList ;
    public MyList(int sum){
      headList = new HeadNode[sum];
    }

    public void setPathPerVer(int head , int tail ,int weight)
    {
      
      headList[tail]  = new HeadNode();
      headList[tail].shortestpath = weight;
      headList[tail].dirty = false;
      headList[tail].perVer = head;
      headList[tail].fristNode = null;
    }

    public void setPathPerVer(int head , int tail )
    {
      MyNode t = new MyNode();
      MyNode temp ;
      temp = headList[tail].fristNode;
      t.perVer = head;
      headList[tail].fristNode = t;
      t.nextNode = temp;

    }

    public int getMinCostPos(){
      int len = headList.length;
      int min = INFINITE;
      int minp = -1;
      for(int i =0 ;i < len; i ++){
        if(min >= headList[i].shortestpath && headList[i].dirty == false){
          min = headList[i].shortestpath;
          minp = i;
        }
      }
      
      return minp;
    }
}