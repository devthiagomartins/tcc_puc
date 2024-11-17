package br.com.projetotcc.dao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.com.projetotcc.util.HibernateUtil;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class GenericDAO<Entidade> {

	private Class<Entidade> classe;

	@SuppressWarnings("unchecked")
	public GenericDAO() {
		this.classe = (Class<Entidade>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0]; // busca a classe da entidade, pega a classe generica GenericDAO<Entidade>
												// e pega o 1ºelemento <Entidade> e seta em classe.
	} //

	public void salvar(Entidade entidade) {

		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;

		try {
			transacao = sessao.beginTransaction();
			sessao.save(entidade);
			transacao.commit();
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			throw erro; // avisa que realmente deu o erro.
		} finally { // tanto faz se deu certo ou nao ele executa.
			sessao.close();
		}
	}

	public List<Entidade> listar() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();

		try {

			CriteriaBuilder builder = sessao.getCriteriaBuilder();

			CriteriaQuery<Entidade> consulta = builder.createQuery(classe);
			consulta.from(classe);
			List<Entidade> resultado = sessao.createQuery(consulta).getResultList();
			return resultado; // resultado guarda a listagem..

		} catch (RuntimeException erro) {

			throw erro;
		} finally {

			sessao.close();
		}

	}

	
	public List<Entidade> listar(String campoOrdenado) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();

		try {

			CriteriaBuilder builder = sessao.getCriteriaBuilder();

			CriteriaQuery<Entidade> consulta = builder.createQuery(classe);
			Root<Entidade> ent = consulta.from(classe);
			   consulta.orderBy(builder.asc(ent.get(campoOrdenado)));
			   
			List<Entidade> resultado = sessao.createQuery(consulta).getResultList();
			return resultado; // resultado guarda a listagem..

		} catch (RuntimeException erro) {

			throw erro;
		} finally {

			sessao.close();
		}

	}
	public Entidade buscar(Long id) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();

		Entidade resultado = null;
		try {
			resultado = sessao.find(classe, id);
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
	public void excluir(Entidade entidade) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();

		Transaction transacao = null;

		try {
			transacao = sessao.beginTransaction();
			sessao.delete(entidade);;
			transacao.commit();
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			throw erro; // avisa que realmente deu o erro.
		} finally { // tanto faz se deu certo ou nao ele executa o fechamento da sessao.
			sessao.close();
		}
	}
	
	public void alterar(Entidade entidade) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();

		Transaction transacao = null;

		try {
			transacao = sessao.beginTransaction();
			sessao.update(entidade);
			transacao.commit();
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			throw erro; // avisa que realmente deu o erro.
		} finally { // tanto faz se deu certo ou nao ele executa.
			sessao.close();
		}
	}
	
	public void merge(Entidade entidade) {

		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;

		try {
			transacao = sessao.beginTransaction();
			sessao.merge(entidade);
			transacao.commit();
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			throw erro; // avisa que realmente deu o erro.
		} finally { // tanto faz se deu certo ou nao ele executa.
			sessao.close();
		}
	}

}
