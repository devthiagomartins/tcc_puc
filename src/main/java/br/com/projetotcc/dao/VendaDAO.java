package br.com.projetotcc.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import br.com.projetotcc.domain.Funcionario;
import br.com.projetotcc.domain.ItemVenda;
import br.com.projetotcc.domain.Produto;
import br.com.projetotcc.domain.Usuario;
import br.com.projetotcc.domain.Venda;
import br.com.projetotcc.util.HibernateUtil;

public class VendaDAO extends GenericDAO<Venda>{
	
	public void save(Venda venda, List<ItemVenda> itensVenda) {  // salvar a venda com os itens da venda . 
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;

		try {
			transacao = sessao.beginTransaction();
		
			 sessao.save(venda);
			 
			 for(int posicao =0; posicao < itensVenda.size(); posicao++) {
				 ItemVenda itemVenda = itensVenda.get(posicao);
				 itemVenda.setVenda(venda);
				 
				 sessao.save(itemVenda);
				 
				 Produto produto = itemVenda.getProduto();
			
				 int qtdeAtual = produto.getQuantidade() - itemVenda.getQuantidade(); 
				 
				 if(qtdeAtual >= 0) { // se o q sobrou
					 
					 produto.setQuantidade(new Short(qtdeAtual + ""));
					 
					 sessao.update(produto);
					 
				 }else {
					 
					
					 throw new RuntimeException("Quantidade insuficiente em estoque do produto" +  produto.getDescricao());
					 
				 }
				
			 }
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
	

		 

	public String valorTotalVendas() {
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
	    return sum.toString();
	   
	  }

	
	public String vendasDiaria() {
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
	
	}

