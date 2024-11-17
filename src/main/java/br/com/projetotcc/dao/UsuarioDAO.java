package br.com.projetotcc.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.hibernate.Session;
import br.com.projetotcc.domain.Pessoa;
import br.com.projetotcc.domain.Usuario;
import br.com.projetotcc.util.HibernateUtil;

public class UsuarioDAO extends GenericDAO<Usuario>{
		
		public Usuario autenticar(String cpf, String senha) {
			
			
			Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();

			try {
				SimpleHash hash = new SimpleHash("md5", senha);
				CriteriaBuilder builder = sessao.getCriteriaBuilder();
				CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
				Root<Usuario> root = query.from(Usuario.class);
				Join<Usuario,Pessoa> joinUser = root.join("pessoa"); 
				query.where(
						builder.equal( joinUser.get("cpf"), cpf ),
						builder.equal( root.get("senha"), hash.toHex() )
						
						);
				
				
				Usuario user = sessao.createQuery(query).uniqueResult();
				
				return user;
				

			} catch (RuntimeException erro) {
				throw erro;
			} finally {
				sessao.close();
			}
			
}
}
