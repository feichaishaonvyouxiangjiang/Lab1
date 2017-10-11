package test.tttest;

import java.util.Random;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;
import java.io.*;
public class TestListG {

  public static final int INFINITE = 2147483647;
  public static final int NUMCON = 10000;
    class EdgeNode{ //边类
    int weight;
    int verpos;
    EdgeNode nextNode;
  }
  class VerNode{ //顶点类
    String word;
    EdgeNode firstedge;
  }

  public int versum; //顶点实际数
    public VerNode[] adjl;  //顶点数组，邻接表头

    public TestListG(int vmax) //构造函数1
    {
         versum = 0;
         adjl = new VerNode[vmax];
    }

    public TestListG() //构造函数2
    {
        versum = 0;
        adjl = new VerNode[NUMCON];
    }

    public int getPos(String vname) //获取单词在顶点数组的下标，不存在的单词返回-1
    {
      for(int i=0;i<versum;i++)
      {
        if(adjl[i].word.compareTo(vname)==0)
        {
          return i;
        }
      }

      return -1;
    }

    public void addEdge(int per_pos,int now_pos) //添加边
    {
        EdgeNode temp = adjl[per_pos].firstedge;
        boolean flag  = false;
        while(temp!=null )
        {
          if(temp.verpos == now_pos){
            temp.weight++;
            flag = true;
            break;
          }
          if(temp.nextNode == null){
                  break;
          }
          temp = temp.nextNode;
        }

        if(temp == null){
          adjl[per_pos].firstedge = new EdgeNode();
          adjl[per_pos].firstedge.weight = 1;
          adjl[per_pos].firstedge.nextNode = null;
          adjl[per_pos].firstedge.verpos = now_pos;
        }
        else if(temp.nextNode == null && flag == false){
          temp.nextNode = new EdgeNode();
          temp.nextNode.weight = 1;
          temp.nextNode.verpos = now_pos;
          temp.nextNode.nextNode = null;
        }
    }


    public void addVer(String str)//添加顶点
    {
        adjl[versum] = new VerNode();
        adjl[versum].word = str;
        adjl[versum].firstedge = null;
        versum ++;
    }

  //主程序
 /* public static void main(String[] args){
    TestListG g = null;
    Scanner scan = new Scanner(System.in);
    String str="aa";
    while(str.compareTo("end")!=0)
    {
      System.out.println("filename: ");
      if(scan.hasNext())
      {
        str = scan.next();
        g = createDirectedGraph(str);
       for(int i=0;i<g.versum;i++){
          System.out.print(g.adjl[i].word+" ");
          EdgeNode aaa = g.adjl[i].firstedge;
          
          while(aaa != null){
            System.out.print(g.adjl[aaa.verpos].word + " ");
            aaa = aaa.nextNode;
          }
          System.out.print("\n");
        }
        System.out.println(queryBridgeWords(g,"text","an"));
        showDirectedGraph(g);
         System.out.println(generateNewText(g,"too strange worlds"));
         System.out.println( calcShortestPath(g,"too") );

         System.out.println( calcShortestPath(g,"too","new") );


      }
    }
  }*/
  
    
    //生成有向图
  public  static TestListG createDirectedGraph(String filename){
    TestListG g = new TestListG();
    try{
  
    InputStream is = new FileInputStream(filename);
    int size = is.available();
    String[] wordList  = new String[NUMCON];//直接用数字了
    String str,per_str=null;
    int pos=0,a,per_pos,now_pos;
    char[] t = new char[30]; 
    for(int i=0; i< size; i++){
      a  = is.read() ;
      if((a<=90 && a>=65) || (a<=122 && a>=97))
      {
        if(a<=90 && a>=65){a += 32;}
        t[pos++]  = (char)a;
      }
      else if(pos!=0)
      {
        str = new String(t,0,pos);
        pos = 0;
        per_pos = g.getPos(per_str);
        now_pos = g.getPos(str);
        if(now_pos == -1){
          g.addVer(str);
        }
        if(per_pos!=-1){

        g.addEdge(per_pos,now_pos==-1?g.versum-1:now_pos);
      }
      per_str = str;
      }


    }
      is.close();
      
    }catch(IOException e){
      System.out.print("Exception");
    }  
    return g;
  }

