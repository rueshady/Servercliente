package mian;
import javax.swing.JFrame;

public class Mian {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Cliente cliente;
		cliente = new Cliente("127.0.0.1");
		cliente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cliente.iniciarPrograma();
		Server server = new Server();
		server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		server.iniciarPrograma();
	}

}
