package com.tienda.gomez.raul.tiendaservicioproductos.constants;

import static com.tienda.gomez.raul.tiendaservicioproductos.constants.Constants.*;

import javax.naming.InvalidNameException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tienda.gomez.raul.tiendaservicioproductos.destino.models.Destino;
import com.tienda.gomez.raul.tiendaservicioproductos.productos.models.Producto;

public class Utils {

    public static Boolean verifyCodigoProveedor(String codigo) throws InvalidFormatException{
        if(codigo == null){
            throw new NullPointerException("El codigo del proveedor no puede ser nulo");
        }
        if(!codigo.matches("\\d{" + CODIGO_PROVEEDOR_LENGTH + "}")){
            throw new InvalidFormatException(null, 
                                                "El codigo del proveedor debe ser numérico y de " + CODIGO_PROVEEDOR_LENGTH + " de longitud", 
                                                codigo, 
                                                null
                                            );
        }

        return true;
    }

    public static Boolean verifyCodigoProducto(String codigo) throws InvalidFormatException{
        if(codigo == null){
            throw new NullPointerException("El codigo del producto no puede ser nulo");
        }
        if(!codigo.matches("\\d{" + CODIGO_PRODUCTO_LENGTH + "}")){
            throw new InvalidFormatException(null, 
                                                "El codigo del producto debe ser numérico y de " + CODIGO_PRODUCTO_LENGTH + " de longitud", 
                                                codigo, 
                                                null
                                            );
        }

        return true;
    }

    public static Boolean verifyCodigoEAN(String codigo) throws InvalidFormatException{
        if(codigo == null){
            throw new NullPointerException("El codigo EAN no puede ser nulo");
        }
        if(!codigo.matches("\\d{" + EAN_LENGTH + "}")){
            throw new InvalidFormatException(null, 
                                                "El codigo EAN debe ser numérico y de " + EAN_LENGTH + " de longitud", 
                                                codigo, 
                                                null
                                            );
        }

        return true;
    }

    /**
     * 
     * @param dest El destino parseado con Destino.fromNumber(x);
     * @return
     * @throws InvalidFormatException
     */
    public static Boolean verifyCodigoDestino(Destino dest) throws InvalidFormatException{
        if(dest == null){
            throw new InvalidFormatException(null, 
                                                "El codigo del destino es inválido o no existe" , 
                                                null, 
                                                null
                                            );
        }

        return true;
    }

    public static Boolean verifyProductoDataExceptCodigo(Producto producto) throws InvalidFormatException, InvalidNameException{
        if(producto.getNombre() == null || producto.getNombre().equals("") )
                throw new InvalidNameException("El nombre no puede estar vacío");
        
        return true;
    }
}