  //展示有向图
  public static void showDirectedGraph(TestListG g){
    if (g == null)return;
    GraphViz gv  = new GraphViz();
    gv.addln(gv.start_graph());
    for(int i=0;i<g.versum;i++){
      String str = g.adjl[i].word;
      EdgeNode e = g.adjl[i].firstedge;
      while(e != null){
        gv.addln(str+" -> "+g.adjl[e.verpos].word +"[label = \""+e.weight+"\"];");
        e = e.nextNode;
      }
    }
    gv.addln(gv.end_graph());

    String type = "gif";
    File adjG = new File("adjGraph."+type);
   gv.writeGraphToFile(gv.getGraph( gv.getDotSource(),type),adjG);
   
  }

  //查询桥接词
  public static String queryBridgeWords(TestListG g,String word1,String word2){
    if (g == null)return "";
    int pos1 = g.getPos(word1.toLowerCase());
    int pos2 = g.getPos(word2.toLowerCase());
    StringBuffer bridge = new StringBuffer("");
    if(pos1 == -1 || pos2 == -1)return "No word1 or word2 in the graph!";
    else
    {
      EdgeNode e1 = g.adjl[pos1].firstedge;
      while(e1 != null){
        EdgeNode e2 = g.adjl[e1.verpos].firstedge;
        while(e2 != null){
          if(e2.verpos == pos2)
          {
            bridge.append(g.adjl[e1.verpos].word + " ");
          }
          e2 = e2.nextNode;
        }
        e1 = e1.nextNode;
      }
      if(bridge.length() == 0){
        return "No bridge words from word1 to word2!";
      }
      else{
        return "The bridge words from word1 to word2 are:"+bridge;
      }
    }
  }

  //根据桥接词生成新文本
  public static String generateNewText(TestListG g,String inputText){
    if (g == null)return "";
    StringBuffer result = new StringBuffer("");
    String[] input = inputText.split(" ");
    String sss = "The bridge words from word1 to word2 are:";
    if(input.length>=1){result.append(input[0]+" ");}
    for(int i = 1;i < input.length;i++){
      String sour_str = queryBridgeWords(g,input[i-1],input[i]);
      if(sour_str.startsWith(sss))
      {
        String str = sour_str.substring(sss.length());
        String[] bridges = str.split(" ");
        Random r = new Random();
        int j = r.nextInt(bridges.length );
        result.append(bridges[j] + " " + input[i] + " ");
      }
      else{
          result.append(input[i]+" ");
        }
    }

    return result+"";
  }
  //计算两个单词最短路径（图有变化，最好所有最短路径都做出来）
  public static String calcShortestPath(TestListG g,String word1,String word2){
    if (g == null || g.getPos(word1) == -1 || g.getPos(word2) == -1){return "";}
    String t = calcShortestPath(g,word1);
    String[] l = t.split("[a-z]*'s:\n");
    int p2 = g.getPos(word2);
    String[] path = l[p2+1].split("\n");

    GraphViz gv  = new GraphViz();
    gv.addln(gv.start_graph());
    for(int i=0;i<g.versum;i++){
      String str = g.adjl[i].word;
      EdgeNode e = g.adjl[i].firstedge;
      while(e != null){
        boolean flag =  false;
        String emmm = str+" -> "+g.adjl[e.verpos].word;
        for(int j =0;j<path.length;j++){
          if(path[j].indexOf(emmm) != -1)
          {
            flag = true;
            gv.addln(str+" -> "+g.adjl[e.verpos].word +"[color=\""+gv.COLOR[j%30]+"\","+ " label = \""+e.weight+"\"];");
            // break;

          }
        }
        if(flag ==false)
        {
          gv.addln(str+" -> "+g.adjl[e.verpos].word +"[label = \""+e.weight+"\"];");
        }
        e = e.nextNode;
        }
        
    }

    

    gv.addln(gv.end_graph());

    String type = "gif";
    File adjG = new File("adjGraphcalc."+type);
   gv.writeGraphToFile(gv.getGraph( gv.getDotSource(),type),adjG);

    return l[p2+1];
  }

