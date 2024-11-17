package br.com.projetotcc.util;
import org.hibernate.Session;
import org.junit.Test;
import br.com.projetotcc.util.HibernateUtil;

public class HibernateUtilTest {
	
	@Test
	public  void conectar() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		sessao.close();
		HibernateUtil.getFabricaDeSessoes().close();
	}
}