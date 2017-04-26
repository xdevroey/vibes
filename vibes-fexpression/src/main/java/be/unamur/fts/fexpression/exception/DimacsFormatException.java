/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.unamur.fts.fexpression.exception;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class DimacsFormatException extends FExpressionException {

    public DimacsFormatException(String message) {
        super(message);
    }

    public DimacsFormatException(String message, Throwable cause) {
        super(message, cause);
    }

}
