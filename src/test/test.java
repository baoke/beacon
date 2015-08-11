package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.buptmap.action.PlaceAction;

public class test {
	public static void main(String[] args){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
		PlaceAction place1 = (PlaceAction)applicationContext.getBean("placeAction");
		PlaceAction place2 = (PlaceAction)applicationContext.getBean("placeAction");
		System.out.println(place1==place2);
	}
}
