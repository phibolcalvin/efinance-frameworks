package org.seuksa.frmk.tools.exception;

/**
 * For technical errors catching purpose 
 * @author prasnar
 */
public class TechnicalException extends T9nException {
	/** */
    private static final long serialVersionUID = -897030700509427958L;

    /**
     * 
     * @param errorKey
     * @param params
     */
    public TechnicalException(String errorKey, Object[] params) {
		super(errorKey, params);
	}

   
}
