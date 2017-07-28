package com.neurosys.birt.poc.birtengine;

import org.eclipse.birt.report.engine.api.*;
 
import java.util.logging.Level;
 
/**
 * Created by IntelliJ IDEA.
 * User: cdutz
 * Date: 07.12.11
 * Time: 16:22
 */
public class BirtEmbeder {
 
    private IReportEngine engine;
 
    public BirtEmbeder() {
        final EngineConfig config = new EngineConfig();
        engine =  new ReportEngine(config);
        engine.changeLogLevel( Level.WARNING );
    }
 
    public void render(String type) {
        try{
            //Open the report design
            final IReportRunnable design = engine.openReportDesign("/home/ms/sandbox/birtengine/sampleChart.rptdesign");
 
            //Create task to run and render the report,
            final IRunAndRenderTask task = engine.createRunAndRenderTask(design);
            //Set parent classloader for engine
            task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, BirtEmbeder.class.getClassLoader());
 
            final IRenderOption options = new RenderOption();
            options.setOutputFormat(type);
            options.setOutputFileName("output/TestReport." + options.getOutputFormat());
            if( options.getOutputFormat().equalsIgnoreCase("html")){
                final HTMLRenderOption htmlOptions = new HTMLRenderOption( options);
                htmlOptions.setImageDirectory("img");
                htmlOptions.setHtmlPagination(true);
                htmlOptions.setHtmlRtLFlag(false);
                htmlOptions.setEmbeddable(true);
                htmlOptions.setSupportedImageFormats("PNG");
 
                //set this if you want your image source url to be altered
                //If using the setBaseImageURL, make sure to set image handler to HTMLServerImageHandler
                //htmlOptions.setBaseImageURL("http://myhost/prependme?image=");
            }else if( options.getOutputFormat().equalsIgnoreCase("pdf")){
                final PDFRenderOption pdfOptions = new PDFRenderOption( options );
                pdfOptions.getIntOption(IPDFRenderOption.PAGE_OVERFLOW, IPDFRenderOption.FIT_TO_PAGE_SIZE);
                pdfOptions.getIntOption(IPDFRenderOption.PAGE_OVERFLOW, IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES);
            }
 
            task.setRenderOption(options);
 
            //run and render report
            task.run();
 
            task.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
 
    public static void main(String[] args) {
        final BirtEmbeder embeder = new BirtEmbeder();
        embeder.render("html");
        embeder.render("pdf");
    }
 
}