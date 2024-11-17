package br.com.projetotcc.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Ignore;
import org.junit.Test;

import br.com.projetotcc.dao.UsuarioDAO;
import br.com.projetotcc.dao.VendaDAO;
import br.com.projetotcc.domain.Funcionario;
import br.com.projetotcc.domain.Usuario;
import br.com.projetotcc.domain.Venda;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class CidadeDAOTest {
	
	@Test
	public void sendMail() throws EmailException {
		
		
		
		String meuEmail = "devthiagomartins@gmail.com";
		String minhaSenha = "@dev1thmreis";
		
		SimpleEmail email = new SimpleEmail();
		email.setHostName("smtp.gmail.com");
		email.setSmtpPort(587);
		email.setAuthenticator(new DefaultAuthenticator(meuEmail, minhaSenha));
		email.setSSLOnConnect(true);
		
		try {
			email.setFrom(meuEmail);
			email.setSubject("Venda Finalizada!");
			email.setMsg("Olá A Neofarm agradece você pela sua compra! Obrigado por acreditar em nossos serviços e volte sempre!.");
			email.addTo("devthiagomartins@gmail.com");
			email.send();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		

}
	
		 


		


	
	
	@Ignore
	@Test
	public void ultimaVenda() {
		
		List<Venda> vendas = null;
		
			VendaDAO dao = new VendaDAO();
			vendas = dao.listar();
			
			String clienteDaVenda = vendas.get(vendas.size() - 1).getCliente().getPessoa().getEmail();
			
			
		System.out.println(vendas.get(vendas.size()-1).getValorTotal());
		String nomeCliente = vendas.get(vendas.size() - 1).getCliente().getPessoa().getNome();
		System.out.println(clienteDaVenda);
	}
	/*
	@Test
	public void totVendas() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;
		
		VendaDAO dao = new VendaDAO();
		List teste;
		teste = dao.valorTotalVendas();
		System.out.println(teste);
}


	@Ignore
	@Test
	public void valorTotalVendas() {
		BigDecimal sum = BigDecimal.ZERO;
		
		
		try {
		Connection con = HibernateUtil.getConexao();
		
	    Statement st = con.createStatement();
	    ResultSet res = st.executeQuery("SELECT SUM(valorTotal) FROM venda");
	    while (res.next()) {
	      BigDecimal c = res.getBigDecimal(1);
	      sum = sum.add(c);
	      System.out.println("Sum of column = " + sum);
	    }
		} catch (SQLException e) {
			// TODO: handle exception
		}
	    
	    System.out.println("Sum of column = " + sum);
	  }
	@Ignore
	@Test
	public void vendasDiaria() {
		int count=0;
		
		
		try {
		Connection con = HibernateUtil.getConexao();
		
	    Statement st = con.createStatement();
	    ResultSet res = st.executeQuery("SELECT count(*)AS total FROM VENDA WHERE DATE_FORMAT(horario, '%Y-%m-%d') = CURDATE()");
	    while (res.next()) {
	     count = res.getInt("total");
	      System.out.println("Sum of column = " + count);
	    }
		} catch (SQLException e) {
			// TODO: handle exception
		}
	   
	  }
	@Ignore
	@Test
	public String vendasDiaria2() {
		int count=0;
		
		try {
		Connection con = HibernateUtil.getConexao();
		
	    Statement st = con.createStatement();
	    ResultSet res = st.executeQuery("SELECT count(*)AS total FROM VENDA WHERE DATE_FORMAT(horario, '%Y-%m-%d') = CURDATE()");
	    while (res.next()) {
	     count = res.getInt("total");
	    }
		} catch (SQLException e) {
			// TODO: handle exception
		}
	   return String.valueOf(count);
	  }
	  	*/
	}



