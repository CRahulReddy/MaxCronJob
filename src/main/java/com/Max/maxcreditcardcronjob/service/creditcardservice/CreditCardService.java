package com.Max.maxcreditcardcronjob.service.creditcardservice;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.Max.maxcreditcardcronjob.dao.CreditCardDAO;
import com.Max.maxcreditcardcronjob.model.Applicant;
import com.Max.maxcreditcardcronjob.model.CreditCard;
import com.Max.maxcreditcardcronjob.service.equifaxservice.EquifaxService;
import com.Max.maxcreditcardcronjob.service.mailservice.SendMailSSL;





/**
 * This class contains method that are going to perform appropriate operation .
 * @author Bootcamp User 021
 *
 */
public class CreditCardService {

	final Logger logger = Logger.getLogger(CreditCardService.class);
	CreditCardDAO creditCardDAO;

	public CreditCardDAO getCreditCardDAO() {
		return creditCardDAO;
	}

	public void setCreditCardDAO(CreditCardDAO creditCardDAO) {
		this.creditCardDAO = creditCardDAO;
	}

	

	
	/**
	 * This method is used to get all the applicants whose application status is new and process them accordingly.
	 */
	public void processApplications() {
		List<Applicant> applicants = creditCardDAO.getApplicationsList();

		ExecutorService executor = Executors.newFixedThreadPool(5);

		Iterator itr = applicants.iterator();

		while (itr.hasNext()) {
			
			Runnable worker = new EquifaxService((Applicant) itr.next());
			logger.info("A task has been assigned to a thread in thread pool");
			executor.execute(worker);
			/* futures.add(executor.submit(worker)); */
		}
		Boolean allTasksDone = true;

		executor.shutdown();
		while (!executor.isTerminated()) {
		}

		
	}

	
	/**
	 * This method is used to process the applicant information and sets the status as approved or rejected.
	 * @param applicant
	 * @throws IOException
	 */
	public void verifyApplicant(Applicant applicant) throws IOException {
		//creating the client
Client client=   ClientBuilder.newClient();
String url="http://localhost:8080/FicoWebserviceUsingRest/rest/customer/"+applicant.getSsn();
//setting the target

WebTarget target=((javax.ws.rs.client.Client) client).target(url);
	
//getting the response
String line=target.request(MediaType.TEXT_PLAIN).get(String.class);
		System.out.println(line);
		while (!line.equals("no datafound")) {
			String feilds[] = line.split(" ");
			logger.info("a record has been received from the database"+line);
			if (applicant.getSsn().equals(feilds[4])) {
				if (Integer.parseInt(feilds[5]) > 700) {
					applicant.setProcessStatus("processed");
					applicant.setApplicationType("approved");

					String number_Limit = generateCreditCard(applicant);
					String message = ",Welcome to CRR CreditCard family.Your creditcard number and limit is  ";

					new SendMailSSL().sendMail(applicant.getEmail(), "CRR CreditCard ",
							applicant.getFirstName() + " " + applicant.getLastName() + message + number_Limit);

				} else {
					applicant.setProcessStatus("processed");
					applicant.setApplicationType("disapproved");
					new SendMailSSL().sendMail(applicant.getEmail(), "CRR CreditCard ",
							"Sorry " + applicant.getFirstName() + " " + applicant.getLastName()
									+ ", we couldnt approve credit card for you at this time.");
				}

				new CreditCardDAO().updateApplicant(applicant);

			}
line="no datafound";
		}
	}

	
	/**
	 * This method is used to generate the credit card. Once the status of the application is set to approved.
	 * @param applicant
	 * @return
	 */
	public String generateCreditCard(Applicant applicant) {
		CreditCard creditCard = new CreditCard();
		Random rnd = new Random();
		Calendar now = Calendar.getInstance();
		String year = String.valueOf(now.get(Calendar.YEAR) + 5);
		String month = String.valueOf(now.get(Calendar.MONTH) + 1);

		int creditCard1 = 100000000 + rnd.nextInt(900000000);
		int creditCard2 = 100000 + rnd.nextInt(900000);
		int cvv = 1000 + rnd.nextInt(9000);

		creditCard.setCreditCardNumber(String.valueOf(creditCard1) + String.valueOf(creditCard2));
		creditCard.setNameOnCard(applicant.getFirstName());
		creditCard.setCvv(String.valueOf(cvv));
		creditCard.setExpiryDate("month/" + year.substring(2));
		creditCard.setCardOwner_ID(applicant.getId());
		creditCard.setCreditLimit("10000");
		creditCard.setCardActivationStatus("new");

	
		System.out.println(creditCard);
		new CreditCardDAO().saveCreditCard(creditCard);
		
		return creditCard.getCreditCardNumber() + "," + creditCard.getCreditLimit();

	}

}
