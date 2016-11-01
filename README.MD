# Sistemas Distribuidos – Tarea 2: RMI & MoM

![alt text](ups.png?raw=true "Me pase po")

### Requerimientos
El programa requiere `Java 1.8` y `rmiregistry` en el sistema.

### Makefile
El makefile tiene las siguientes opciones:

- `make` Compila el servidor y el cliente
- `make client` Compila el cliente
- `make server` Compila el servidor
- `make run-server` Ejecuta el servidor
- `make run-client` Ejecuta el cliente
- `make clean` Limpia la carpeta compilada

**Importante**: al ejecutar `make run-server` se ejecuta automaticamente `rmiregistry`. Una vez que el servidor es terminado, `rmiregistry` es cerrado por el makefile con el siguiente comando automaticamente:

```bash
killall rmiregistry
```

Luego para levantar el servidor no es necesario iniciar rmiregistry, el makefile se encarga de levantarlo y levantarlo cuando es necesario.
