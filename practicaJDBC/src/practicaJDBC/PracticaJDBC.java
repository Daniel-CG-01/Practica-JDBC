package practicaJDBC;

import java.sql.*;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PracticaJDBC {

	private JFrame frame;
	private JTable table;
	private JTextField textFieldNumero;
	private JTextField textFieldHora;
	private JTextField textFieldVuelo;
	private JTextField textFieldDestino;
	private JTextField textFieldMostrador;
	private JTextField textFieldPtaEmbarque;

	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PracticaJDBC window = new PracticaJDBC();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	
	public PracticaJDBC() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	class ConnectionSingleton {
		private static Connection con;
		
		public static Connection getConnection() throws SQLException {
			//Estas tres lineas sirven para que el programa Java reconozca la base de datos
			String url="jdbc:mysql://127.0.0.1:3307/aeropuerto";
			String user="alumno";
			String password="alumno";
			
			if (con==null ||  con.isClosed()) {
				con=DriverManager.getConnection(url, user, password);
			}
			return con;
		}
	}
	
	private void initialize() {
		try {
			Connection con = ConnectionSingleton.getConnection();
			
			frame = new JFrame();
			frame.setBounds(100, 100, 800, 350);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(null);
			
			DefaultTableModel modelTable = new DefaultTableModel();
			modelTable.addColumn("nº");
			modelTable.addColumn("Hora");
			modelTable.addColumn("Vuelo");
			modelTable.addColumn("Destino");
			modelTable.addColumn("Mostrador");
			modelTable.addColumn("Pta. Embarque");
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM avion");
			while (rs.next()) {
				Object[] row = new Object[6];
				row[0] = rs.getInt("numero");
				row[1] = rs.getString("hora");
				row[2] = rs.getString("vuelo");
				row[3] = rs.getString("destino");
				
				int mostradorInicial = rs.getInt("mostradorInicial");
				int mostradorFinal = rs.getInt("mostradorFinal");
				
				row[4] = mostradorInicial+"-"+mostradorFinal;
				row[5] = rs.getInt("ptaEmbarque");
				modelTable.addRow(row);
			}
			rs.close();
			stmt.close();
			
			table = new JTable(modelTable);
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int index = table.getSelectedRow();
					TableModel modelUsuario = table.getModel();
					textFieldNumero.setText(modelUsuario.getValueAt(index, 0).toString());
					textFieldHora.setText(modelUsuario.getValueAt(index, 1).toString());
					textFieldVuelo.setText(modelUsuario.getValueAt(index, 2).toString());
					textFieldDestino.setText(modelUsuario.getValueAt(index, 3).toString());
					textFieldMostrador.setText(modelUsuario.getValueAt(index, 4).toString());
					textFieldPtaEmbarque.setText(modelUsuario.getValueAt(index, 5).toString());
				}
			});
			table.setBounds(10, 10, 500, 275);
			frame.getContentPane().add(table);
			
			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setBounds(10, 10, 535, 275);
			frame.getContentPane().add(scrollPane);
			
			JLabel lblNumero = new JLabel("nº: ");
			lblNumero.setBounds(555, 13, 45, 13);
			frame.getContentPane().add(lblNumero);
			
			JLabel lblHora = new JLabel("Hora: ");
			lblHora.setBounds(555, 36, 45, 13);
			frame.getContentPane().add(lblHora);
			
			JLabel lblVuelo = new JLabel("Vuelo: ");
			lblVuelo.setBounds(555, 59, 45, 13);
			frame.getContentPane().add(lblVuelo);
			
			JLabel lblDestino = new JLabel("Destino: ");
			lblDestino.setBounds(555, 82, 85, 13);
			frame.getContentPane().add(lblDestino);
			
			JLabel lblMostrador = new JLabel("Mostrador: ");
			lblMostrador.setBounds(555, 105, 85, 13);
			frame.getContentPane().add(lblMostrador);
			
			JLabel lblPtaEmbarque = new JLabel("Pta. Embarque: ");
			lblPtaEmbarque.setBounds(555, 128, 115, 13);
			frame.getContentPane().add(lblPtaEmbarque);
			
			textFieldNumero = new JTextField();
			textFieldNumero.setBounds(620, 10, 96, 19);
			frame.getContentPane().add(textFieldNumero);
			textFieldNumero.setColumns(10);
			
			textFieldNumero.setEnabled(false);
			
			textFieldHora = new JTextField();
			textFieldHora.setBounds(620, 33, 96, 19);
			frame.getContentPane().add(textFieldHora);
			textFieldHora.setColumns(10);
			
			textFieldVuelo = new JTextField();
			textFieldVuelo.setBounds(620, 56, 96, 19);
			frame.getContentPane().add(textFieldVuelo);
			textFieldVuelo.setColumns(10);
			
			textFieldDestino = new JTextField();
			textFieldDestino.setBounds(650, 79, 96, 19);
			frame.getContentPane().add(textFieldDestino);
			textFieldDestino.setColumns(10);
			
			textFieldMostrador = new JTextField();
			textFieldMostrador.setBounds(650, 102, 96, 19);
			frame.getContentPane().add(textFieldMostrador);
			textFieldMostrador.setColumns(10);
			
			textFieldPtaEmbarque = new JTextField();
			textFieldPtaEmbarque.setBounds(680, 125, 96, 19);
			frame.getContentPane().add(textFieldPtaEmbarque);
			textFieldPtaEmbarque.setColumns(10);
			
			JButton botonInsertar = new JButton("Insertar");
			botonInsertar.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					String hora=textFieldHora.getText();
					
					String vuelo=textFieldVuelo.getText();
					
					String destino=textFieldDestino.getText();
					
					String mostrador=textFieldMostrador.getText();
					String[] elementos=mostrador.split("-");
					
					String ptaEmbarqueTexto=textFieldPtaEmbarque.getText();
					int ptaEmbarque=Integer.parseInt(ptaEmbarqueTexto);
					
					try {
						Connection con = ConnectionSingleton.getConnection();
						
						PreparedStatement ins_pstmt = con.prepareStatement("INSERT INTO avion (hora, vuelo, destino, "
								 + "mostradorInicial, mostradorFinal, ptaEmbarque) "
								 + "VALUES (?, ?, ?, ?, ?, ?)");
						ins_pstmt.setString(1, hora);
						ins_pstmt.setString(2, vuelo);
						ins_pstmt.setString(3, destino);
						
						int mostradorInicial=Integer.parseInt(elementos[0].trim());
						int mostradorFinal=Integer.parseInt(elementos[1].trim());
						
						ins_pstmt.setInt(4, mostradorInicial);
						ins_pstmt.setInt(5, mostradorFinal);
						ins_pstmt.setInt(6, ptaEmbarque);
						ins_pstmt.executeUpdate();
						ins_pstmt.close();
						
						textFieldHora.setText("");
						textFieldVuelo.setText("");
						textFieldDestino.setText("");
						textFieldMostrador.setText("");
						textFieldPtaEmbarque.setText("");
						
						Statement stmt = con.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT * FROM avion");
						modelTable.setRowCount(0);
						while (rs.next()) {
							Object[] row = new Object[6];
							row[0] = rs.getInt("numero");
							row[1] = rs.getString("hora");
							row[2] = rs.getString("vuelo");
							row[3] = rs.getString("destino");
							
							mostradorInicial = rs.getInt("mostradorInicial");
							mostradorFinal = rs.getInt("mostradorFinal");
							
							row[4] = mostradorInicial+"-"+mostradorFinal;
							row[5] = rs.getInt("ptaEmbarque");
							modelTable.addRow(row);
						}
						rs.close();
						stmt.close();
					} catch (SQLException e2) {
						e2.printStackTrace();
					}
				}
			});
			botonInsertar.setBounds(555, 203, 115, 21);
			frame.getContentPane().add(botonInsertar);
			
			JButton botonActualizar = new JButton("Actualizar");
			botonActualizar.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					String numeroTexto=textFieldNumero.getText();
					int numero=Integer.parseInt(numeroTexto);
					
					String hora=textFieldHora.getText();
					
					String vuelo=textFieldVuelo.getText();
					
					String destino=textFieldDestino.getText();
					
					String mostrador=textFieldMostrador.getText();
					String[] elementos=mostrador.split("-");
					
					String ptaEmbarqueTexto=textFieldPtaEmbarque.getText();
					int ptaEmbarque=Integer.parseInt(ptaEmbarqueTexto);
					
					try {
						Connection con = ConnectionSingleton.getConnection();
						
						PreparedStatement upd_pstmt = con.prepareStatement("UPDATE avion SET hora = ?, vuelo = ?, "
																		 + "destino = ?, mostradorInicial = ?, "
																		 + "mostradorFinal = ?, ptaEmbarque = ? "
																		 + "WHERE numero = ?");
						upd_pstmt.setString(1, hora);
						upd_pstmt.setString(2, vuelo);
						upd_pstmt.setString(3, destino);
						
						int mostradorInicial=Integer.parseInt(elementos[0].trim());
						int mostradorFinal=Integer.parseInt(elementos[1].trim());
						
						upd_pstmt.setInt(4, mostradorInicial);
						upd_pstmt.setInt(5, mostradorFinal);
						upd_pstmt.setInt(6, ptaEmbarque);
						upd_pstmt.setInt(7, numero);
						upd_pstmt.executeUpdate();
						upd_pstmt.close();
						
						textFieldNumero.setText("");
						textFieldHora.setText("");
						textFieldVuelo.setText("");
						textFieldDestino.setText("");
						textFieldMostrador.setText("");
						textFieldPtaEmbarque.setText("");
						
						Statement stmt = con.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT * FROM avion");
						modelTable.setRowCount(0);
						while (rs.next()) {
							Object[] row = new Object[6];
							row[0] = rs.getInt("numero");
							row[1] = rs.getString("hora");
							row[2] = rs.getString("vuelo");
							row[3] = rs.getString("destino");
							
							mostradorInicial = rs.getInt("mostradorInicial");
							mostradorFinal = rs.getInt("mostradorFinal");
							
							row[4] = mostradorInicial+"-"+mostradorFinal;
							row[5] = rs.getInt("ptaEmbarque");
							modelTable.addRow(row);
						}
						rs.close();
						stmt.close();
					} catch (SQLException e2) {
						e2.printStackTrace();
					}
				}
			});
			botonActualizar.setBounds(555, 234, 115, 21);
			frame.getContentPane().add(botonActualizar);
			
			JButton botonBorrar = new JButton("Borrar");
			botonBorrar.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					String numeroTexto=textFieldNumero.getText();
					int numero=Integer.parseInt(numeroTexto);
					
					try {
						Connection con = ConnectionSingleton.getConnection();
						
						PreparedStatement dele_pstmt = con.prepareStatement("DELETE FROM avion WHERE numero = ?");
						dele_pstmt.setInt(1, numero);
						dele_pstmt.executeUpdate();
						dele_pstmt.close();
						
						textFieldNumero.setText("");
						textFieldHora.setText("");
						textFieldVuelo.setText("");
						textFieldDestino.setText("");
						textFieldMostrador.setText("");
						textFieldPtaEmbarque.setText("");
						
						Statement stmt = con.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT * FROM avion");
						modelTable.setRowCount(0);
						while (rs.next()) {
							Object[] row = new Object[6];
							row[0] = rs.getInt("numero");
							row[1] = rs.getString("hora");
							row[2] = rs.getString("vuelo");
							row[3] = rs.getString("destino");
							
							int mostradorInicial = rs.getInt("mostradorInicial");
							int mostradorFinal = rs.getInt("mostradorFinal");
							
							row[4] = mostradorInicial+"-"+mostradorFinal;
							row[5] = rs.getInt("ptaEmbarque");
							modelTable.addRow(row);
						}
						rs.close();
						stmt.close();
					} catch (SQLException e2) {
						e2.printStackTrace();
					}
				}
			});
			botonBorrar.setBounds(555, 265, 115, 21);
			frame.getContentPane().add(botonBorrar);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}