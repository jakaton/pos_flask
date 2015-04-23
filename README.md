# POS tagging

Librería para poner un servicio web de etiquetación de categorías 
gramaticales. Por lo pronto español e inglés basadons en el parse de Stanford.

# Requisitos

* Java 8
* CoreNLP de Stanford 

Para su instalación ver instrucciones en `lib/README.md`

# Peticiones REST a servicio web

## Algunos ejemplos


### Etiquetando oraciones desde la url

Este método se pude probar también usando un navegador

    curl http://URL/pos/api/v1.0/tag/este%20es%20un%20ejemplo

Si gustamos podemos definir el idioma a analizar

    curl http://URL/pos/api/v1.0/tag/es/este%20es%20un%20ejemplo
    curl http://URL/pos/api/v1.0/tag/en/this%20is%20an%20example

### Etiquetando orraciones con método POST

Usar el método POST nos permite etiquetar más de una oración al mismo tiempo

    curl -X POST -T ARCHIVO http://URL/pos/api/v1.0/tag

Si gustamos podemos definir el idioma a analizar

    curl -X POST -T ARCHIVO http://URL/pos/api/v1.0/tag/es
    curl -X POST -T ARCHIVO http://URL/pos/api/v1.0/tag/en
