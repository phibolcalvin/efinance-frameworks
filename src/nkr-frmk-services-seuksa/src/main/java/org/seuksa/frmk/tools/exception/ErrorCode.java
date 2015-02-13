package org.seuksa.frmk.tools.exception;

/**
 * @author prasnar
 * @version $Revision$
 */
public class ErrorCode {
    public static final ErrorCode ERR_NONE = new ErrorCode(-1);
    public static final ErrorCode ERR_DEFAULT = new ErrorCode(0);
    public static final ErrorCode ERR_SYS = new ErrorCode(1);
    public static final ErrorCode ERR_SYS_TIMEOUT = new ErrorCode(2);
    public static final ErrorCode ERR_INCORRECT_PARAMETERS = new ErrorCode(3);

    private int code = 0;


    /**
     * 
     * @param code
     */
    private ErrorCode(int code) {
    	this.code = code;
    }
    
    /**
     * 
     * @return
     */
    public int value() {
    	return code;
    }
    
    @Override
	public boolean equals(Object o) {
		if (!(o instanceof ErrorCode)) {
			return false;
		}
		return code == ((ErrorCode) o).value();
    }
}
