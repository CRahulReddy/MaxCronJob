package com.Max.maxcreditcardcronjob.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

import com.Max.maxcreditcardcronjob.model.Applicant;
import com.Max.maxcreditcardcronjob.model.CreditCard;
import com.Max.maxcreditcardcronjob.service.equifaxservice.EquifaxService;

@Transactional
/**
 * This class contains methods which are used to interact with the database by setting information in table or by getting information form tables.
 * @author Bootcamp User 021
 *
 */
public class CreditCardDAO {

	@Autowired
	private SessionFactory sessionFactory;
	final Logger logger = Logger.getLogger(EquifaxService.class);

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	/**
	 * This method is used to get the list of applicants whose appliation status is new.
	 * @return
	 */
	public List<Applicant> getApplicationsList() {

		Session session = this.sessionFactory.openSession();
		Query query = session.createQuery("FROM Applicant WHERE applicationType = :username");
		query.setString("username", "New");
		List<Applicant> returnedMembers = query.list();
		session.close();
		logger.info("the list of applicants whose application status is new has been obtained");
		return returnedMembers;

	}

	
	/**
	 * this method is used to update the applicant reocred with the application status as approved or rejected.
	 * @param applicant
	 */
	public void updateApplicant(Applicant applicant) {
		Resource r = new ClassPathResource("spring-servlet.xml");
		BeanFactory factory = new XmlBeanFactory(r);
		SessionFactoryImplementor sessionFactory = (SessionFactoryImplementor) factory
				.getBean("hibernate4AnnotatedSessionFactory");

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
	
		System.out.println(applicant);
		session.update(applicant);
		t.commit();
		logger.info("applicant detials have been updated");
		session.close();

	}

	/**
	 * This method is used to save the credit card detials in the databse.
	 * @param creditCard
	 */
	public void saveCreditCard(CreditCard creditCard) {
		System.out.println("rahul " + getSessionFactory());
		Resource r = new ClassPathResource("spring-servlet.xml");
		BeanFactory factory = new XmlBeanFactory(r);
		SessionFactoryImplementor sessionFactory = (SessionFactoryImplementor) factory
				.getBean("hibernate4AnnotatedSessionFactory");
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
		
		
		session.persist(creditCard);
		logger.info("creditcard detials have been saved into database");
		t.commit();
		session.close();

	}

}
