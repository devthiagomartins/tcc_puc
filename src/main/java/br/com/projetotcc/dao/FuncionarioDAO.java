package br.com.projetotcc.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;


import br.com.projetotcc.domain.Funcionario;
import br.com.projetotcc.util.HibernateUtil;

public class FuncionarioDAO extends GenericDAO<Funcionario>{  // ERRO . 
				
	
	public List<Funcionario> listarOrdenado(String p) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();

		try {

			CriteriaBuilder builder = sessao.getCriteriaBuilder();

			CriteriaQuery<Funcionario> consulta = builder.createQuery(Funcionario.class);
			Root<Funcionario> ent = consulta.from(Funcionario.class);
			consulta.select(ent.alias("p"));
			
					
			   consulta.orderBy(builder.asc(ent.get("p.nome")));
	
			List<Funcionario> resultado = sessao.createQuery(consulta).getResultList();
			return resultado; // resultado guarda a listagem..

		} catch (RuntimeException erro) {

			throw erro;
		} finally {

			sessao.close();
		}

	}
}
