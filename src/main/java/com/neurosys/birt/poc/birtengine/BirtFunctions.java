
package com.neurosys.birt.poc.birtengine;



import java.io.ByteArrayOutputStream;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.core.internal.registry.RegistryProviderFactory;

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
public class BirtFunctions {
	
	
	
	public String executeReport(String reportName) throws EngineException
	{
	IReportEngine engine=null;
	EngineConfig config = null;

	try{	
		// start up Platform
		config = new EngineConfig( );	
		config.setLogConfig("/home/ms/workspace/birtengine/logs", java.util.logging.Level.FINEST);
		Platform.startup( config );
		
		
		// create new Report Engine
		IReportEngineFactory factory = (IReportEngineFactory) Platform
		.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
		engine = factory.createReportEngine( config );		

		// open the report design
		IReportRunnable design = null;
		design = engine.openReportDesign("/home/ms/workspace/birtengine/"+reportName); 

		// create RunandRender Task
		IRunAndRenderTask task = engine.createRunAndRenderTask(design); 	
			
		// pass necessary parameters
		task.setParameterValue("myParam", "myParamValue");
		task.validateParameters();
				
		// set render options including output type
		HTMLRenderOption options = new HTMLRenderOption();
		ByteArrayOutputStream outs = new ByteArrayOutputStream();
		options.setOutputStream(outs);
		options.setImageHandler(new HTMLServerImageHandler());
		options.setBaseImageURL("images");
		options.setImageDirectory("/home/ms/workspace/birtengine/images");
		options.setEmbeddable(true);
		//options.setOutputFormat("html");
		 options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
		task.setRenderOption(options);

		// run task
		String output;
		
		task.run();
		output = outs.toString();
		task.close();
		engine.destroy();
		return output;
	}catch( Exception ex){
		ex.printStackTrace();
		return "Error";
	}		
	finally
	{
	       Platform.shutdown( );
	       RegistryProviderFactory.releaseDefault();
	}
	}
}
