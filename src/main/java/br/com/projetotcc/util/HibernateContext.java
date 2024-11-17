package br.com.projetotcc.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;	//classe para somar o tempo de inicializacao do TOMCAT + HIBERNATE (evita q a aplicacao demore tanto quando em producao para ser carregada pela 1 vez)



public class HibernateContext implements ServletContextListener{  

	@Override
	public void contextInitialized(ServletContextEvent event) { //liga o TOMCAT
		
		HibernateUtil.getFabricaDeSessoes();
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) { //desliga o TOMCAT
		
		HibernateUtil.getFabricaDeSessoes().close();
		
		
		
	}

}
