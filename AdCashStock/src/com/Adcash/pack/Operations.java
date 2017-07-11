package com.Adcash.pack;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class Operations {
	
	static Logger logger = Logger.getLogger("Operations");
	// this is to record the details filled by clients.
	public synchronized void requestReceived(String country, String category, String basebid) {
		LoggerUtil.initLogger();
		Thread reqReceivedTh = new Thread(new Runnable() {
			public void run() {
				System.out.println(country + " " + category + " " + basebid);
				logger.info(
						"Details for Current bid:- Country: " + country + " Category: " + category + " BaseBis: " + basebid);
			
				
			}
		});
		reqReceivedTh.start();
	}
// this function will select the company which is passing the country and category 
	public  List<String> baseTargeting(HttpServletRequest request, ServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		String country = request.getParameter("country");
		String category = request.getParameter("category");
		String basebid = request.getParameter("basebid");
		List<String> result = new ArrayList<String>();
		if(country == "" ){
			out.println("No Countries Passed from Targeting");
			logger.error("No Countries Passed from Targeting ");
			return result;
		}else if(category ==""){
			out.println("No Category Passed from Targeting");
			logger.error("No Countries Passed from Targeting ");
			return result;
		}else if(basebid ==""){
			out.println("No BaseBid Passed from Targeting");
			logger.error("No BaseBid Passed from Targeting ");
			return result;			
		}		
		try {
			Connection con = (Connection) request.getSession().getServletContext().getAttribute("DBConnection");
			logger.info("DB Connection initialized successfully.");;
			System.out.println("Company after base targeting");
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = con.prepareStatement(
						"select * from companystock");
				rs = ps.executeQuery();
				int count = 0;
				while (rs.next()) {
					count++;
					String rscompany = rs.getString("companyid");
					String rscountry = rs.getString("countries");
					String rscategory = rs.getString("category");
					int matchCountry = matcher(country, rscountry);
					int matchCategory = matcher(category, rscategory);
					if(matchCountry == 1&& matchCategory ==1){
						System.out.println(rscompany+","+"passed");
						logger.info("BaseTargeting:"+rscompany+","+ "passed");
					}
					else{
						System.out.println(rscompany+","+"Failed");
						logger.info("BaseTargeting:"+rscompany+","+ "failed");
					}
				}
				if (count == 0) {
					logger.info("No Companies Passed from Targeting");

				}
				return result;
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("Database connection problem");
				throw new ServletException("DB Connection problem.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Connection problem");
		}
		return result;
	}
	
	public synchronized void budgetCheck(HttpServletRequest request, ServletResponse response){
	Thread budgetCheckTh = new Thread(new Runnable() {
	public void run() {
	String query="select * from companystock where budget>0";
	Connection con =(Connection) request.getSession().getServletContext().getAttribute("DBConnection");
	System.out.println("Company after Budget Check");
	String country = request.getParameter("country");
	String category = request.getParameter("category");
	PreparedStatement ps = null;
	ResultSet rs= null;
	try {	
		ps = con.prepareStatement(query);
			rs = ps.executeQuery();
		int count = 0;
		while(rs.next()){
			count++;
			String rscompany = rs.getString("companyid");
			String rscountry = rs.getString("countries");
			String rscategory = rs.getString("category");
			int matchCountry = matcher(country, rscountry);
			int matchCategory = matcher(category, rscategory);
			if(matchCountry == 1&& matchCategory ==1){
				System.out.println("BudgetCheck:"+rscompany+","+"passed");
				logger.info("BudgetCheck:"+rscompany+","+"passed");
			}
			else{
				System.out.println("BudgetCheck:"+rscompany+","+"Failed");
				logger.info("BudgetCheck:"+rscompany+","+"failed");
			}
		}
		if(count==0){
			logger.error("No Companies Passed from Budget");
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	}
	});
	budgetCheckTh.start();
}
	public String baseBidCheck(HttpServletRequest request, ServletResponse response){
		int basebid = Integer.parseInt(request.getParameter("basebid"));
		String query ="select * from companystock order by bid desc ";
		Connection con =(Connection) request.getSession().getServletContext().getAttribute("DBConnection");
		System.out.println("Company after Basebid Check");
		String country = request.getParameter("country");
		String category = request.getParameter("category");
		PreparedStatement ps = null;
		ResultSet rs= null;
		String resultcompany = "";
		try {	
			ps = con.prepareStatement(query);
				rs = ps.executeQuery();
			int count = 0;
			
			while(rs.next()){
				count++;
				//String result = rs.getString("companyid");
				//System.out.println(result);
				int rsbid = rs.getInt("bid");
				
				String rscompany = rs.getString("companyid");
				String rscountry = rs.getString("countries");
				String rscategory = rs.getString("category");
				if(basebid <= rsbid){
				
				int matchCountry = matcher(country, rscountry);
				int matchCategory = matcher(category, rscategory);
				if(matchCountry == 1&& matchCategory ==1){
					if (resultcompany == ""){
						resultcompany = rscompany;
					}
					System.out.println(rscompany+","+"passed");
					logger.info("BaseBid:"+rscompany+","+"passed");
				}
				else{
					System.out.println(rscompany+","+"Failed");
					logger.info("BaseBid:"+rscompany+","+"failed");
				}
				}else{
					System.out.println(rscompany+","+"Failed");
					logger.info("BaseBid:"+rscompany+","+"failed");
				}
			} 
			PrintWriter out = response.getWriter();
			out.println(resultcompany);
			System.out.println(resultcompany);
			logger.info("Winner ="+resultcompany);
			if(count==0){
				logger.error("“No Companies Passed from BaseBid check");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultcompany;

	}
	public synchronized void reducedBudget(HttpServletRequest request, ServletResponse response, String companyid) throws SQLException{
		Thread reducedBudgetTh = new Thread(new Runnable() {
			public void run() {
		float basebid = (Float.parseFloat(request.getParameter("basebid"))/100);
		PreparedStatement ps = null;
		Connection con =(Connection) request.getSession().getServletContext().getAttribute("DBConnection");
		String query = "update companystock set budget = budget-"+basebid+" where companyid ='"+companyid+"' limit 1";
		try {
			ps = con.prepareStatement(query);
			ps.executeUpdate();
			} catch (SQLException e) {
			e.printStackTrace();
		}
		
			}
		});
		reducedBudgetTh.start();
	}

	
	public int matcher(String words, String searching){
		String[] matchme=words.split(",");
		int matches= 0;
		for(String w: matchme){
		String tw = w.trim();
		Pattern p = Pattern.compile(".*"+tw+".*");
		Matcher m = p.matcher(searching);
		boolean b = m.matches();
		if(b == true){
			matches =1;
			break;
		}
		}
		return matches;
	}
	
}