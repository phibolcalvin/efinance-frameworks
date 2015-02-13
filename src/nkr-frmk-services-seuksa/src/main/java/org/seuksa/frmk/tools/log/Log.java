package org.seuksa.frmk.tools.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Priority;
import org.seuksa.frmk.tools.log.impl.Log4jImpl;

/**
 * 
 * @author prasnar
 * @version 1.0
 */
public abstract class Log {
    /** */
    private static Map<String, Log> instances = new TreeMap<String, Log>();
    /** */
    protected String name = null;
    private static boolean isInit = false;

    /**
     * 
     * @param file
     */
    public static void initFile(String file) {
    	if (!isInit) {
    		Log4jImpl.initFile(file);
    		isInit = true;
    	}
	}
    
    /**
     * Constructor
     */
    protected Log() {
        super();
    }
    
    /**
     * Constructor
     */
    protected Log(String name) {
        this.name = name;
    }

    /**
     * 
     * @param o
     * @return
     */
    public static Log getInstance(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("No specified Object");
          }
          return getInstance(o.getClass());
    }
    

    /**
     * 
     * @param c
     * @return
     */
    public static Log getLog(Class<?> c) {
    	return getInstance(c);
    }
    
    /**
     * 
     * @param c
     * @return
     */
    public static Log getInstance(Class<?> c) {
        if (c == null) {
            throw new IllegalArgumentException("No specified Class");
        }
        return getInstance(c.getName());
    }

    /**
     * 
     * @param name
     * @return
     */
	public static Log getInstance(String name) {
		Log instance = (Log) instances.get(name);
		if (instance == null) {
			synchronized (Log.class) {
				if (instance == null) {
					instance = createLogImpl(name);
					instances.put(name, instance);
				}
			}
		}
		return instance;
	}

    /**
     * 
     * @param name
     * @return
     */
    private static Log createLogImpl(String name) {
        return new Log4jImpl(name);
    }

    /**
     * Log a message, debug level.
     * @param message the message to log
     */
    public abstract void debug(String message);

 
    /**
     * Log a message associated to a throwable object, debug level.
     * @param message the message to log
     * @param t the throwable to log
     */
    public abstract void debug(String message, Throwable t);

    /**
     * Log a method entering, level depend on implementation.
     * @param method the method name
     */
    public abstract void enter(String method);

    /**
     * Log a method exiting, level depend on implementation.
     * @param method the method name
     */
    public abstract void exit(String method);

    /**
     * Log a message, information level.
     * @param message the message to log
     */
    public abstract void info(String message);

    /**
     * Log a message, warning level.
     * @param message the message to log
     */
    public abstract void warn(String message);

    /**
     * Log a message associated to a throwable object, warning level.
     * @param message the message to log
     * @param t the throwable to log
     */
    public abstract void warn(String message, Throwable t);

    /**
     * Log a message associated to a throwable object, error level.
     * @param message the message to log
     * @param t the throwable to log
     */
    public abstract void error(String message, Throwable t);

    /**
     * Log a message, error level.
     * @param message the message to log
     */
    public abstract void error(String message);

    public abstract void error(Throwable t);

    /**
     * 
     * @param message
     * @param t
     */
    public void errorStackTrace(String message, Throwable t) {
    	error(message);
    	error(getStackTrace(t), t);
    }

    /**
     * 
     * @param t
     */
    public void errorStackTrace(Throwable t) {
    	error(getStackTrace(t), t);
    }
    /**
     * 
     * @param t
     * @return
     */
    public String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
        try {
            t.printStackTrace(new PrintWriter(sw, true));
            String stacktrace = sw.toString();

            return stacktrace;
        } finally {
            try {
                if (sw != null)
                    sw.close();
            } catch (Exception ignore) {
            }
        }
	}

	public abstract boolean isErrorEnabled();

	public abstract boolean isDebugEnabled();

	public abstract boolean isInfoEnabled();

	public abstract boolean isInfoEnabled(Priority level);
  
}
