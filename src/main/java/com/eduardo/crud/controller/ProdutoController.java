package com.eduardo.crud.controller;

import com.eduardo.crud.data.vo.ProdutoVO;
import com.eduardo.crud.services.ProdutoService;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    private final ProdutoService produtoService;

    private final PagedResourcesAssembler<ProdutoVO> assembler;

    public ProdutoController(ProdutoService produtoService, PagedResourcesAssembler<ProdutoVO> assembler) {
        this.produtoService = produtoService;
        this.assembler = assembler;
    }

    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public ProdutoVO findById(@PathVariable("id") Long id) {
        ProdutoVO produtoVO = produtoService.findById(id);
        produtoVO.add(linkTo(methodOn(ProdutoController.class).findById(id)).withSelfRel());
        return produtoVO;
    }

    @GetMapping(produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<?> findByAll
            (
                    @RequestParam(value = "page", defaultValue = "0") int page,
                    @RequestParam(value = "limit", defaultValue = "12") int limit,
                    @RequestParam(value = "direction", defaultValue = "asc") String direction
            ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome"));

        Page<ProdutoVO> produtos = produtoService.findAll(pageable);

        produtos
                .stream()
                .forEach(p -> p.add(linkTo(methodOn(ProdutoController.class).findById(p.getId())).withSelfRel()));

        PagedModel<EntityModel<ProdutoVO>> pagedModel = assembler.toModel(produtos);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @PostMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ProdutoVO create(@RequestBody ProdutoVO produtoVO) {
        ProdutoVO proVo = produtoService.create(produtoVO);
        proVo.add(linkTo(methodOn(ProdutoController.class).findById(proVo.getId())).withSelfRel());
        return proVo;
    }

    @PutMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ProdutoVO update(@RequestBody ProdutoVO produtoVO) {
        ProdutoVO proVo = produtoService.update(produtoVO);
        proVo.add(linkTo(methodOn(ProdutoController.class).findById(proVo.getId())).withSelfRel());
        return proVo;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        produtoService.delete(id);
        return ResponseEntity.ok().build();
    }
}

