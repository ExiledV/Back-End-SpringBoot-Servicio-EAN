package com.tienda.gomez.raul.tiendaservicioproductos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TiendaServicioProductosApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiendaServicioProductosApplication.class, args);
	}

}
