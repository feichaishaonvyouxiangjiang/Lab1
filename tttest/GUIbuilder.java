package test.tttest;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.event.*;
import java.net.URL;
import java.io.*;

import javax.imageio.ImageIO;

public class GUIbuilder extends JFrame implements ActionListener{
  JTextField filename;
  JTextArea textIn;
  JTextArea textOut;
  JTextField pathStart;
  JTextField pathEnd;
  TestListG g;
  

  public void GuiApp(){
    g=null;
    JFrame jf = new JFrame("有向图");
    Container con = jf.getContentPane();
    JButton createDG = new JButton("创建有向图");
    JButton showDG = new JButton("显示有向图");
    JButton queryBW = new JButton("查询桥接词");
    JButton generateNT = new JButton("生成新文本");
    JButton calcSP = new JButton( "计算最短路径 ");
    JButton walkBt=new JButton(" 随机游走  ");
    con.setLayout(new FlowLayout(2,10,10));

    filename = new JTextField("输入文件名",30);
    pathEnd = new JTextField("path end",30);
    pathStart = new JTextField("path start",30);
    textOut = new JTextArea(10,30);
    textIn = new JTextArea(10,30);
    

    JScrollPane StextOut = new JScrollPane(textOut);



    textOut.setText("out");
    textIn.setText("in");

    con.add(filename);
    con.add(pathEnd);
    con.add(pathStart);
    con.add(textIn);
    con.add(StextOut);

    createDG.addActionListener(this);
    con.add(createDG);

    showDG.addActionListener(this);
    con.add(showDG);

    queryBW.addActionListener(this);
    con.add(queryBW);

    generateNT.addActionListener(this);
    con.add(generateNT);

    calcSP.addActionListener(this);
    con.add(calcSP);
    
    walkBt.addActionListener(this);
    con.add(walkBt);

    jf.setVisible(true);
    jf.setSize(365,600);
    jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }


  public void actionPerformed(ActionEvent e){
    String act = e.getActionCommand();
    if(act == "创建有向图"){
      g = TestListG.createDirectedGraph(filename.getText());
    }
   // C:/Users/10953/Desktop/a/test001.txt
    //E:/workspace/tttest/
    else if(act == "显示有向图"){
      TestListG.showDirectedGraph(g);
      new PictureDialog(this,"adjGraph.gif").setVisible(true);
    }
    else if(act == "查询桥接词"){
      textOut.setText(TestListG.queryBridgeWords(g,pathStart.getText().toLowerCase(),pathEnd.getText().toLowerCase()));
    }
    else if(act == "生成新文本"){
      textOut.setText(TestListG.generateNewText(g,textIn.getText()));
    }
    else if(act == " 随机游走  ")
    {
    	String lala=TestListG.randomWalk(g);
    }
    else{ //计算最短路径
      String t = pathEnd.getText();
      if(t!=null && t.compareTo("")!=0){
        String sss = TestListG.calcShortestPath(g,pathStart.getText().toLowerCase(),t.toLowerCase()) ;
        textOut.setText( (sss.compareTo("")==0)?"NO PATH!":sss );
        if((sss.compareTo("")!=0)){
          new PictureDialog(this,"adjGraphcalc.gif").setVisible(true);
        }
      }
      else{
        String emmm = TestListG.calcShortestPath(g,pathStart.getText().toLowerCase()) ;
        textOut.setText( emmm.compareTo("")==0?"NO PATH!":emmm );
        if(emmm.compareTo("")!=0){
          TestListG.printPicture(emmm,g);
        }

      }

    }
  }

 

   public static void main(String[] args){
     new GUIbuilder().GuiApp();
   }

}



class PictureDialog extends JDialog{
  public PictureDialog(GUIbuilder f, String picname){
    super(f,picname,true);
    Container con = getContentPane();
    JLabel jl = new JLabel();
   // URL url = PictureDialog.class.getResource(picname);
    Icon icon = new ImageIcon(picname);
    jl.setIcon(icon);
    con.add(new JScrollPane( jl));

    try{
    File ppp = new File(picname);
    BufferedImage sss = ImageIO.read(ppp);

    int w = sss.getWidth();
    int h = sss.getHeight();
    setBounds(120,120,w,400);
  }catch(IOException e){e.printStackTrace();}

  }

}
