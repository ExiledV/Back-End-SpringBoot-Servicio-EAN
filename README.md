# Prueba técnica: Servicio de Productos Tienda

## Despricpión

Este documento describe la prueba técnica realizada para un puesto de desarrollador Java. El objetivo de la prueba es desarrollar un microservicio RESTful que gestione productos, proveedores y códigos EAN.

## Instalación

1. Postman: Herramienta para realizar pruebas de API.

## Uso

1. Levantar el microservicio:

- Acceda a la carpeta servicio-productos.
- Ejecute el siguiente comando en el terminal **`./mvnw spring-boot:run`**

2. Probar el CRUD:

- Importe las requests disponibles en la carpeta raíz del repositorio en Postman.
- Ejecute las requests para probar las funcionalidades CRUD de productos, proveedores y códigos EAN.

## Tecnologías usadas

- **Base de datos**: H2 Database
- **Persistencia**: Spring JPA
- **Desarrollo Web**: Spring Web
- **Testing**: Mockito, JUnit
- **Caché**: Caffeine

## Modelado de los datos

Se han creado las siguientes entidades:

1. **Productos**
2. **Proveedores**
3. **EAN**

Para el destino, debido a la especificidad aportada en el pdf con el enunciado, se ha considerado que no escalará apenas, es por eso que no se ha establecido ninguna base de datos para las funcionalidades del destino. En su lugar, se ha utilizado un Enum.

Se ha creado la entidad EAN como tabla aparte ya que, debido al desconocimiento del negocio y a la poca información que aporta el enunciado, se ha supuesto que Productos podria conllevar a tener algunos atributos comunes en todos ellos independientemente de su proveedor o su destino.

## Testing

He decidido realizar los tests automaticos con Mockito y Junit, con el propósito de automatizar las pruebas y los diferentes comportamientos que debería tener en cuenta la aplicación.
Las clases utilizadas para
Para ejecutar los tests unitarios, usar el comando:

- `./mvnw test`

## Trabajo futuro

Debido a la tardanza en la toma de la mejor decisión posible en cuanto a modelado de datos, falta un poco de trabajo para terminar la prueba técnica que me gustaría entregar. En esta sección se describirán las cosas que realizaría en caso de disponer un poco de tiempo extra.

### Estructura por microservicios y securización

En esta prueba, se ha entregado un microservicio de productos de las etiquetas EAN asociadas a los productos de una tienda, pero en caso de disponer de mas tiempo, profundizaría en su estructura, e incluiría unos microservicios extra. Crearía una puerta de entrada con la dependencia de Spring Cloud Gateway y un nuevo microservicio de Autenticacion para la autenticación del usuario con JWT.

Los microservicios extra serían:

- **Gateway**: Spring security para la securizacion de las rutas, metiendo un validador que desencripte el token con los roles del usuario
- **Servicio Auth**: Spring security para la encriptacion de la contraseña, y mandarla al servicio Usuarios. También para la validación del usuario y generación del token, teniendo en cuenta los comportamientos de si falla o acierta la validación.
- **Servicio Usuarios**: servicio usuarios que simplemente se conecta con el gateway y el servicio de autenticacion.

Por supuesto, también se conectaría el servicio productos entregado al servicio Gateway.

Con esto, el tema de la securización estaría cubierto.

### Patrones a implementar

En caso de que la aplicación llegase a ser mas grande, seguramente incorporaría el **Patrón DTO** para el traspaso de Entidades con menos datos.

## Conclusiones personales

He intentado hacer lo mejor que he podido en esta prueba. Es mi primera prueba técnica, por lo cual no se que cosas eran necesarias implementar y cuales no. Yo me he dedicado a hacer todo el crud lo mejor posible para que se comprueben mis habilidades programando y mis conocimientos con buenas prácicas. He priorizado sobre todo eso.

Autor:
Raúl Gómez Beteta.
