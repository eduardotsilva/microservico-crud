package com.eduardo.crud.entity;

import com.eduardo.crud.data.vo.ProdutoVO;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "produto")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Produto implements Serializable {

    private static final long serialVersionUID = -408166828249124445L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome",  nullable = false, length = 255)
    private String nome;

    @Column(name = "estoque",  nullable = false, length = 10)
    private Integer estoque;

    @Column(name = "preco",  nullable = false, length = 10)
    private Double preco;

    public static Produto create(ProdutoVO produtoVO){
        return new ModelMapper().map(produtoVO, Produto.class);
    }

}