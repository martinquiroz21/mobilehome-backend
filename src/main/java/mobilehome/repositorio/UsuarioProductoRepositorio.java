package mobilehome.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mobilehome.modelo.Usuario;
import mobilehome.modelo.Usuario_Producto;

@Repository
public interface UsuarioProductoRepositorio extends JpaRepository<Usuario_Producto, Long> {
	
	List<Usuario_Producto> findByUsuario(Usuario usuario);
	
}
