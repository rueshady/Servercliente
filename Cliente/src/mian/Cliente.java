package mian;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


@SuppressWarnings("serial")
public class Cliente extends JFrame{

	private JTextField textoUsuario;
	private JTextArea ventanaChat;
	private ObjectOutputStream salida;
	private ObjectInputStream entrada;
	private String mensaje = "";
	private String serverIP;
	private Socket conexion;
	
	public Cliente(String host){
		super("ShadyCliente");
		serverIP = host;
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
			conectarConServer();
			iniciarTransmicion();
			mientrasChat();
		}catch(EOFException eofException){
			mostrarMensaje("\n cliente termino conexion");
		}catch(IOException ioException){
			ioException.printStackTrace();}finally{
			cerrarTransmision();
		}
	}
	private void conectarConServer() throws IOException{
		mostrarMensaje("Esperando conexion... \n");
		conexion = new Socket(InetAddress.getByName(serverIP), 6789);
		mostrarMensaje("Conectado con: " + conexion.getInetAddress().getHostName());
	}
	private void iniciarTransmicion() throws IOException{
		salida = new ObjectOutputStream(conexion.getOutputStream());
		salida.flush();
		entrada = new ObjectInputStream(conexion.getInputStream());
		mostrarMensaje("\n transmicion iniciada \n");
	}
	private void mientrasChat() throws IOException{
		editable(true);
		do{
			try{
				mensaje = (String) entrada.readObject();
				mostrarMensaje("\n" + mensaje);
			}catch(ClassNotFoundException classNotFoundException){
				mostrarMensaje("Esa cosa no era texto!");
			}
		}while(!mensaje.equals("SERVER - FIN"));	
	}
	private void cerrarTransmision(){
		mostrarMensaje("\n Cerrando conexion!");
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
			salida.writeObject("CLIENTE - " + mensaje);
			salida.flush();
			mostrarMensaje("\nCLIENTE - " + mensaje);
		}catch(IOException ioException){
			ventanaChat.append("\n Algo salio mal D:");
		}
	}
	private void mostrarMensaje(final String mensaje){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					ventanaChat.append(mensaje);
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