  //计算最短路径重载版
  public static String calcShortestPath(TestListG g,String word1){
    if (g == null ||  g.getPos(word1) == -1)return "";
    int p = g.getPos(word1);
    MyList l  = new MyList(g.versum);
    EdgeNode temp = g.adjl[p].firstedge;
    StringBuffer sb = new StringBuffer("");

    if(temp == null)return "";//没有出路时
    //l的初始化
    for(int i = 0; i<g.versum ; i++){
      l.setPathPerVer(p,i,INFINITE);
    }
    while(temp != null){ 
      l.setPathPerVer(p,temp.verpos,temp.weight);
      temp = temp.nextNode;
     }
    l.headList[p].dirty = true;

    for(int i = 0; i<g.versum -1 ; i++){
      int a = l.getMinCostPos();
      if(a==-1)break;
      System.out.print("a = "+a+" ");
      System.out.print(g.adjl[a].word+" ");
      int s = l.headList[a].shortestpath;
      System.out.println("s = "+s);
      l.headList[a].dirty = true;
      temp = g.adjl[a].firstedge;
      while(temp != null){
    	if(s+temp.weight < l.headList[temp.verpos].shortestpath){
          l.headList[temp.verpos].shortestpath = s+temp.weight;
          l.headList[temp.verpos].perVer = a;
          l.headList[temp.verpos].fristNode = null;
        }
        else if(s+temp.weight == l.headList[temp.verpos].shortestpath){
          l.setPathPerVer(a,temp.verpos);
        }
        temp = temp.nextNode;
      }
    }
    for(int i = 0;i<g.versum;i++){
    	if(l.headList[i].shortestpath == INFINITE)sb.append("|");
    	else  sb.append(reseverPath(g,l,i,p,""));
      sb.append(";");

    }

    //字符串处理
    StringBuffer result = new StringBuffer("");
    String[] res = sb.toString().split(";");
    for(int i=0; i<res.length ;i++){ //不同终点
      result.append(g.adjl[i].word +"'s:\n");

      StringBuffer tt = new StringBuffer("");
      String[] tm = res[i].split("\\|");//路径倒叙数组
      for(int j=0;j<tm.length;j++){//同尾巴不同最短路径
        String[] tm2 = tm[j].split(" ");
        StringBuffer ttt = new StringBuffer("");
        for(int k = tm2.length -1;k>1;k--){
          ttt.append(tm2[k]+" -> ");
        }
        
        if(tm2.length>=2)ttt.append(tm2[1]);
        tm[j] = ttt.toString();
        tt.append(tm[j]+"\n");
      }
      res[i] = tt.toString();
      result.append(res[i]);

    }





   return result.toString();
    
  }

