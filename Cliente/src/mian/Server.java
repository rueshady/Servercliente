package mian;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Server extends JFrame {

	private JTextField textoUsuario;
	private JTextArea ventanaChat;
	private ObjectOutputStream salida;
	private ObjectInputStream entrada;
	private ServerSocket server;
	private Socket conexion;

	public Server(){
		super("ShadySerer");
		textoUsuario = new JTextField();
		textoUsuario.setEditable(false);
		textoUsuario.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					mandarMensaje(event.getActionCommand());
					textoUsuario.setText("");
				}
			}
		);
		add(textoUsuario, BorderLayout.NORTH);
		ventanaChat = new JTextArea();
		add(new JScrollPane(ventanaChat));
		setSize(300, 150); 
		setVisible(true);
	}
	public void iniciarPrograma(){
		try{
			server = new ServerSocket(6789, 100); 
			while(true){
				try{
					esperarCliente();
					iniciarConexiones();
					mientrasChat();
				}catch(EOFException eofException){
					mostrarMensaje("\n Server ended the conexion! ");
				} finally{
					cerrarConexion();
				}
			}
		} catch (IOException ioException){
			ioException.printStackTrace();
		}
	}
	private void esperarCliente() throws IOException{
		mostrarMensaje(" Esperando conexion... \n");
		conexion = server.accept();
		mostrarMensaje(" Conectado con " + conexion.getInetAddress().getHostName());
	}
	private void iniciarConexiones() throws IOException{
		salida = new ObjectOutputStream(conexion.getOutputStream());
		salida.flush();
		entrada = new ObjectInputStream(conexion.getInputStream());
		mostrarMensaje("\n Transmicion iniciada \n");
	}
	private void mientrasChat() throws IOException{
		String mensaje = " conectado! ";
		mandarMensaje(mensaje);
		editable(true);
		do{
			try{
				mensaje = (String) entrada.readObject();
				mostrarMensaje("\n" + mensaje);
			}catch(ClassNotFoundException classNotFoundException){
				mostrarMensaje("Eso no era texto");
			}
		}while(!mensaje.equals("CLIENTE - FIN"));
	}
	public void cerrarConexion(){
		mostrarMensaje("\n Cerrando transmisores \n");
		editable(false);
		try{
			salida.close();
			entrada.close();
			conexion.close();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	private void mandarMensaje(String mensaje){
		try{
			salida.writeObject("SERVER - " + mensaje);
			salida.flush();
			mostrarMensaje("\nSERVER -" + mensaje);
		}catch(IOException ioException){
			ventanaChat.append("\n ERROr no se puede enviar el mensaje reinte");
		}
	}
	private void mostrarMensaje(final String text){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					ventanaChat.append(text);
				}
			}
		);
	}
	private void editable(final boolean tof){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					textoUsuario.setEditable(tof);
				}
			}
		);
	}
}