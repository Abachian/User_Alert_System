# Ejercicio - Sistema de Alertas

Es probable que estés familiarizado con la funcionalidad de Notificaciones de Facebook: es esa campanita en la parte superior del menú que muestra las nuevas alertas que el sistema tiene para mostrarte sobre diversos temas, como el cumpleaños de un amigo, una página que sigues que comparte una publicación, un amigo que publica una foto, alguien que comenta en tu publicación, una sugerencia de amistad, etc.

Facebook no es el único; en general, todas las aplicaciones tienen un sistema de alertas o notificaciones. En este ejercicio, te pediremos que crees una versión muy simplificada.

Se requiere programar un sistema para enviar alertas a usuarios con la siguiente funcionalidad:

1. Se pueden registrar usuarios que recibirán alertas.
2. Se pueden registrar temas sobre los cuales se enviarán alertas.
3. Los usuarios pueden elegir sobre qué temas quieren recibir alertas.
4. Se puede enviar una alerta sobre un tema y la recibirán todos los usuarios que hayan optado por recibir alertas de ese tema.
5. Se puede enviar una alerta sobre un tema a un usuario específico, y solo ese usuario la recibirá.
6. Una alerta puede tener una fecha y hora de expiración.
7. Hay dos tipos de alertas: Informativas y Urgentes.
8. Un usuario puede marcar una alerta como leída.
9. Se pueden obtener todas las alertas no expiradas de un usuario que aún no las ha leído.
10. Se pueden obtener todas las alertas no expiradas para un tema. Se informa para cada alerta si es para todos los usuarios o para uno específico.
11. Tanto para el punto 9 como para el 10, el ordenamiento de las alertas es el siguiente: las Urgentes van al inicio, siendo la última en llegar la primera en obtenerse (LIFO). Y a continuación las informativas, siendo la primera en llegar la primera en obtenerse (FIFO). Por ejemplo, dadas las siguientes alertas Informativas y Urgentes que llegan en el siguiente orden: I1, I2, U1, I3, U2, I4, se ordenarán de la siguiente forma --> U2, U1, I1, I2, I3, I4.

### Aclaraciones importantes:

- La aplicación se ejecuta desde la línea de comando. En ningún caso pedimos que escribas código de front end, tampoco que hagas impresiones a la consola.
- Debe tener Tests Unitarios.
- No debes hacer ningún tipo de persistencia de datos (base de datos, archivos). Todo debe resolverse con estructuras en memoria.
- Si tienes que hacer algún supuesto sobre algo que no esté claro en el ejercicio, por favor anótalo para que lo tengamos en cuenta al revisar el código.


Al responder el ejercicio, por favor entregá código que funcione y se pueda probar. Se minuciosa en los detalles y en la claridad del código que escribas para que al revisor le sea fácil entenderlo.

Cuando revisamos el ejercicio, esto es lo que evaluamos:

Solución: 
- ¿El código soluciona correctamente los requisitos?
- Programación orientada a objetos: a. ¿Hay un modelo de clases pensado, que modela la realidad del enunciado? b. ¿Está resuelto usando polimorfismo? c. ¿Hay algún patrón de diseño presente en la solución?
- Legibilidad del código: Miramos la elección de los nombres de las variables, los nombres de los métodos y de las clases. ¿Son lo suficientemente representativos como para entender su funcionamiento o propósito, si lo leyera un compañero tuyo sin que vos estés?
- Principio de responsabilidad única: Cada clase, ¿tiene una única responsabilidad?¿Cada método hace una única cosa? (¿O tienen mucho código y se podría refactorizar en varios métodos más cortitos?).
- Unit Test: ¿Hay tests que prueben el correcto funcionamiento de los casos de uso?

## Resolucion

Para poder probar los test unitarios es necesario java 17 o una version mayor

Ejecutar por terminal con el comando 
- .\mvnw test para Linux 
- .\mvnw.cmd test para Windows


### Consideraciones y Decisiones de Diseño

Dentro de las clases principales estan:
    
User: Representa a los usuarios del sistema, los cuales contienen un identificador unico, un nombre y 
la lista de las alarmas que tengan.

Alert: Representa las alertas que se les puede enviar a los usuarios, estas contienen un contenido, el tema 
sobre el que tratan, la fecha de expiracion, un enum que define si es Urgente o Informativa y un HashMap que se usa
para determinar que usuario la leyo. 

Topic: Representa a los temas en el sistema, contienen un nombre, un set de usuarios registrados para no tener repetidos.

Para el inciso 2
2.       Se pueden registrar temas sobre los cuales se enviarán alertas.
Bastaria con tener una clase "Sistema" que contenga una lista de topicos, ya que a partir de ellos los usuarios reciben las alertas,
pero a fines practicos decidi no generarla ya que para probar los demas incisos no era necesario.

Tome la decision de que el topico sea el encargado de enviar las alertas a sus usuarios subscriptos, en el caso del 
inciso 5:
5.     Se puede enviar una alerta sobre un tema a un usuario específico, solo lo recibe ese único usuario.

Interprete que a pesar de que se le puede enviar una alerta de forma especifica a un usuario sigue vigente la restriccion
de que este debe estar subscrito al tema.

Para el inciso 7:
7.      Hay dos tipos de alertas: Informativas y Urgentes.

En primera instancia habia pensado en utilizar herencia y tener dos clases separadas para cada tipo de alerta
pero al no tener mas diferencia que la forma de como se ordenan decidi crear el enum AlertType y que cada vez que
se agregue a la lista se haga en el orden correspondiente.

Para el inciso 8 y segunda parte del 11:
 -  Un usuario puede marcar una alerta como leída.
 - Se informa para cada alerta si es para todos los usuarios o para uno específico.

Con el HashMap userAlerted de la clase Alert se puede determinar que usuario leyo la alerta y
si es para un unico usuario en el caso que su tamaño sea 1.




Para poder probar el funcionamiento del sistema se debe realizar a traves de los test unitarios los cuales fueron creados
para cada caso del enunciado.



