package com.generation.farmacia.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.farmacia.model.Produto;
import com.generation.farmacia.repository.CategoriaRepository;
import com.generation.farmacia.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping
	public ResponseEntity<List<Produto>> getAll(){ 
		return ResponseEntity.ok(produtoRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id){
		return produtoRepository.findById(id)
			.map(resp -> ResponseEntity.ok(resp))
			.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Produto>> getByname(@PathVariable String name){
		return ResponseEntity.ok(produtoRepository.findAllByNameContainingIgnoreCase(name));
	}
	
	@PostMapping 
	public ResponseEntity<Produto> postProduto(@Valid @RequestBody Produto produto){ 
		if (categoriaRepository.existsById(produto.getCategoria().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));

		return ResponseEntity.badRequest().build();
	}
	
	@PutMapping
	public ResponseEntity<Produto> putProduto(@Valid @RequestBody Produto produto) {
					
		if (produtoRepository.existsById(produto.getId())) {
		
			if (categoriaRepository.existsById(produto.getCategoria().getId())) 
				return ResponseEntity.ok(produtoRepository.save(produto));
			else
				return ResponseEntity.badRequest().build();
					
		}
		
		return ResponseEntity.notFound().build();

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduto(@PathVariable Long id) {
		
		return produtoRepository.findById(id)
				.map(resposta -> {
					produtoRepository.deleteById(id);
					return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
				})
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/name/{name}/oulaboratorio/{laboratorio}")
	public ResponseEntity<List<Produto>> getByNameOuLaboratorio(@PathVariable String name, @PathVariable String laboratorio){
		return ResponseEntity.ok(produtoRepository.findAllByNameOrLab(name, laboratorio));
	}
	
	@GetMapping("/name/{name}/elaboratorio/{laboratorio}")
	public ResponseEntity<List<Produto>> getByNameELaboratorio(@PathVariable String name, @PathVariable String lab){
		return ResponseEntity.ok(produtoRepository.findAllByNameAndLab(name, lab));
	}
	
	@GetMapping("/preco_inicial/{inicio}/preco_final/{fim}")
	public ResponseEntity<List<Produto>> getByPriceEntreNatve(@PathVariable BigDecimal inicio, @PathVariable BigDecimal fim){
			return ResponseEntity.ok(produtoRepository.findAllByPriceBetween(inicio, fim));
	}

}