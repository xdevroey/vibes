/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.unamur.transitionsystem.transformation.util;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author gperroui
 */
public class TransformationUtils {

    public static File deleteOnExistAndCreate(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        return file;
    }
    
}
