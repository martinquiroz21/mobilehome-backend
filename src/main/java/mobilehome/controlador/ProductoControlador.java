package mobilehome.controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mobilehome.excepciones.ResourceNotFoundException;
import mobilehome.modelo.Adicional;
import mobilehome.modelo.Producto;
import mobilehome.modelo.Producto_Adicional;
import mobilehome.modelo.RequestPayload;
import mobilehome.modelo.Usuario;
import mobilehome.modelo.Usuario_Producto;
import mobilehome.repositorio.ProductoAdicionalRepositorio;
import mobilehome.repositorio.ProductoRepositorio;
import mobilehome.repositorio.UsuarioProductoRepositorio;
import mobilehome.repositorio.UsuarioRepositorio;

@RestController
@RequestMapping("/api-backend/")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoControlador {
	
	@Autowired
	private ProductoRepositorio productoRepositorio;
	
	@Autowired
	private ProductoAdicionalRepositorio productoAdicionalRepositorio;
	
	@Autowired
	private UsuarioProductoRepositorio usuarioProductoRepositorio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	// Trae una lista de todos los productos
	@GetMapping("/productos")
	public List<Producto> listarProductos() {
		return productoRepositorio.findAll();
	}
	
	// Obtiene una lista de los productos por categoria
	@GetMapping("/productos/{categoria}")
	public List<Producto> obtenerProductosPorCategoria(@PathVariable String categoria) {
		return productoRepositorio.findByCategoria(categoria);
	}

	// Obtiene un producto por id
	@GetMapping("/productos/producto-detalle/{id}")
	public ResponseEntity<Producto> obtenerProductoPorID(@PathVariable Long id) {
		Producto producto = productoRepositorio.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No existe el producto con el ID : " + id));
		return ResponseEntity.ok(producto);
	}
	
	// Obtiene una lista de los adicionales de un producto por su id
	@GetMapping("/productos/producto-adicional/{id}")
	public List<Adicional> obtenerAdicionalesDeUnProductoPorID(@PathVariable Long id) {
		Producto producto = new Producto();
		producto.setId(id);
		List<Producto_Adicional> pads = productoAdicionalRepositorio.findByProducto(producto);
		List<Adicional> adicionales = new ArrayList<Adicional>();
		for (Producto_Adicional pad : pads) {
		    adicionales.add(pad.getAdicional());
		}
		return adicionales;
	}
	
	// Se registra la compra de un producto
	@PostMapping("/productos/producto-detalle/compra")
	public ResponseEntity<Producto> registrarCompra(@RequestBody RequestPayload payload) {
		// Se obtiene el usuario y producto
		Usuario usuario = usuarioRepositorio.findById(payload.getUsuario().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Error usuario!"));
		Producto producto = productoRepositorio.findById(payload.getProducto().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Error producto!"));
		// Se crea el registro del usuario_producto
		Usuario_Producto usuarioProducto = new Usuario_Producto();
		usuarioProducto.setUsuario(usuario);
		usuarioProducto.setProducto(producto);
		usuarioProducto.setFechaCompra(LocalDate.now());
		return ResponseEntity.ok(usuarioProductoRepositorio.save(usuarioProducto).getProducto());
	}
	
}
