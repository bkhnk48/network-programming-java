/*package control;
import model.*;
import view.*;*/
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import javax.swing.*;

import server.User;
import server.AvailableUserImpl;
import server.AvailableUsersInterface;

public class ClientControl {
	//private User model;
	private ClientView view;
	private int serverHost;
	private int serverPort;
	
	//private ArrayList<User> list;
	
	public ClientControl(ClientView view) {
	    
		this.view = view;
		this.view.addLoginListener(new LoginListener(this));
	}
	
	class LoginListener implements ActionListener{
		private ClientControl control;
		
		public LoginListener(ClientControl control) {
			this.control = control;
		}
		
		public void registry(String username, int port) {
			try {
				Registry registry = LocateRegistry.createRegistry(port);
				registry.rebind(username, new ClientNotificationImpl());
				System.out.println("Client lien lac voi Registry thanh cong");
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent e){
			//Lấy thông tin đăng nhập ở ClientView
			User model = control.view.getUser();
			if(Constant.VALID.equals(Validator.checkValid(model))) {
				String hostname = "localhost";
				int port = 6868;
				try(Socket socket = new Socket(hostname, port)){
					OutputStream out = socket.getOutputStream();
					ObjectOutputStream obj = new ObjectOutputStream(out);
					obj.writeObject(model);
					
					//Nhan ket qua tu Server
					InputStream in = socket.getInputStream();
					BufferedReader bf = new BufferedReader(new InputStreamReader(in));
					String response = bf.readLine();
					control.view.showMessage(response);
					if("Success".equals(response)) {
						System.out.print("User " + model.getUsername() + " logins sucessfully: \n");
						
						registry(model.getUsername(), model.getPort());
						
						try {
							AvailableUsersInterface availUsers = 
									(AvailableUsersInterface)Naming.lookup("rmi://localhost:789/availUsers");
							ArrayList<String> allOtherUsers = availUsers.getAllAvailableUsers();
							if(allOtherUsers != null && allOtherUsers.size() > 0) {
								for(String name : allOtherUsers) {
									if(!name.equals(model.getUsername()))
										System.out.println("\t Available user " + name);
								}
							}
							else {
								System.out.println("\t No one in the room!");
							}
							
							availUsers.updateRMIClient(model);
						}catch(Exception ex) {
							ex.printStackTrace();
						}
					}
					in.close();
					bf.close();
					
					obj.flush();
					obj.close();
					out.close();
				}catch(Exception ex) {
					ex.printStackTrace();
					System.out.println("Server not found");
				}
			}
			else {
				control.view.showMessage("Wrong username or password");
			}
			
		}
		
	}
}