package org.eclipse.birt.report.engine.emitter.csv;

import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;

public class CSVRenderOption extends RenderOption implements ICSVRenderOption{
	
	public static final String CSV = "CSV";	
	
	public CSVRenderOption( ) 
	{
		super();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param options
	 */
	public CSVRenderOption( IRenderOption options )
	{
		super( options );
	}
}
