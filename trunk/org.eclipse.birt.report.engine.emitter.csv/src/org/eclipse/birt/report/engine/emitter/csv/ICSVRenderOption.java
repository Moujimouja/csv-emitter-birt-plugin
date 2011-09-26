package org.eclipse.birt.report.engine.emitter.csv;

import org.eclipse.birt.report.engine.api.IRenderOption;

public interface ICSVRenderOption extends IRenderOption{
	
	public static final String OUTPUT_EMITTERID_CSV = "org.eclipse.birt.report.engine.emitter.csv";	
	
	public static final String OUTPUT_FORMAT_CSV = "csv";
	
	/**
	 * The option to decide to show data type in second row of output CSV
	 */
	public static final String SHOW_DATATYPE_IN_SECOND_ROW = "csvRenderOption.showDatatypeInSecondRow";
	
	/**
	 * The option to export a specific table by name in the CSV Output
	 */
	public static final String EXPORT_TABLE_BY_NAME = "csvRenderOption.exportTableByName";
	
	public void setShowDatatypeInSecondRow(boolean showDatatypeInSecondRow);
	
	public boolean getShowDatatypeInSecondRow();
	
	public void setExportTableByName(String tableName);
	
	public String getExportTableByName();
}
