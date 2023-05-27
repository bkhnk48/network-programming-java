import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import server.RMIClientInterface;

public class ClientNotificationImpl extends UnicastRemoteObject implements RMIClientInterface {

	private static final long serialVersionUID = -2541852276741164148L;

	public ClientNotificationImpl() throws RemoteException {
		// TODO Auto-generated constructor stub
	}

		@Override
	public void notifyOnOff(String username, String status) throws RemoteException {
		System.out.println("\t " + username + " is " + status + ".");
	}

}
