package com.Adcash.pack;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Screenshot {
	public void databaseScreenshot(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String query ="select * from companystock order by bid desc ";
		Connection con =(Connection) request.getSession().getServletContext().getAttribute("DBConnection");
		PrintWriter out = response.getWriter();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(query);
			rs =  ps.executeQuery();
			int count =0;
			out.println("<table><tr><td>"+"CompanyID"+"</td><td>"+"Countries"+"</td><td>"+"Budget"+"</td>"
					+ "<td>"+"Bid"+"</td><td>"+"Category"+"</td></tr>");
			
			while(rs.next()){
				count++;
				
					out.println("<tr><td>"+rs.getString("CompanyID")+"</td><td>"+rs.getString("Countries")+"</td><td>"+rs.getFloat("Budget")+"</td><td>"+rs.getFloat("Bid")+"</td><td>"+rs.getString("Category")+"</td></tr>");
			}
			if(count ==0){
				out.println("Databse Empty");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	public void logScreenshot() throws IOException{
		File file = new File("D:\\AdCash\\Stocklog.log");
		//first check if Desktop is supported by Platform or not
        if(!Desktop.isDesktopSupported()){
            System.out.println("Desktop is not supported");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        if(file.exists()) desktop.open(file);
        
	}
}

	