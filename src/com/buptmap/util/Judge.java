package com.buptmap.util;
import java.awt.geom.Point2D;  
import java.util.List;
public class Judge {
	
	public  boolean checked(Point2D.Double point, Double r, List<Point2D.Double> polygon){
		double x0 = point.getX();
		double y0 = point.getY();
		Point2D.Double first = polygon.get(0);
		double x1,y1,x2,y2 = 0;
		if (checkWithJdkGeneralPath(point, polygon)) {
			return true;
		}
		else{
			for (int i = 0; i < polygon.size(); i++) {
				
				if (i !=  polygon.size() - 1) {
					x1 = polygon.get(i).getX();
					y1 = polygon.get(i).getY();
					x2 = polygon.get(i+1).getX();
					y2 = polygon.get(i+1).getY();
					if (pointToLine(x1, y1, x2, y2, x0, y0) < r) {
						return true;
					}
				}
				else {
					x1 = polygon.get(i).getX();
					y1 = polygon.get(i).getY();
					x2 = first.getX();
					y2 = first.getY();
					if (pointToLine(x1, y1, x2, y2, x0, y0) < r) {
						return true;
					}
				}
			}
			return false;
		}
		
	}

	 //计算(x0,y0)到线段(x1,y1),(x2,y2)的最短距离
	 public double pointToLine(double x1, double y1, double x2, double y2, double x0, double y0) {  
 
          double space = 0;  
 
          double a, b, c;  
 
          a = lineSpace(x1, y1, x2, y2);// 线段的长度  
 
          b = lineSpace(x1, y1, x0, y0);// (x1,y1)到点的距离  
 
          c = lineSpace(x2, y2, x0, y0);// (x2,y2)到点的距离  
 
          if (c+b == a) {//点在线段上  
 
             space = 0;  
 
             return space;  
 
          }  
 
          if (a <= 0.000001) {//不是线段，是一个点  
 
             space = b;  
 
             return space;  
 
          }  
 
          if (c * c >= a * a + b * b) { //组成直角三角形或钝角三角形，(x1,y1)为直角或钝角  
 
             space = b;  
 
             return space;  
 
          }  
 
          if (b * b >= a * a + c * c) {//组成直角三角形或钝角三角形，(x2,y2)为直角或钝角  
 
             space = c;  
 
             return space;  
 
          }  
   //组成锐角三角形，则求三角形的高  
          double p = (a + b + c) / 2;// 半周长  
 
          double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积  
 
          space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）  
 
          return space;  
 
      }  
 
     // 计算两点之间的距离  
     public double lineSpace(double x1, double y1, double x2, double y2) {  
 
          double lineLength = 0;  
 
          lineLength = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)  
 
                 * (y1 - y2));  
 
          return lineLength;  
 
 
      }  

     /** 
      * 返回一个点是否在一个多边形区域内 
      * @param point 
      * @param polygon 
      * @return 
      */  
    private boolean checkWithJdkGeneralPath(Point2D.Double point, List<Point2D.Double> polygon) {  
           java.awt.geom.GeneralPath p = new java.awt.geom.GeneralPath();  
  
           Point2D.Double first = polygon.get(0);  
           p.moveTo(first.x, first.y);  
           polygon.remove(0);  
           for (Point2D.Double d : polygon) {  
              p.lineTo(d.x, d.y);  
           }  
  
           p.lineTo(first.x, first.y);  
  
           p.closePath();  
  
           return p.contains(point);  
  
        }  
}
