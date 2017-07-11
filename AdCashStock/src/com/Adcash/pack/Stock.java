package com.Adcash.pack;

import java.io.IOException;

import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


@WebServlet(name = "Stock", urlPatterns = { "/Stock" })
public class Stock extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(Stock.class);

	public Stock() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//creating object of operation to call our various methods
		Operations rr = new Operations();
		//calling requestReceived to get the data into the log only.
		rr.requestReceived(request.getParameter("country"), request.getParameter("category"),
				String.valueOf(request.getParameter("basebid")));
		//checking for the base targeting conditions it will give us a list of qualified companies.
		rr.baseTargeting(request, response);
		//budget check and getting the highest bid company
		rr.budgetCheck(request, response);
		String resCompany = rr.baseBidCheck(request, response);
		try {
			rr.reducedBudget(request, response, resCompany);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		//to check out the screen shot function:  Please uncomment the following line
//		Screenshot sc = new Screenshot();
//		sc.databaseScreenshot(request, response);
//		sc.logScreenshot();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
	}

}
