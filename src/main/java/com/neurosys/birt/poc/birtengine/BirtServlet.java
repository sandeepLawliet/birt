/**
 * 
 */
package com.neurosys.birt.poc.birtengine;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;

/*
 * Copyright Â©2017 Neurosystems Technologies Pvt. Ltd. All Rights reserved. This material 
 * contains confidential and proprietary information of Neurosystems Technologies. 
 * Any disclosure, reproduction, dissemination or distribution of the material 
 * contained herein is strictly prohibited.
*/
/**
 * @author Sandeep Kumar Singh
 *
 */
@WebServlet("/GetReportServlet")
public class BirtServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor of the object.
	 */
	private IReportEngine birtReportEngine = null;
	protected static Logger logger = Logger.getLogger( "org.eclipse.birt" );

	public BirtServlet() {
	 super();
	}

	/**
	 * Destruction of the servlet. 

	 */
	public void destroy() {
	 super.destroy(); 
	 BirtFactory.destroyBirtEngine();
	}


	/**
	 * The doGet method of the servlet. 

	 *
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	  throws ServletException, IOException {

	 //get report name and launch the engine
	 resp.setContentType("text/html");
	 //resp.setContentType( "application/pdf" ); 
	 //resp.setHeader ("Content-Disposition","inline; filename=test.pdf");  
	
	 String reportName = req.getParameter("ReportName");
	 ServletContext sc = req.getSession().getServletContext();
	 this.birtReportEngine = BirtFactory.getBirtEngine(sc);
	 
	 //setup image directory
	 HTMLRenderContext renderContext = new HTMLRenderContext();
	 renderContext.setBaseImageURL(req.getContextPath()+"/images");
	 renderContext.setImageDirectory(sc.getRealPath("/images"));
	 
	 logger.log( Level.FINE, "image directory " + sc.getRealPath("/images"));  
	 System.out.println("stdout image directory " + sc.getRealPath("/images"));
	 
	 HashMap<String, HTMLRenderContext> contextMap = new HashMap<String, HTMLRenderContext>();
	 contextMap.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext );
	 
	 IReportRunnable design;
	 try
	 {
	  //Open report design
	  design = birtReportEngine.openReportDesign( sc.getRealPath("/Reports")+"/"+reportName );
	  //create task to run and render report
	  IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask( design );  
	  task.setAppContext( contextMap );
	  
	  //set output options
	  HTMLRenderOption options = new HTMLRenderOption();
	  options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
	  options.setEmbeddable(true);
	  options.setImageHandler(new HTMLServerImageHandler());
	  //options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
	  options.setOutputStream(resp.getOutputStream());
	  task.setRenderOption(options);
	  
	  //run report
	  /* converting html output to string */
	  String output;
	  task.run();
	  output = resp.getOutputStream().toString();
	
	 
	  task.close();
	  
	  
	  
	 }catch (Exception e){
	  
	  e.printStackTrace();
	  throw new ServletException( e );
	 }
	}

	/**
	 * The doPost method of the servlet. 

	 *
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException {

	 response.setContentType("text/html");
	 PrintWriter out = response.getWriter();
	 out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
	 out.println("<HTML>");
	 out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
	 out.println("  <BODY>");
	 out.println(" Post does nothing");
	 out.println("  </BODY>");
	 out.println("</HTML>");
	 out.flush();
	 out.close();
	}

	/**
	 * Initialization of the servlet. 

	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
	 BirtFactory.initBirtConfig();
	 
	}

}
