/*package test;
import view.*;
import control.*;*/

public class ClientRun {

	public static void main(String[] args) {
		ClientView view = new ClientView();
		ClientControl control = new ClientControl(view);
		view.setVisible(true);
	}

}