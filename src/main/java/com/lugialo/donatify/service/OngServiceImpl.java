package com.lugialo.donatify.service;

import com.lugialo.donatify.dto.OngCreateUpdateDto;
import com.lugialo.donatify.dto.OngDto;
import com.lugialo.donatify.model.Ong;
import com.lugialo.donatify.repository.OngRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OngServiceImpl implements OngService {

    @Autowired
    private OngRepository ongRepository;

    @Override
    @Transactional
    public OngDto createOng(OngCreateUpdateDto ongDto) {
        // Verifica se já existe uma ONG com o mesmo nome para evitar duplicatas
        ongRepository.findByNameIgnoreCase(ongDto.getName()).ifPresent(ong -> {
            throw new IllegalArgumentException("ONG com o nome '" + ongDto.getName() + "' já existe.");
        });

        Ong newOng = new Ong();
        newOng.setName(ongDto.getName());

        Ong savedOng = ongRepository.save(newOng);
        return OngDto.fromEntity(savedOng);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OngDto> getAllOngs() {
        return ongRepository.findAll().stream()
                .map(OngDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OngDto getOngById(Long id) {
        return ongRepository.findById(id)
                .map(OngDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("ONG com ID " + id + " não encontrada."));
    }

    @Override
    @Transactional
    public OngDto updateOng(Long id, OngCreateUpdateDto ongDto) {
        Ong ongToUpdate = ongRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ONG com ID " + id + " não encontrada."));

        ongToUpdate.setName(ongDto.getName());

        Ong updatedOng = ongRepository.save(ongToUpdate);
        return OngDto.fromEntity(updatedOng);
    }

    @Override
    @Transactional
    public void deleteOng(Long id) {
        if (!ongRepository.existsById(id)) {
            throw new IllegalArgumentException("ONG com ID " + id + " não encontrada, não foi possível deletar.");
        }
        // Atenção: Usuários associados a esta ONG terão seu campo 'ong_id' definido como NULL,
        // conforme a regra 'ON DELETE SET NULL' que definimos no banco.
        ongRepository.deleteById(id);
    }
}
