package com.lugialo.donatify.service;

import com.lugialo.donatify.dto.OngCreateUpdateDto;
import com.lugialo.donatify.dto.OngDto;

import java.util.List;

public interface OngService {

    /**
     * Cria uma nova ONG.
     * @param ongDto Dados para a nova ONG.
     * @return A ONG criada.
     */
    OngDto createOng(OngCreateUpdateDto ongDto);

    /**
     * Retorna uma lista de todas as ONGs.
     * @return Lista de ONGs.
     */
    List<OngDto> getAllOngs();

    /**
     * Busca uma ONG pelo seu ID.
     * @param id O ID da ONG.
     * @return A ONG encontrada.
     */
    OngDto getOngById(Long id);

    /**
     * Atualiza uma ONG existente.
     * @param id O ID da ONG a ser atualizada.
     * @param ongDto os novos dados para a ONG.
     * @return A ONG atualizada.
     */
    OngDto updateOng(Long id, OngCreateUpdateDto ongDto);

    /**
     * Deleta uma ONG pelo seu ID.
     * @param id O ID da ONG a ser deletada.
     */
    void deleteOng(Long id);
}