  //图片生成
  public static void printPicture(String result,TestListG g){
    if(result.compareTo("")==0)return ;
    String[] res = result.split("[a-z]*'s:\n");
    for(int h = 0;h<res.length;h++){
    String[] path = res[h].split("\n");
    GraphViz gv  = new GraphViz();
    gv.addln(gv.start_graph());
    for(int i=0;i<g.versum;i++){
      String str = g.adjl[i].word;
      EdgeNode e = g.adjl[i].firstedge;
      while(e != null){
        boolean flag =  false;
        String emmm = str+" -> "+g.adjl[e.verpos].word;
        for(int j =0;j<path.length;j++){
          if(path[j].indexOf(emmm) != -1)
          {
            flag = true;
            gv.addln(str+" -> "+g.adjl[e.verpos].word +"[color=\""+gv.COLOR[j%30]+"\","+ " label = \""+e.weight+"\"];");
            

          }
        }
        if(flag ==false)
        {
          gv.addln(str+" -> "+g.adjl[e.verpos].word +"[label = \""+e.weight+"\"];");
        }
        e = e.nextNode;
        }
        
    }

    gv.addln(gv.end_graph());

    String type = "gif";
    File adjG = new File("adjGraphcalcemmm"+h+"."+type);
   gv.writeGraphToFile(gv.getGraph( gv.getDotSource(),type),adjG);
}

  }
  public static String randomWalkAssit(TestListG G) {
		String res = new String();
		Random ran = new Random();
		int i = ran.nextInt(G.versum);// 随机游走的起点
		String[] edgeArr=new String[NUMCON];
		VerNode[] verArr = new VerNode[NUMCON];
		EdgeNode edgeTmp;
		int count = 0;
		res=G.adjl[i].word;//起点
		verArr[count]=G.adjl[i];
		edgeTmp = G.adjl[i].firstedge;
	
		while (edgeTmp != null)// 有出边
		{
			EdgeNode tmp;
			tmp = edgeTmp;
			int outLine = 1;
			// 先统计有几条出边 ，选一条
			while (tmp.nextNode!= null) {
				outLine++;
				tmp=tmp.nextNode;
			}
			int j = ran.nextInt(outLine);
			for (int k = 0; k < j; k++) {
				edgeTmp = edgeTmp.nextNode;// 下个节点
			}
			String temp=new String();
			temp=G.adjl[i].word+G.adjl[edgeTmp.verpos].word;//new edge
			for (int k = 0; k < count; k++) {
				if (temp.equals(edgeArr[k])) {
					verArr[count++] = G.adjl[edgeTmp.verpos];
					return res + " "+G.adjl[edgeTmp.verpos].word;
				}
			}
			edgeArr[count++] = temp;
			i=edgeTmp.verpos;
			edgeTmp = G.adjl[i].firstedge;
			
			verArr[count++] = G.adjl[i];
			res = res + " " + G.adjl[i].word;
		}
		//System.out.println(res);
		return res;
	}

	// 随机游走 最好图有高亮显示
	public static String randomWalk(TestListG G) {

		final TestListG g = G;
		final JLabel lab = new JLabel();
		String re = new String("aa");
		final JFrame jf = new JFrame("随机游走");
		
		
		JButton startBt = new JButton("开始");
		JButton stopBt = new JButton("停止");

		startBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lab.setText(randomWalkAssit(g));
				lab.setVisible(true);
				
			}
		});
		stopBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jf.dispose();
			}
		});

		jf.setSize(500, 200);
		jf.setLayout(new BorderLayout());
		jf.add(startBt, BorderLayout.WEST);
		jf.add(stopBt, BorderLayout.EAST);
		jf.add(new JScrollPane(lab), BorderLayout.CENTER);
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.setVisible(true);

		return re;

	}
    
  public static String reseverPath(TestListG g ,  MyList l , int start,int end , String father){
    StringBuffer str = new StringBuffer("");
    StringBuffer f = new StringBuffer(father +" "+ g.adjl[start].word );
    int p  = l.headList[start].perVer;
    if(l.headList[start].shortestpath == INFINITE){return " ";}//没有最短路径
    if(p == end){
      f.append(" "+g.adjl[p].word+"|");
      return f.toString();
    }
    str.append(reseverPath(g,l,p,end,f.toString()));
    MyList.MyNode e = l.headList[start].fristNode;
    while(e!=null){
      p = e.perVer;
      str.append(reseverPath(g,l,p,end,f.toString()));
      e = e.nextNode;
    }
    return str.toString();
  }
}


