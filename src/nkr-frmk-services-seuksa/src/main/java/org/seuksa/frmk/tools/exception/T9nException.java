package org.seuksa.frmk.tools.exception;


/**
 * Exception is for internal application exception handling.
 *
 * @author prasnar
 */
public class T9nException extends Exception implements ErrorHandlerExceptionAware {
    /** */
	private static final long serialVersionUID = 4211177435065907284L;
	
	private ErrorHandler errorHandler;
	
	/**
	 * 
	 * @param errorHandler
	 */
	public T9nException(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
	
	/**
	 * 
	 * @param params
	 */
	public T9nException(String errorKey) {
		errorHandler = new ErrorHandler(errorKey);
    }
	
	/**
	 * 
	 * @param errorKey
	 * @param params
	 */
    public T9nException(String errorKey, Object... params) {
    	errorHandler = new ErrorHandler(errorKey, params);
    }

     @Override
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }
    
    /**
     * 
     * @return
     */
    public int getCode() {
    	return errorHandler.getCode().value();
    }
}