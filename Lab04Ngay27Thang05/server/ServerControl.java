package server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerControl {
	private ArrayList<User> listActiveAccounts;
	private ArrayList<User> availableAccounts;
	public AvailableUsersInterface availUsers; 
	
	private int serverPort;
	//private ServerSocket myServer;
	//private Socket clietnSocket;
	private ServerView view;
	//private ArrayList<RMIClientInterface> listRMIClients;
	private HashMap<String, RMIClientInterface> listRMIClients;
	
	public ServerControl(ServerView view) {
		this.view = view;
		listActiveAccounts = new ArrayList<User>();
		availableAccounts = new ArrayList<User>();
		listRMIClients = new HashMap<String, RMIClientInterface>();
		/*listActiveAccounts.add(new User("0987654321", "111111"));
		listActiveAccounts.add(new User("0988888888", "111111"));
		listActiveAccounts.add(new User("0977777777", "111111"));
		listActiveAccounts.add(new User("0987575701", "111111"));*/
		listActiveAccounts = ServerDBControl.getAllUsers();
		try {
			availUsers = new AvailableUserImpl(availableAccounts);
			availUsers.updateServerControl(this);
			Registry registry = LocateRegistry.createRegistry(789);
			registry.bind("availUsers", availUsers);
			//System.out.println("Dang ky thanh cong availUsers");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		//openServer(6868);
	}
	
	private int checkAlreadyLogin(User user) {
		int index = -1;
		for(User u : this.availableAccounts) {
			index++;
			if(u.equals(user)) {
				return index;
			}
		}
		return -1;
	}
	
	private void addAvailableUser(User user) {
		int index = this.checkAlreadyLogin(user);
		if(index == -1) {
			this.availableAccounts.add(user);
			
			//Cap nhat status trong listActiveAccounts
			for (int i = 0; i < listActiveAccounts.size(); i++) {
	            if (listActiveAccounts.get(i).equals(user)) {
	            	user.setStatus("on");
	            	listActiveAccounts.set(i, user);
	            	break;
	            }
	        }
			
			for(int i = 0; i < this.availableAccounts.size(); i++) {
				if(!this.availableAccounts.get(i).
							getUsername().equals(user.getUsername())){
					RMIClientInterface client = this.listRMIClients.get
										(this.availableAccounts.get(i).
							getUsername());
					try {
						client.notifyOnOff(user.getUsername(), "on");
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void addRMIClientInterface(User user) {
		try {
			RMIClientInterface client = 
					(RMIClientInterface)Naming.lookup("rmi://localhost:"
											+ user.getPort() + "/" + user.getUsername());
			this.listRMIClients.put(user.getUsername(), client);
			System.out.println(this.listRMIClients.size());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void openServer(int port) {
		this.serverPort = port;
	}
	
	public void listening() {
		try (ServerSocket serverSocket = new ServerSocket(this.serverPort)){
			System.out.println("Server is listening at the port: " + this.serverPort);
			while(true) {
				Socket socketFromClient = serverSocket.accept();
				//this.view.showMessage("New Client Connected");
				
				ObjectInputStream ois = new ObjectInputStream(socketFromClient.getInputStream());
				Object obj = (Object)ois.readObject();
				User user = (User)obj;
				System.out.println(obj.toString());
				//User user = new 
				//User user = (User)ois.readObject();
				OutputStream output = socketFromClient.getOutputStream();
				PrintWriter writer = new PrintWriter(output, true);
				if(this.checkLogin(user)) {
					boolean isOn = true;
					ServerDBControl.updateOnOff(user.getUsername(), user.getPassword(), isOn);
					this.view.showMessage("Success");
					this.addAvailableUser(user);
					writer.print("Success");
					
					
				}
				else {
					this.view.showMessage("Failed");
					writer.print("Failed");
				}
				
				writer.flush();
				writer.close();
				output.close();
			}
			
		}
		catch(Exception e) {
			System.err.println("Server exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public boolean checkLogin(User user) {
		if(checkAlreadyLogin(user) != -1)//neu user da dang nhap roi thi khong cho dang nhap lan nua
			return false;
		for(User u : listActiveAccounts) {
			if(u.equals(user)) {
				return true;
			}
		}
		return false;
	}
}
