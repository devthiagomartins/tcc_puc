package br.com.projetotcc.domain;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class SendMail {
		
	
	
	
	
	public void sendMail(String clienteEmail, String nomeCliente) throws EmailException {
		
		
	
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
				email.setMsg("Olá "+nomeCliente+"! A Neofarm agradece você pela sua compra! Obrigado por acreditar em nossos serviços e volte sempre!.");
				email.addTo(clienteEmail);
				email.send();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
	
	}
}
