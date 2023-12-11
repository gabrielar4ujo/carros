package br.com.ifpb.carros.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.ifpb.carros.dao.UsuarioDao;
import br.com.ifpb.carros.modelo.Usuario;

@ManagedBean
@ViewScoped
public class LoginBean {

	private Usuario usuario = new Usuario();

	public Usuario getUsuario() {
		return usuario;
	}

	public String efetuaLogin() {
		System.out.println("Fazendo login do usuário " + this.usuario.getEmail());

		FacesContext context = FacesContext.getCurrentInstance();
		boolean existe = new UsuarioDao().existe(this.usuario);

		if (existe) {
			System.out.println("EXISTE");
			context.getExternalContext().getSessionMap().put("usuarioLogado", this.usuario);

			return "livro?faces-redirect=true";
		}
			
		System.out.println("NAÕ EXISTE");
		return null;
	}
	
	public String deslogar() {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove("usuarioLogado");
		return "login?faces-redirect=true";

	}

}
