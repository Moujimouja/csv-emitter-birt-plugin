package org.eclipse.birt.report.engine.emitter.csv;


import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;

public class CSVReportEmitter extends ContentEmitterAdapter
{	
	
	protected static Logger logger = Logger.getLogger( CSVReportEmitter.class.getName( ) );
	
	protected static final String OUTPUT_FORMAT_CSV = "csv";

	protected static final String REPORT_FILE = "report.csv";
	
	protected ContentEmitterVisitor contentVisitor;
	
	protected IEmitterServices services;
	
	protected CSVWriter writer; 
	
	protected IReportContent report;
	
	protected int totalColumns;
	
	protected int currentColumn;
	
	protected OutputStream out = null;	
	
	protected boolean isFirstPage = false;	
	
	protected boolean writeData = true;			
	
	public CSVReportEmitter( )
	{
		contentVisitor = new ContentEmitterVisitor( this );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#initialize(org.eclipse.birt.report.engine.emitter.IEmitterServices)
	 */
	public void initialize( IEmitterServices services ) throws EngineException
	{
		this.services = services;
		
		this.out = EmitterUtil.getOuputStream( services, REPORT_FILE );		
		
		writer = new CSVWriter( );	
	}
	
	public void start( IReportContent report )
	{
		logger.log( Level.FINE,"Starting CSVReportEmitter." );
		this.report = report;
		writer.open( out, "UTF-8" );
		writer.startWriter( );
	}
	
	public void end( IReportContent report )
	{
		logger.log( Level.FINE,"CSVReportEmitter end report." );
		writer.endWriter( );
		writer.close( );
		if( out != null )
		{
			try
			{
				out.close( );
			}
			catch ( IOException e )
			{
				logger.log( Level.WARNING, e.getMessage( ), e );
			}
		}
	}
	public void startPage( IPageContent page ) throws BirtException
	{
		logger.log( Level.FINE,"CSVReportEmitter startPage" );
		
		startContainer( page );
		
		if(page.getPageNumber()>1){
			isFirstPage = false;
		}else{
			isFirstPage = true;			
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#startLabel(org.eclipse.birt.report.engine.content.ILabelContent)
	 * To avoid framework to print label for every page.
	 */
	public void startLabel( ILabelContent label ) throws BirtException
	{
		if(isFirstPage)
			startText( label );
		
	}
	
	public void startTable( ITableContent table )
	{
		assert table != null;
		totalColumns = table.getColumnCount();		
	}
	

	public void startRow( IRowContent row )
	{
		assert row != null;
		if ( isRowInFooter( row ) || isRowInHeaderExceptFirstHeader(row) )
			writeData = false;
		
		currentColumn = 0;		
	}
	
	public void startText( ITextContent text )
	{				
		if ( isHidden(text.getStyle()) )
		{
			logger.log( Level.FINE,"Skipping Hidden text" );
			return;
		}
		logger.log( Level.FINE,"Start text" );
		String textValue = text.getText( );
		if ( writeData )
		{
			writer.text( textValue );
			currentColumn++;			
		}
	}
	
	
	public void endCell( ICellContent cell )
	{
		if ( isHidden(cell.getStyle()))
		{
			logger.log( Level.FINE,"Skipping Hidden cell" );
			return;
		}		
		
		if ( ( currentColumn < totalColumns )&& writeData )
		{
			writer.closeTag( CSVTags.TAG_COMMA );
		}
		
	}
	
	public void endRow( IRowContent row )
	{		
		if ( writeData )
			writer.closeTag( CSVTags.TAG_CR );
		
		writeData = true;
	}	
	
	private boolean isHidden(IStyle style)
	{		
		String format=style.getVisibleFormat();
		
		if ( format != null && ( format.indexOf( EngineIRConstants.FORMAT_TYPE_VIEWER ) >= 0 || format.indexOf( BIRTConstants.BIRT_ALL_VALUE ) >= 0 ) )
		{
			return true;
		}
		else
		{
			return false;
		}		
	}
	
	private boolean isRowInFooter( IRowContent row )
	{
		IElement parent = row.getParent( );
		if ( !( parent instanceof IBandContent ) )
		{
			return false;
		}
		IBandContent band = ( IBandContent )parent;
		if ( band.getBandType( ) == IBandContent.BAND_FOOTER )
		{
			return true;
		}
		return false;
	}
	
	private boolean isRowInHeaderExceptFirstHeader( IRowContent row )
	{
		if(isFirstPage)
			return false;
		
		IElement parent = row.getParent( );
		if ( !( parent instanceof IBandContent ) )
		{
			return false;
		}
		IBandContent band = ( IBandContent )parent;
		if ( band.getBandType( ) == IBandContent.BAND_HEADER )
		{
			return true;
		}
		return false;
	}
	
}
