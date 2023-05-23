package com.generation.farmacia.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.farmacia.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	
	public List <Produto> findAllByNameContainingIgnoreCase(@Param("name")String name);
	public List <Produto> findAllByNameAndLab(String name, String lab);
	public List <Produto> findAllByNameOrLab(String name, String lab);
	List <Produto> findAllByPriceBetween(BigDecimal inicio, BigDecimal fim);
	
} 
