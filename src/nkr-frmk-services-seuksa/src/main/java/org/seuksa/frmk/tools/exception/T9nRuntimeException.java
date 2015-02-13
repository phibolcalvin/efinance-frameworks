package org.seuksa.frmk.tools.exception;


/**
 * RuntimeException is for internal application exception handling.
 *
 * @author prasnar
 */
public class T9nRuntimeException extends RuntimeException implements ErrorHandlerExceptionAware {
	/** */
	private static final long serialVersionUID = 5821963031048958366L;

	private ErrorHandler errorHandler;
	
	/**
	 * 
	 * @param errorHandler
	 */
	public T9nRuntimeException(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
	
	/**
	 * 
	 * @param params
	 */
	public T9nRuntimeException(String errorKey) {
		errorHandler = new ErrorHandler(errorKey);
    }
	
	/**
	 * 
	 * @param errorKey
	 * @param params
	 */
    public T9nRuntimeException(String errorKey, Object... params) {
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