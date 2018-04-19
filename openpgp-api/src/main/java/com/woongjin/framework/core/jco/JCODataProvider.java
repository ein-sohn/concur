/**
 * JCO DestinationDataProvider
 */
package com.woongjin.framework.core.jco;

import java.util.HashMap;
import java.util.Properties;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class JCODataProvider implements DestinationDataProvider {
	private DestinationDataEventListener eL;
	private HashMap<String, Properties> secureDBStorage = new HashMap<String, Properties>();

	public Properties getDestinationProperties(String destinationName) {
		Properties p = null;
		try {
			p = secureDBStorage.get(destinationName);
			if(p!=null) {
				if(p.isEmpty())
					//throw new Exception("유효하지 않은 환경 JCO 설정 ");
				return p;
			}
			return null;
		} catch(RuntimeException e) {
			//throw new Exception("JCO Runtime DataProviderException ", re);
		}
		return p;
	}

	public void setDestinationDataEventListener(DestinationDataEventListener eventListener) {
		this.eL = eventListener;
	}

	public boolean supportsEvents() {
		return true;
	}

	void changeProperties(String destName, Properties properties) {
		synchronized(secureDBStorage) {
			if(properties==null) {
				if(secureDBStorage.remove(destName)!=null)
					eL.deleted(destName);
			} else  {
				secureDBStorage.put(destName, properties);
				eL.updated(destName); // create or updated
			}
		}
	}

	//business logic
	void testCalls(String destName) throws Exception {
		JCoDestination dest;
		try {
			dest = JCoDestinationManager.getDestination(destName);
			System.out.println("User : " + dest.getUser());
			dest.ping();
			System.out.println("Destination " + destName + " works");

			dest.removeThroughput();

		} catch(JCoException e) {
			throw new Exception(e.getMessage());
		}
	}
}
