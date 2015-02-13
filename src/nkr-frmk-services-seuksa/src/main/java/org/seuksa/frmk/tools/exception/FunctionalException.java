package org.seuksa.frmk.tools.exception;

/**
 * For functional errors catching purpose 
 * @author Prasnar
 * @version 1.0
 */
public class FunctionalException extends T9nException {
	
	/**  */
    private static final long serialVersionUID = 4768329319450337779L;

   
    /**
     * 
     * @param errorKey
     * @param params
     */
    public FunctionalException(String errorKey, Object[] params) {
		super(errorKey, params);
	}

}
