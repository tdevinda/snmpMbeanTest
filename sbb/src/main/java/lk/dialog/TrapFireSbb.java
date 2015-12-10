package lk.dialog;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.JMX;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.AlarmLevel;
import javax.slee.management.SbbNotification;
import javax.slee.serviceactivity.ServiceStartedEvent;

import org.mobicents.slee.SbbContextExt;
import org.mobicents.slee.container.SleeContainer;

import net.java.slee.resource.http.events.HttpServletRequestEvent;

public abstract class TrapFireSbb implements Sbb, TrapFire {

	AlarmFacility alarm;
	private SbbContextExt sbbContext;

	public void setSbbContext(SbbContext sbbContext) { 
		this.sbbContext = (SbbContextExt) sbbContext;

		try {
			InitialContext initialContext = new InitialContext();
			
			Context context = (Context) initialContext.lookup("java:comp/env");
			
			alarm = (AlarmFacility) initialContext.lookup(AlarmFacility.JNDI_NAME);
			
			

			
		} catch(NamingException e) {
			e.printStackTrace();
		}


	}

	public void onServiceStartedEvent(ServiceStartedEvent event, ActivityContextInterface aci/*, EventContext eventContext*/) {

		System.out.println("service started");
		
		try {
			ObjectName obj = new ObjectName("slee:snmpmonitor=SnmpStatus");
			SnmpStatus status = new SnmpStatus();
			status.setSbbStatus("initial");
			
			
			SleeContainer cont = SleeContainer.lookupFromJndi();
			MBeanServer mbs = cont.getMBeanServer();
			
			if(mbs.isRegistered(obj)) {
				mbs.unregisterMBean(obj);
			}
			mbs.registerMBean(status, obj);
			
			
			//InitialContext initialContext = new InitialContext();
			//initialContext.rebind("lk.dialog:type=SnmpStatus", status);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}


	public void onGET(HttpServletRequestEvent event, ActivityContextInterface aci/*, EventContext eventContext*/) {
		
		//String alarmid = alarm.raiseAlarm(SbbNotification.ALARM_NOTIFICATION_TYPE, "TestAlarm", AlarmLevel.CRITICAL, "Test Alarm level critical");
		//System.out.println("Alarm Id is "+ alarmid);
		
		/*String port = System.getProperty("com.sun.management.jmxremote.port");
		try {
			System.out.println("calling "+ "service:jmx:rmi:///jndi/rmi://:"+ port +"/jmxrmi");
			JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:"+ 1099 +"/jmxrmi");
			JMXConnector jmxcon = JMXConnectorFactory.connect(url, null);
			MBeanServerConnection mBeanCon = jmxcon.getMBeanServerConnection();
			ObjectName obj = new ObjectName("slee:snmpmonitor=SnmpStatus");
			SnmpStatusMBean bean = JMX.newMBeanProxy(mBeanCon, obj, SnmpStatusMBean.class, true);
			
			bean.setSbbStatus(bean.getSbbStatus() + "read");
			
			System.out.println("status is "+ bean.getSbbStatus());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		}*/
		
		SleeContainer container  = SleeContainer.lookupFromJndi();
		MBeanServer server = container.getMBeanServer();
		
		
		try {
			ObjectName obj = new ObjectName("slee:snmpmonitor=SnmpStatus");
			String sbbStatusString = (String) server.getAttribute(obj, "SbbStatus");
			System.out.println("Sbb status is "+ sbbStatusString);
		} catch (AttributeNotFoundException e) {
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		} catch (MBeanException e) {
			e.printStackTrace();
		} catch (ReflectionException e) {
			e.printStackTrace();
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		}
	}



	public void unsetSbbContext() { this.sbbContext = null; }

	public void sbbCreate() throws javax.slee.CreateException {}
	public void sbbPostCreate() throws javax.slee.CreateException {}
	public void sbbActivate() {}
	public void sbbPassivate() {}
	public void sbbRemove() {}
	public void sbbLoad() {}
	public void sbbStore() {}
	public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface activity) {}
	public void sbbRolledBack(RolledBackContext context) {}



	/**
	 * Convenience method to retrieve the SbbContext object stored in setSbbContext.
	 * 
	 * TODO: If your SBB doesn't require the SbbContext object you may remove this 
	 * method, the sbbContext variable and the variable assignment in setSbbContext().
	 *
	 * @return this SBB's SbbContext object
	 */

	protected SbbContextExt getSbbContext() {
		return sbbContext;
	}




}
