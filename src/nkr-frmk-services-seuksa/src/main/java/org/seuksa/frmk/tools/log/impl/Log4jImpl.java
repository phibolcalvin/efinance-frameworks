package org.seuksa.frmk.tools.log.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;
import org.seuksa.frmk.tools.log.Log;
/**
 * Default implementation of Log 
 * @author  prasnar
 * @version 1.0
 */
public class Log4jImpl extends Log implements Serializable {
    
	private static final long serialVersionUID = -40974770158842788L;

	/** Log4j category */
    private Logger logger = null;
    
    /** */
    private static String initFilename = null;
    
    /**
     * 
     * @param file
     */
    public static void initFile(String propertiesFile) {
        if (initFilename == null) {
            if (propertiesFile != null) {
                InputStream is = ClassLoader.getSystemResourceAsStream(propertiesFile);
                if (is == null) {
                    is = Log.class.getClassLoader().getResourceAsStream(propertiesFile);
                    if (is == null) {
                        String errMsg = "Can't find the file [" + propertiesFile + "]. Be sure it exists." + "\n WARNING !!! log4j.properties will then be taken by default.";
                        System.err.println("*****Log4Impl.initFile()*******-" + errMsg);
                        return;
                    }
                }

                Properties prop = new Properties();

                try {
                    prop.load(is);
                } catch (IOException e) {
                    String errMsg = "Error while loading the properties file [" + propertiesFile + "]" + "\n WARNING !!! log4j.properties will then be taken by default.";
                    Log.getInstance(Log4jImpl.class).error(errMsg);
                    return;
                }
                try {
                    is.close();
                } catch (IOException e) {
                    ; // Do nothing
                }

                PropertyConfigurator.configure(prop);

                initFilename = propertiesFile;
            }
        }
    }

    public Log4jImpl() {
        super();
    }

    /**
     * Creates a new Log4jInstance
     * @param name name
     */
    public Log4jImpl(String name) {
        super(name);
        logger = Logger.getLogger(name);
    }

    /**
     * 
     * @return
     */
    @Override
    public boolean isDebugEnabled() {
    	return logger.isDebugEnabled();
    }
    
    /**
     * 
     * @return
     */
    @Override
    public boolean isInfoEnabled() {
    	return logger.isInfoEnabled();
    }
    
    /**
     * 
     * @param level
     * @return
     */
    @Override
    public boolean isInfoEnabled(Priority level) {
    	return logger.isEnabledFor(level);
    }
    
    /**
     * 
     * @see org.seuksa.frmk.tools.log.mycompany.frmk.util.log.Log#debug(java.lang.String)
     */
    public void debug(String message) {

        logger.debug(getIndent() + message);
    }

    /**
     * 
     * @see org.seuksa.frmk.tools.log.mycompany.frmk.util.log.Log#debug(java.lang.String, java.lang.Throwable)
     */
    public void debug(String message, Throwable t) {
        logger.debug(getIndent() + message, t);
    }

    /**
     * 
     * @see org.seuksa.frmk.tools.log.mycompany.frmk.util.log.Log#info(java.lang.String)
     */
    public void info(String message) {
        logger.info(getIndent() + message);
    }

    /**
     * 
     * @see org.seuksa.frmk.tools.log.mycompany.frmk.util.log.Log#warn(java.lang.String)
     */
    public void warn(String message) {
        logger.warn(message);
    }

    /**
     * 
     * @see org.seuksa.frmk.tools.log.mycompany.frmk.util.log.Log#warn(java.lang.String, java.lang.Throwable)
     */
    public void warn(String message, Throwable t) {
        logger.warn(message, t);
    }

    /**
     * 
     * @see org.seuksa.frmk.tools.log.mycompany.frmk.util.log.Log#error(java.lang.String, java.lang.Throwable)
     */
    public void error(String message, Throwable t) {
        logger.error(message, t);
    }

    /**
     * 
     * @see org.seuksa.frmk.tools.log.mycompany.frmk.util.log.Log#error(java.lang.String)
     */
    public void error(String message) {
        logger.error(message);
    }

    /**
     * 
     * @see org.seuksa.frmk.tools.log.mycompany.frmk.util.log.Log#enter(java.lang.String)
     */
    public void enter(String method) {
        if (logger.isDebugEnabled()) {
            this.debug(methodOnEnterPattern() + super.name + ':' + method);
        }
    }
    
    /**
     * 
     * @see org.seuksa.frmk.tools.log.mycompany.frmk.util.log.Log#exit(java.lang.String)
     */
    public void exit(String method) {
        if (logger.isDebugEnabled()) {
            this.debug(methodOnExitPattern() + super.name + ':' + method);
        }
    }
    
    /**
     * 
     * @return
     */
    public static String methodOnEnterPattern() {
        return "";
    }

    /**
     * 
     * @return
     */
    public static String methodOnExitPattern() {
        return "";
    }

    /**
     * 
     * @return
     */
    public static String getIndent() {
        return "";
    }


	@Override
	public void error(Throwable t) {
		logger.error(t);
		
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isErrorEnabled() {
		return logger.isEnabledFor(Priority.ERROR);
	}

}
