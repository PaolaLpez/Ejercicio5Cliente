package com.mx.cbtis.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.annotation.PostConstruct;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mx.cbtis.modelo.Comentarios;
import com.mx.cbtis.modelo.Proyectos;
import com.mx.cbtis.modelo.Tareas;
import com.mx.cbtis.modelo.Usuario;

@Controller
public class GestorController {

	@Value("${gestor.backend.url:https://ejercicio5myapigestor-pm77.onrender.com}")
	private String backendUrl = "https://ejercicio5myapigestor-pm77.onrender.com";

	@PostConstruct
	public void sanitizeBackendUrl() {
		if (backendUrl != null) {
			backendUrl = backendUrl.trim();
			while (backendUrl.endsWith("/")) {
				backendUrl = backendUrl.substring(0, backendUrl.length() - 1);
			}
		}
	}

	public void setBackendUrl(String backendUrl) {
		if (backendUrl != null) {
			backendUrl = backendUrl.trim();
			while (backendUrl.endsWith("/")) {
				backendUrl = backendUrl.substring(0, backendUrl.length() - 1);
			}
		}
		this.backendUrl = backendUrl;
	}

	
	@GetMapping("/")
	public String getHome() {
		return "home";
	}
	
	@GetMapping("/tutorial")
	public String getTutorial() {
		return "tutorial";
	}
	
