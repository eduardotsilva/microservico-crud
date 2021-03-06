package com.eduardo.crud.services;

import com.eduardo.crud.data.vo.ProdutoVO;
import com.eduardo.crud.entity.Produto;
import com.eduardo.crud.exception.ResourceNotFoundException;
import com.eduardo.crud.message.ProdutoSendMessage;
import com.eduardo.crud.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.OptionalInt;

@Service
public class ProdutoService {
	private final ProdutoRepository produtoRepository;
	private final ProdutoSendMessage produtoSendMessage;

	@Autowired
	public ProdutoService(ProdutoRepository produtoRepository, ProdutoSendMessage produtoSendMessage) {
		this.produtoRepository = produtoRepository;
		this.produtoSendMessage = produtoSendMessage;
	}

	public ProdutoVO create(ProdutoVO produtoVO) {
		ProdutoVO produtoRetorno = ProdutoVO.create(produtoRepository.save(Produto.create(produtoVO)));
		produtoSendMessage.SendMessage(produtoRetorno);
		return produtoRetorno;
	}

	public Page<ProdutoVO> findAll(Pageable pageable) {
		var page = produtoRepository.findAll(pageable);
		return page.map(this::convertToProdutoVO);
	}

	private ProdutoVO convertToProdutoVO(Produto produto) {
		return ProdutoVO.create(produto);
	}

	public ProdutoVO findById(Long id) {
		var entity = produtoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		return ProdutoVO.create(entity);
	}

	public ProdutoVO update(ProdutoVO produtoVO) {
		final Optional<Produto> optionalProduto = produtoRepository.findById(produtoVO.getId());

		return ProdutoVO.create(produtoRepository.save(Produto.create(produtoVO)));
	}

	public void delete(long id) {
		var entity = produtoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		produtoRepository.delete(entity);
	}

}
