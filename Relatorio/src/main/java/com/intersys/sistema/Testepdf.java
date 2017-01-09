package com.intersys.sistema;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.eclipse.jdt.internal.compiler.batch.FileSystem;

import com.intersys.relatorio.fabricaconexao.FabricaDeConexao;

import oracle.sql.BLOB;

public class Testepdf {
	public static void main(String[] args) {

//		try {
			// String sql = "update CADP01_REQUISICOES set P1R_ARQUIVO_PDF=?
			// where p1r_chave=345642";
			// Path path =
			// Paths.get("C:/Users/PROGRAMADOR-02/Desktop/relatorio/teste-master/Relatorio/relatorio2.pdf");
			// byte[] data = Files.readAllBytes(path);
			// try {
			// try (Connection connection =
			// FabricaDeConexao.getInstancia().getConnxao()) {
			// try (PreparedStatement statement =
			// connection.prepareStatement(sql)) {
			// statement.setBytes(1, data);;
			// statement.executeUpdate();
			// System.out.println("statement ok!");
			// }
			// } catch (FileNotFoundException e1) {
			// System.out.println("statement 1!");
			//
			// e1.printStackTrace();
			// }
			// } catch (SQLException e) {
			// System.out.println("statement 2!");
			//
			// e.printStackTrace();
			// System.out.println("statement 3!");
			//
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			//
			//
			// System.out.println("atualizado com sucesso!");
//			String sql = "update CADP01_REQUISICOES set P1R_ARQUIVO_PDF=? where p1r_chave=345642";
//			File file = new File("C:/Users/PROGRAMADOR-02/Desktop/relatorio/teste-master/Relatorio/relatorio2.pdf");
//			System.out.println(file.length());
//			FileInputStream fis = new FileInputStream(file);
//			byte[] bytes = new byte[(int) file.length()];
//			Connection connection = FabricaDeConexao.getInstancia().getConnxao();
//			PreparedStatement statement = connection.prepareStatement(sql);
//			statement.setBinaryStream(1, fis,(int)file.length());
//			statement.execute();
//			System.out.println("atualizado com sucesso");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
		  try{ 
			  String sql="select P1R_ARQUIVO_PDF from CADP01_REQUISICOES where p1r_chave=345642";
			  Connection connection = FabricaDeConexao.getInstancia().getConnxao();
			  PreparedStatement statement = connection.prepareStatement(null);
			   ResultSet resultSet = statement.executeQuery();
			    if( resultSet.next() ){
			      Blob blob = resultSet.getBlob(1);
			      ImageIcon imageIcon = new ImageIcon(blob.getBytes(1, (int)blob.length())); 
			    }
			    
			  
	} catch (Exception e) {
		e.printStackTrace();
	}
		  try{ 
			String sql = "update CADP01_REQUISICOES set P1R_ARQUIVO_PDF=? where p1r_chave=345642";
			File file = new File("C:/Users/PROGRAMADOR-02/Desktop/relatorio/teste-master/Relatorio/relatorio2.pdf");
			System.out.println(file.length());
			FileInputStream fis = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			Connection connection = FabricaDeConexao.getInstancia().getConnxao();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setBinaryStream(1, fis,(int)file.length());
			statement.execute();
			System.out.println("atualizado com sucesso");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

}
