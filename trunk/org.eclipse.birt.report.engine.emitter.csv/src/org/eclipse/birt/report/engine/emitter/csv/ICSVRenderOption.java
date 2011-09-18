package org.eclipse.birt.report.engine.emitter.csv;

import org.eclipse.birt.report.engine.api.IRenderOption;

public interface ICSVRenderOption extends IRenderOption{

	public static final String OUTPUT_EMITTERID_CSV = "org.eclipse.birt.report.engine.emitter.csv";
	public static final String OUTPUT_FORMAT_CSV = "csv";
	public static final String OUTPUT_DATATYPE_IN_SECOND_ROW = "csvRenderOption.outputDatatypeInSecondRow";
	
}
