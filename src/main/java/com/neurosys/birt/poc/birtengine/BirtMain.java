/**
 * 
 */
package com.neurosys.birt.poc.birtengine;

import org.eclipse.birt.report.engine.api.EngineException;

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
public class BirtMain {
	public static void main(String args[]) throws EngineException{
		
		BirtFunctions birtfunctions = new BirtFunctions();
		String htmlOutput = birtfunctions.executeReport("test.rptdesign");
		System.out.println(htmlOutput);
	}

}
