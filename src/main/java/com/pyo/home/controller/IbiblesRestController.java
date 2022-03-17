package com.pyo.home.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pyo.home.DTO.Bible;
import com.pyo.home.exception.ContentsException;
import com.pyo.home.repository.IbibleRepository;

@RestController
@RequestMapping("/getContents")
public class IbiblesRestController {
	@Autowired
	IbibleRepository ibibleRepository;
	
	@GetMapping("/{contents}")
	public CollectionModel<Bible> retrieveContents(@PathVariable String contents) throws Exception{
		List<Bible> biblelist = ibibleRepository.findAllContents(contents, Sort.by(Sort.Direction.ASC,"id"));
		if(biblelist.size()<=0)
			throw new ContentsException(String.format("Not Exist \'[%s]\'", contents));
		
		// HATEOS 
		CollectionModel<Bible> model = CollectionModel.of(biblelist);
		WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAll());
		model.add(linkTo.withRel("all-contents")); // return all-contents ref
		
		return model;
	}
	
	@GetMapping("/all")
	public List<Bible> retrieveAll() throws Exception{
		return ibibleRepository.findAll();
	}

}
