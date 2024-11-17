package br.com.projetotcc.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import br.com.projetotcc.domain.Cidade;
import br.com.projetotcc.util.HibernateUtil;

public class CidadeDAO extends GenericDAO<Cidade>{
										//chave primária do estado.
	public List<Cidade> buscaPorEstado(Long idEstado) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();

		try {

			CriteriaBuilder builder = sessao.getCriteriaBuilder();

			CriteriaQuery<Cidade> consulta = builder.createQuery(Cidade.class);
			Root<Cidade> root = consulta.from(Cidade.class);
			 
			   CriteriaQuery<Cidade> select = consulta.select(root);

			   Predicate p1 = builder.equal(root.get("estado").get("id"), idEstado);

			   TypedQuery<Cidade> typedQuery = sessao.createQuery(select.where(p1));
			   List<Cidade> result = typedQuery.getResultList();
			return result; // resultado guarda a listagem..

		} catch (RuntimeException erro) {

			throw erro;
		} finally {

			sessao.close();
		}
	}
}