	@GetMapping("/manual")
	public String getManual() {
		return "manual";
	}
	
	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}
	
	@GetMapping("/register")
	public String getRegister(Model modelo) {
		
		Usuario usuario = new Usuario();
		modelo.addAttribute("usuario", usuario);
		
		return "register";
	}
	
	@PostMapping("/saveUsuario")
	public ModelAndView setSaveUsuario(@ModelAttribute Usuario usuario) {
                   
    	RestTemplate template = new RestTemplate();
    	String urlservicebd = backendUrl + "/addUsuario";
    	ResponseEntity<Integer> response = template.postForEntity(urlservicebd,usuario,Integer.class);

    	return new ModelAndView("redirect:/login");

	}
	
	@GetMapping("/inicio")
	public String getInicio(Model modelo) {
		
		Proyectos proyectos = new Proyectos();
		modelo.addAttribute("proyectos", proyectos);
		
		return "inicio";
	}
	
	@PostMapping("/saveProyecto")
	public ModelAndView setSaveProyecto(@ModelAttribute Proyectos proyecto) {
		try {
			RestTemplate template = new RestTemplate();
			String urlservicebd = backendUrl + "/addProyecto";
			template.postForEntity(urlservicebd, proyecto, Integer.class);
		} catch (Exception e) {
			System.err.println("Error en saveProyecto: " + e.getMessage());
		}
		return new ModelAndView("redirect:/inicio");
	}
	
	@PostMapping("/consultarProyectos")
	public ResponseEntity<Object> consultarProyectos(Model modelo) {
		
		RestTemplate template = new RestTemplate();
		String urlservicebd = backendUrl + "/buscarProyectos";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Proyectos[]  response = template.postForObject(urlservicebd, headers, Proyectos[].class);
		List<Proyectos> proyectos = Arrays.asList(response);
		modelo.addAttribute("listaProyectos", proyectos);
		
		//ResponseEntity<List<Proyectos>> respuesta = (ResponseEntity<List<Proyectos>>) proyectos;
		
		ResponseEntity<Object> objeto = new ResponseEntity<Object>(proyectos, HttpStatusCode.valueOf(200));
	
		return objeto;
	}
	
	@PostMapping("/eliminarProyecto")
	public ModelAndView eliminarProyecto(@RequestBody Proyectos proyecto) {
		System.out.println("El id del proyecto es: " + proyecto.getId_proyecto());
		RestTemplate  template = new RestTemplate();
		String urlservicebd = backendUrl + "/deleteProyecto";
		HttpEntity<Proyectos> request = new HttpEntity<Proyectos>(proyecto); 
		ResponseEntity<Integer> response = template.exchange(urlservicebd, HttpMethod.DELETE,request,Integer.class);
	
		return new ModelAndView("redirect:/inicio");
	}
	
	@PostMapping ("/actualizarProyecto")
	public ResponseEntity<Object> updateProyecto(@RequestBody Proyectos proyecto) {
		RestTemplate  template = new RestTemplate();
		Map<String,Integer> params = new HashMap<String,Integer>();
		params.put("id_proyecto", proyecto.getId_proyecto());
		String urlservicebd = backendUrl + "/buscarProyecto?id_proyecto={id_proyecto}";
		Proyectos proyectos = template.getForObject(urlservicebd, Proyectos.class, params);
		
		ResponseEntity<Object> objeto = new ResponseEntity<Object>(proyectos, HttpStatusCode.valueOf(200));
		
		return objeto;
	}
	
	
	@PostMapping("/verificarLogin")
	public ResponseEntity<Usuario> verificarUsuario(@RequestBody Usuario paramUser) {
		try {
			String urlservicebd = backendUrl + "/getUsuarioLogin";
			RestTemplate template = new RestTemplate();
			Usuario user = template.postForObject(urlservicebd, paramUser, Usuario.class);
			if (user == null) {
				user = new Usuario();
				user.setNombre("000");
			}
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			Usuario fallback = new Usuario();
			fallback.setNombre("000");
			return ResponseEntity.ok(fallback);
		}
	}
	
	/*@PostMapping("/obtenerTareas")
	public ResponseEntity<Object> obtenerTareas(@RequestBody Tareas idProyecto, Model modelo) {
		String urlservicebd = backendUrl + "/getTarea";
		RestTemplate  template = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Tareas[] response = template.postForObject(urlservicebd, idProyecto, Tareas[].class);
		List<Tareas> listaTareas = Arrays.asList(response);
		modelo.addAttribute("tareas", listaTareas);
		
		ResponseEntity<Object> objeto = new ResponseEntity<Object>(listaTareas, HttpStatusCode.valueOf(200));
	
		return objeto;

	}*/
	
	@PostMapping("/consultarUsuarios")
	public ResponseEntity<Object> consultarUsuarios(Model modelo) {
		
		RestTemplate template = new RestTemplate();
		String urlservicebd = backendUrl + "/buscarUsuarios";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Usuario[]  response = template.postForObject(urlservicebd, headers, Usuario[].class);
		List<Usuario> usuarios = Arrays.asList(response);
		modelo.addAttribute("listaUsuarios", usuarios);
		
		ResponseEntity<Object> objeto = new ResponseEntity<Object>(usuarios, HttpStatusCode.valueOf(200));
	
		return objeto;
	}
	
	@PostMapping("/obtenerTareas")
		public ResponseEntity<List<Tareas>> obtenerTareas(@RequestBody Tareas idProyecto, Model modelo) {
	    String urlservicebd = backendUrl + "/getTarea";
	    RestTemplate template = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    Tareas[] response = template.postForObject(urlservicebd, idProyecto, Tareas[].class);
	    List<Tareas> listaTareas = Arrays.asList(response);
	    modelo.addAttribute("tareas", listaTareas);
	    return ResponseEntity.ok(listaTareas);
	}
	
	@PostMapping("/obtenerComentarios")
		public ResponseEntity<List<Comentarios>> obtenerComentarios(@RequestBody Comentarios idComentario, Model modelo) {
	    String urlservicebd = backendUrl + "/getComentario";
	    RestTemplate template = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    Comentarios[] response = template.postForObject(urlservicebd, idComentario, Comentarios[].class);
	    List<Comentarios> listaComentarios = Arrays.asList(response);
	    modelo.addAttribute("comentarios", listaComentarios);
	    return ResponseEntity.ok(listaComentarios);
	}
	
	//Liliana
	@GetMapping("/proyecto")
	public String getProyecto(Model modelo) {
		Tareas tarea = new Tareas();
		modelo.addAttribute("tarea", tarea);
		
		Comentarios comentario = new Comentarios();
		modelo.addAttribute("comentarios", comentario);
		
		RestTemplate template = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String urlservicebdUsuario = backendUrl + "/buscarUsuarios";
		Usuario[] responseUsuario = template.postForObject(urlservicebdUsuario, headers, Usuario[].class);  
		List<Usuario> usuarios = Arrays.asList(responseUsuario);
		modelo.addAttribute("listaUsuarios", usuarios);
		
		return "proyecto";
	}
	
	@GetMapping("/tarea")
	public String getTarea(Model modelo) {

		RestTemplate template = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Tareas tareas = new Tareas();
		modelo.addAttribute("tareas", tareas);
		
		return "proyecto";
	}
	
	@PostMapping("/saveTarea")
	public ModelAndView setSaveTareas(@ModelAttribute Tareas tareas, @RequestParam(value = "id_proyecto", required = false) Integer idProyectoParam) {
		Integer idProj = tareas.getId_proyecto() != null ? tareas.getId_proyecto() : idProyectoParam;
		try {
			RestTemplate template = new RestTemplate();
			String urlservicebd = backendUrl + "/addTareas";
			template.postForEntity(urlservicebd, tareas, Integer.class);
		} catch (Exception e) {
			System.err.println("Error guardando tarea: " + e.getMessage());
		}
		ModelAndView mav = new ModelAndView("redirect:/proyecto");
		if (idProj != null) {
			mav.addObject("id_proyecto", idProj);
		}
		return mav;
	}

	@PostMapping("/saveComentario")
	@ResponseBody
	public ResponseEntity<Integer> setSaveComentario(@RequestBody Comentarios comentarios) {
		try {
			RestTemplate template = new RestTemplate();
			String urlservicebd = backendUrl + "/addComentario";
			Integer resp = template.postForObject(urlservicebd, comentarios, Integer.class);
			return ResponseEntity.ok(resp != null ? resp : 1);
		} catch (Exception e) {
			System.err.println("Error guardando comentario: " + e.getMessage());
			return ResponseEntity.ok(0);
		}
	}
	
	@PostMapping("/actualizarTarea")
	public ResponseEntity<Object> updateTarea(@RequestBody Tareas tarea) {
		try {
			RestTemplate template = new RestTemplate();
			Map<String,Integer> params = new HashMap<String,Integer>();
			params.put("id_tarea", tarea.getId_tarea());
			String urlservicebd = backendUrl + "/buscarTarea?id_tarea={id_tarea}";
			Tareas tareas = template.getForObject(urlservicebd, Tareas.class, params);
			return new ResponseEntity<Object>(tareas, HttpStatusCode.valueOf(200));
		} catch (Exception e) {
			return new ResponseEntity<Object>(new Tareas(), HttpStatusCode.valueOf(200));
		}
	}
	
	@PostMapping("/eliminarTarea")
	@ResponseBody
	public ResponseEntity<Integer> eliminarTarea(@RequestBody Tareas tareas) {
		try {
			RestTemplate template = new RestTemplate();
			String urlservicebd = backendUrl + "/deleteTareas";
			HttpEntity<Tareas> request = new HttpEntity<Tareas>(tareas);
			ResponseEntity<Integer> response = template.exchange(urlservicebd, HttpMethod.DELETE, request, Integer.class);
			return ResponseEntity.ok(response.getBody() != null ? response.getBody() : 1);
		} catch (Exception e) {
			System.err.println("Error eliminando tarea: " + e.getMessage());
			return ResponseEntity.ok(0);
		}
	}
	
	@PostMapping("/verificarTarea")
	public ResponseEntity<Integer> verificarTarea(@RequestBody Tareas paramName){
		try {
			String urlservicebd = backendUrl + "/addTareas";
			RestTemplate template = new RestTemplate();
			Integer resp = template.postForObject(urlservicebd, paramName, Integer.class);
			return ResponseEntity.ok(resp != null ? resp : 0);
		} catch (Exception e) {
			return ResponseEntity.ok(0);
		}
	}
	
	@PostMapping("/cambiosTareas")
	public ResponseEntity<Integer> cambiosTareas(@RequestBody Tareas paramDatos){
		try {
			String urlservicebd = backendUrl + "/addTareas";
			RestTemplate template = new RestTemplate();
			Integer resp = template.postForObject(urlservicebd, paramDatos, Integer.class);
			return ResponseEntity.ok(resp != null ? resp : 0);
		} catch (Exception e) {
			return ResponseEntity.ok(0);
		}
	}
	
	@PostMapping("/consultarTareas")
	public ResponseEntity<Object> consultarTareas() {	
		RestTemplate template = new RestTemplate();
		String urlservicebd = backendUrl + "/buscarTareas";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Tareas[]  response = template.postForObject(urlservicebd, headers, Tareas[].class);
		List<Tareas> tareas = Arrays.asList(response);
		//ResponseEntity<List<Proyectos>> respuesta = (ResponseEntity<List<Proyectos>>) proyectos;
		ResponseEntity<Object> objeto = new ResponseEntity<Object>(tareas, HttpStatusCode.valueOf(200));
		
		return objeto;

	}
	
	@PostMapping("/consultarComentarios")
	public ResponseEntity<Object> consultarComentarios() {	
		RestTemplate template = new RestTemplate();
		String urlservicebd = backendUrl + "/buscarComentarios";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Comentarios[]  response = template.postForObject(urlservicebd, headers, Comentarios[].class);
		List<Comentarios> comentarios = Arrays.asList(response);
		//ResponseEntity<List<Proyectos>> respuesta = (ResponseEntity<List<Proyectos>>) proyectos;
		ResponseEntity<Object> objeto = new ResponseEntity<Object>(comentarios, HttpStatusCode.valueOf(200));
		
		return objeto;

	}
}
