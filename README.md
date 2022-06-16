# challenge-pokedex
Repositorio con exposición de APIs REST e integración con Pokedex APIs

------ Despliegue de la aplicación ------

Para empezar, se privilegia el uso de Docker para el despliegue de la aplicación con tal asegurar que la aplicación
se comporte como un microservicio (auto contenida, aislada, liviana, etc.).

1- Generar JAR del proyecto con Maven.
-> mvn clean package

2- Construir imagen docker del proyecto según el JAR generado anteriormente. La imagen creada para este caso, tiene el nombre de "pokedex_image".
-> docker build -t pokedex_image .

#El despliegue se realiza con la ayuda de los servicios de AWS. Por esta razón, se recomienda usar AWS CLI con tal de proceder al deploy.
3- Una vez configurado el usuario de AWS-CLI en nuestro local, se debe autenticar el usuario para acceder al servicio Amazon ECR (Registro de contenedores Docker).
-> aws ecr get-login-password --region <REGION_SELECCIONADA> | docker login --username AWS --password-stdin <ID_CUENTA_AWS>.dkr.ecr.<REGION_SELECCIONADA>.amazonaws.com

4- En ECR se ha creado un repositorio llamado "apt" en el cual se puede subir la imagen de Docker generada en el punto 2.
Sin embargo, primero se debe señalar el tag que hace referencia a la imagen generada de forma local.
-> docker tag pokedex_image:latest <ID_CUENTA_AWS>.dkr.ecr.<REGION_SELECCIONADA>.amazonaws.com/apt

5- Se procede a subir la imagen al repositorio ECR señalado en el punto 4
-> docker push <ID_CUENTA_AWS>.dkr.ecr.<REGION_SELECCIONADA>.amazonaws.com/apt

6- Por último, para finalizar el despliegue se ha dispuesto de un cluster que gestiona las imagenes de docker con AMAZON ECS.
En este sentido, se debe ejecutar la siguiente instrucción para realizar un nuevo despliegue cada vez que se contemple subir una nueva imagen docker a AMAZON ECR.
-> aws ecs update-service --cluster <NOMBRE_CLUSTER_ECR> --service <NOMBRE_SERVICIO_ECR> --force-new-deployment --<REGION_SELECCIONADA>

*NOTA: Para cada nuevo deploy la IP Pública de la aplicación también cambia. Esto se podría solucionar configurando un balanceador
de carga a las tareas de deploy en el cluster de AMAZON ECS y de esta forma el cliente de esta API no pierda conexión con el servicio ante nuevos despliegues.
