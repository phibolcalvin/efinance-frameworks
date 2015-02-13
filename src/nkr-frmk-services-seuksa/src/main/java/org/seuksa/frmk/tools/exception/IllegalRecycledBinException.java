package org.seuksa.frmk.tools.exception;


/**
 * 
 * @author prasnar
 * @version $version$
 */
public class IllegalRecycledBinException extends RuntimeException implements ErrorHandlerExceptionAware {
    /** */
	private static final long serialVersionUID = 4165050495058411669L;

	private ErrorHandler errorHandler;
	
	/**
	 * 
	 * @param errorHandler
	 */
	public IllegalRecycledBinException(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
	
	/**
     * 
     * @param e
     */
    public IllegalRecycledBinException(Exception e) {
        super(e);
    }

    /**
     * 
     * @param message
     */
    public IllegalRecycledBinException(String message) {
        super(message);
    }

    /**
     * 
     * @param message
     * @param innerException
     */
    public IllegalRecycledBinException(String message, Exception innerException) {
        super(message, innerException);
    }

	@Override
	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}
}
