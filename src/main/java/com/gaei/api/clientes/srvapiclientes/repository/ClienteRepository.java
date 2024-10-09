package com.gaei.api.clientes.srvapiclientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.gaei.api.clientes.srvapiclientes.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente,String> {
    Optional<Cliente> findByNumeroDocumento(String numeroDocumento);
    Optional<Cliente> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);

}
