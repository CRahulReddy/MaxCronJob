package com.Max.maxcreditcardcronjob.service.equifaxservice;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.Max.maxcreditcardcronjob.dao.CreditCardDAO;
import com.Max.maxcreditcardcronjob.model.Applicant;
import com.Max.maxcreditcardcronjob.service.creditcardservice.CreditCardService;

/**
 * This class implements the runnable interface.
 * 
 * @author Bootcamp User 021
 *
 */
public class EquifaxService implements Runnable {

	final Logger logger = Logger.getLogger(EquifaxService.class);
	CreditCardDAO creditCardDAO;
	Applicant applicant;

	public EquifaxService()
	{
		
	}
	public EquifaxService(Applicant applicant) {
		this.applicant = applicant;
	}

	public CreditCardDAO getCreditCardDAO() {
		return creditCardDAO;
	}

	public void setCreditCardDAO(CreditCardDAO creditCardDAO) {
		this.creditCardDAO = creditCardDAO;
	}

	
	/**
	 * This method contains the actual logic. This method is executed by threads.
	 */
	public void run() {

		System.out.println(Thread.currentThread().getName() + " (Start)  ");

		try {

			new CreditCardService().verifyApplicant(applicant);
			logger.info("the logic in run method has been invoked");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " (End)");// prints
																		// thread

	}

}
