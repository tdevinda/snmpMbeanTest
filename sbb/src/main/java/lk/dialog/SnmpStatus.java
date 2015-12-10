package lk.dialog;

import java.io.Serializable;

import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.slee.connection.SleeConnection;
import javax.slee.connection.SleeConnectionFactory;

public class SnmpStatus implements SnmpStatusMBean, Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String status = "nothing";
	private boolean started;
	private String jndiName;

	@Override
	public String getSbbStatus() {
		System.out.println("get called");
		return status;
	}

	@Override
	public void setSbbStatus(String status) {
		this.status = status;
		System.out.println("set called");
	}
	
	
	public void start() throws Exception
    {
		System.out.println("start called");
        started = true;
        rebind();
    }
                
    public void stop()
    {
        started = false;
        unbind(jndiName);
    }
                
    private void rebind() throws NamingException
    {
        InitialContext rootCtx = new InitialContext();
        Name fullName = rootCtx.getNameParser("").parse(jndiName);
        System.out.println("fullName="+fullName);
        
//        NonSerializableFactory.rebind(fullName, contextMap, true);
    }

    private void unbind(String jndiName)
    {
        try {
            InitialContext rootCtx = new InitialContext();
            rootCtx.unbind(jndiName);
//            NonSerializableFactory.unbind(jndiName);
        } catch(NamingException e) {
            e.printStackTrace();
        }
    }
	
	

}
