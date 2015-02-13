package com.nokor.frmk.vaadin.util.filebuilder;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.seuksa.frmk.tools.log.Log;

import com.vaadin.data.Container;
import com.vaadin.data.Property;

/**
 * @author ky.nora
 */
public abstract class FileBuilder implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3202506378364666853L;
	protected Log logger = Log.getInstance(getClass());

	protected File file;
    public Container container;
    private Object[] visibleColumns;
    private Map<Object, String> columnHeaderMap;
    private String header;
    private Locale locale = Locale.getDefault();;
    private String dateFormatString = "dd/MM/yyyy hh:mm";

    public FileBuilder() {

    }

    public FileBuilder(Container container) {
        setContainer(container);
    }

    public void setContainer(Container container) {
        this.container = container;
        columnHeaderMap = new HashMap<Object, String>();
        for (Object propertyId : container.getContainerPropertyIds()) {
            columnHeaderMap
                    .put(propertyId, propertyId.toString().toUpperCase());
        }
        if (visibleColumns == null) {
            visibleColumns = container.getContainerPropertyIds().toArray();
        }
    }

    public void setVisibleColumns(Object[] visibleColumns) {
        this.visibleColumns = visibleColumns;
    }

    /**
     * 
     * @return
     */
    public File getFile() {
        try {
            initTempFile();
        } catch (Exception e) {
        	logger.errorStackTrace("Error in initTempFile the file", e);
        }
        try {
            resetContent();
        } catch (Exception e) {
        	logger.errorStackTrace("Error in resetContent the file", e);
        }
        try {
            buildFileContent();
        } catch (Exception e) {
        	logger.errorStackTrace("Error in buildFileContent the file", e);
        }
        try {
            writeToFile();
        } catch (Exception e) {
        	logger.errorStackTrace("Error in writeToFile the file", e);
        }
        
        return file;
    }

    private void initTempFile() throws IOException {
        if (file != null) {
            file.delete();
        }
        file = createTempFile();
    }

    protected void buildFileContent() {
        buildHeader();
        buildColumnHeaders();
        buildRows();
        buildFooter();
    }

    protected void resetContent() {

    }

    protected void buildColumnHeaders() {
        if (visibleColumns.length == 0) {
            return;
        }
        onHeader();
        for (Object propertyId : visibleColumns) {
            String header = columnHeaderMap.get(propertyId);
            onNewCell();
            buildColumnHeaderCell(header);
        }
    }

    protected void onHeader() {
        onNewRow();
    }

    protected void buildColumnHeaderCell(String header) {

    }

    protected void buildHeader() {

    }

    private void buildRows() {
        if (container == null || container.getItemIds().isEmpty()) {
            return;
        }
        for (Object itemId : container.getItemIds()) {
            onNewRow();
            buildRow(itemId);
        }
    }

    private void buildRow(Object itemId) {
        if (visibleColumns.length == 0) {
            return;
        }
        for (Object propertyId : visibleColumns) {
            Property<?> property = container.getContainerProperty(itemId,
                    propertyId);
            onNewCell();
            buildCell(property == null ? null : property.getValue());
        }
    }

    protected void onNewRow() {

    }

    protected void onNewCell() {

    }

    protected abstract void buildCell(Object value);

    protected void buildFooter() {

    }

    protected abstract String getFileExtension();

    protected String getFileName() {
        return "tmp";
    }

    /**
     * 
     * @return
     * @throws IOException
     */
    protected File createTempFile() throws IOException {
        return File.createTempFile(getFileName(), getFileExtension());
    }

    protected abstract void writeToFile();

    public void setColumnHeader(Object propertyId, String header) {
        columnHeaderMap.put(propertyId, header);
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    protected int getNumberofColumns() {
        return visibleColumns.length;
    }

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public void setDateFormat(String dateFormat) {
		this.dateFormatString = dateFormat;
	}
	
	protected String getDateFormatString() {
		return dateFormatString;
	}
	
	protected String formatDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString, locale);
		return dateFormat.format(date);
	}
}
