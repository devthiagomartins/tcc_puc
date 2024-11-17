package br.com.projetotcc.domain;

import java.io.Serializable; // pojo, os frameworks pedem para serializar futuramente, pode dar algum erro .

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


@SuppressWarnings("serial") // avisa o compilador para ignorar o serial da classe.
@MappedSuperclass  //essa classe nao corresponde a uma tabela, mas vai ser usada por outras para gerar tabelas  (classe pai nao vai ser tabela) 
public class GenericDomain implements Serializable{

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) //auto-incremento, id controlado pelo banco.
	private long id;
	
	
	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
	    return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericDomain other = (GenericDomain) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
	
	
	
	
}
