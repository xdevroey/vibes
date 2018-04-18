/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.vibes.fexpression.exception;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class FExpressionException extends Exception {

    public FExpressionException(String message) {
        super(message);
    }

    public FExpressionException(String message, Throwable cause) {
        super(message, cause);
    }

}
