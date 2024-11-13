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
public class Usuario extends GenericDomain{

		@Column(length = 32, nullable = false) //length 32, hash md5 ( 32 )
		private String senha;
		
		@Transient
		private String senhaSemCriptografia; // n é a q vai salvar do banco , é apenas pra mostrar pro usuario caso de algum erro .
		
		@Column(nullable = false)
		private Character tipo;
		
		@Column(nullable = false)
		@Temporal(TemporalType.DATE)//Date(java.util) informa data, hora ou data e hora
		private Date dataCadastro;  
		
		@Column(nullable = false)
		private Boolean ativo;
		
		@JoinColumn(nullable = false)
		@OneToOne
		private Pessoa pessoa;

		public String getSenha() {
			return senha;
		}

		public void setSenha(String senha) {
			this.senha = senha;
		}

		public Character getTipo() {
			return tipo;
		}
		
		public String getTipoString() {
			String tipoString = null;
			if(tipo == 'A') {
				tipoString = "Administrador";
			}else if(tipo == 'G') {
				tipoString = "Gerente";
			}else if(tipo == 'B') {
				tipoString = "Balconista";
			}
			return tipoString;
		}

		public void setTipo(Character tipo) {
			this.tipo = tipo;
		}

		public Boolean getAtivo() {
			return ativo;
		}
		
		@Transient
		public String getAtivoString() {
			String ativoString = null;
			if(ativo) {
				ativoString = "Ativo";
			}else if(ativo == false) {
				ativoString = "Inativo"; 
			}
			return ativoString;
		}

		public void setAtivo(Boolean ativo) {
			this.ativo = ativo;
		}

		public Pessoa getPessoa() {
			return pessoa;
		}

		public void setPessoa(Pessoa pessoa) {
			this.pessoa = pessoa;
		}

		public Date getDataCadastro() {
			return dataCadastro;
		}

		public void setDataCadastro(Date dataCadastro) {
			this.dataCadastro = dataCadastro;
		}

		public String getSenhaSemCriptografia() {
			return senhaSemCriptografia;
		}

		public void setSenhaSemCriptografia(String senhaSemCriptografia) {
			this.senhaSemCriptografia = senhaSemCriptografia;
		}
		
		
}
