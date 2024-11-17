 package br.com.projetotcc.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
public class Cliente extends GenericDomain{
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)//Date(java.util) informa data, hora ou data e hora
	private Date dataCadastro;  
	
	@Column(nullable = false)
	private Boolean liberado;
	
	@JoinColumn(nullable = false)
	@OneToOne
	private Pessoa pessoa;

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Boolean getLiberado() {
		return liberado;
	}
	
	@Transient //serve so pra formataçao , nao é um campo físico do bd
	public String getLiberadoString() {
		String liberadoString = null;
		if(liberado == true) {
			liberadoString = "Liberado";
		}else if(liberado == false){
			liberadoString = "Bloqueado";
		}
		return liberadoString;
	}

	public void setLiberado(Boolean liberado) {
		this.liberado = liberado;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	
	
}
