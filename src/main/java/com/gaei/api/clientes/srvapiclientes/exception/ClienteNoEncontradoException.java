package com.gaei.api.clientes.srvapiclientes.exception;

public class ClienteNoEncontradoException extends RuntimeException {
    public ClienteNoEncontradoException(String idTx) {
        super(String.format("Cliente con idTx %s no encontrado.", idTx));
    }
}
