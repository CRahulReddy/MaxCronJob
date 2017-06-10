package com.Max.maxcreditcardcronjob;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.Max.maxcreditcardcronjob.service.creditcardservice.CreditCardService;


/**
 * This is the main method where the application is triggered.
 * @author Bootcamp User 021
 *
 */
public class App {

	/**
	 * In this method we are loading the spring congfiguration file and getting the instance of factory.
	 * @param args
	 */
	public static void main(String[] args) {
		
		final Logger logger = Logger.getLogger(App.class);
		logger.info("main method has been invoked");

		ApplicationContext context = new ClassPathXmlApplicationContext("spring-servlet.xml");
		//BeanFactory factory = new XmlBeanFactory(r);
		CreditCardService creditCardService = (CreditCardService) context.getBean("service");
		creditCardService.processApplications();

	}
}
